package com.ombremoon.tennocraft.common.api.handler;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.IModHolder;
import com.ombremoon.tennocraft.common.api.mod.Modification;
import com.ombremoon.tennocraft.common.api.mod.WeaponModContainer;
import com.ombremoon.tennocraft.common.api.weapon.TriggerType;
import com.ombremoon.tennocraft.common.api.weapon.schema.AttackSchema;
import com.ombremoon.tennocraft.common.api.weapon.schema.MeleeUtilitySchema;
import com.ombremoon.tennocraft.common.api.weapon.schema.MeleeWeaponSchema;
import com.ombremoon.tennocraft.common.api.weapon.schema.Schema;
import com.ombremoon.tennocraft.common.api.weapon.schema.data.AttackMultiplier;
import com.ombremoon.tennocraft.common.api.weapon.schema.data.ComboSet;
import com.ombremoon.tennocraft.common.api.weapon.schema.data.ComboType;
import com.ombremoon.tennocraft.common.init.TCData;
import com.ombremoon.tennocraft.common.world.PlayerCombo;
import com.ombremoon.tennocraft.main.Keys;
import com.ombremoon.tennocraft.util.Loggable;
import com.ombremoon.tennocraft.util.WeaponDamageResult;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.apache.commons.lang3.mutable.MutableFloat;

import javax.annotation.Nullable;
import java.util.List;

public class MeleeWeaponHandler implements ModHandler, Loggable {
    public static final Codec<MeleeWeaponHandler> CODEC = Codec.withAlternative(
            RecordCodecBuilder.create(
                    instance -> instance.group(
                            CompoundTag.CODEC.fieldOf("tag").forGetter(handler -> handler.tag),
                            Schema.DIRECT_CODEC.fieldOf("schema").forGetter(handler -> handler.schema)
                    ).apply(instance, MeleeWeaponHandler::forCodec)
            ),
            RecordCodecBuilder.create(
                    instance -> instance.group(
                            CompoundTag.CODEC.fieldOf("tag").forGetter(handler -> handler.tag),
                            Schema.DIRECT_CODEC.fieldOf("schema").forGetter(handler -> handler.schema)
                    ).apply(instance, MeleeWeaponHandler::forCodec)
            )
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, MeleeWeaponHandler> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public MeleeWeaponHandler decode(RegistryFriendlyByteBuf buffer) {
            CompoundTag tag = ByteBufCodecs.COMPOUND_TAG.decode(buffer);
            Schema schema = Schema.STREAM_CODEC.decode(buffer);
            return new MeleeWeaponHandler(tag, schema, buffer.registryAccess());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buffer, MeleeWeaponHandler handler) {
            ByteBufCodecs.COMPOUND_TAG.encode(buffer, handler.tag);
            Schema.STREAM_CODEC.encode(buffer, handler.schema);
        }
    };

    private final CompoundTag tag;
    private final MeleeWeaponSchema schema;
    private final AttackSchema attacks;
    private Holder<ComboSet> comboSet;
    private final WeaponModContainer mods;
    @Nullable
    private HolderLookup.Provider registries;

    private static MeleeWeaponHandler forCodec(CompoundTag tag, Schema schema) {
        return new MeleeWeaponHandler(tag, schema, null);
    }

    public MeleeWeaponHandler(CompoundTag tag, Schema schema, @Nullable HolderLookup.Provider registries) {
        this.tag = tag;
        this.schema = (MeleeWeaponSchema) schema;
        this.registries = registries;

        Modification.Compatibility compatibility = this.schema.getGeneral().layout().compatibility();
        this.attacks = this.schema.getAttacks().attack();
        this.comboSet = this.schema.getAttacks().combo();
        this.mods = new WeaponModContainer(compatibility, this.schema);
        this.loadFromTag(tag, registries);
    }

    public void setRegistries(HolderLookup.Provider registries) {
        if (this.registries == null) {
            this.registries = registries;
            this.loadFromTag(this.tag, registries);
        }
    }

    public void ensureRegistryAccess() {
        if (this.registries == null) {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                setRegistries(server.registryAccess());
            }
        }
    }

    //MAKE COMBO PREDICATE

    public void handleComboModifiers(ItemStack stack, LivingEntity attacker, LivingEntity target, WeaponDamageResult.Partial partial) {
        ComboSet comboSet = this.comboSet.value();
        var distribution = partial.getDistribution();
        float damage = partial.getDamage();

        PlayerCombo combo = attacker.getData(TCData.PLAYER_COMBO);
        ComboType comboType = combo.getComboType();
        comboType = ComboType.NEUTRAL;
        int comboCount = combo.getComboCount();
        var comboValues = comboSet.combos().get(comboType);
        AttackMultiplier multiplier = comboValues.get(combo.getComboIndex()).multipliers().get(combo.getHitIndex());
        MutableFloat delta = new MutableFloat();
        var physicalDistribution = multiplier.getDistribution();
        if (physicalDistribution != null) {
            physicalDistribution.forEach((damageType, amount) -> {
                float modifier = distribution.getAmount(damageType) * (1.0F + amount);
                distribution.replace(damageType, modifier);
                delta.add(modifier);
            });
        }

        damage += delta.floatValue();
        damage *= multiplier.getMultiplier();
        if (comboType == ComboType.HEAVY) {
            float comboMultiplier = 1.0F + comboCount / 20.0F;
            int comboCountReduction = comboCount - comboCount;
            combo.setComboCount(comboCountReduction);
            damage *= comboMultiplier;
        }

        partial.setDamage(damage);
    }

    public void confirmModChanges(Player player, ItemStack stack) {
        Mutable mutable = new Mutable(this);
        mutable.confirmModChanges(player, stack, this.mods);
        stack.set(TCData.MELEE_WEAPON_HANDLER, mutable.toImmutable());
    }

    public CompoundTag getTag() {
        return this.tag;
    }

    public MeleeWeaponSchema getSchema() {
        return this.schema;
    }

    public WeaponModContainer getMods() {
        return this.mods;
    }

    public Holder<ComboSet> getComboSet() {
        return this.comboSet;
    }

    private void loadFromTag(CompoundTag tag, HolderLookup.Provider registries) {
        if (registries != null) {
            if (tag.contains("Mods"))
                this.mods.deserializeNBT(registries, tag.getCompound("Mods"));

            if (tag.contains("Combo")) {
                ResourceLocation combo = ResourceLocation.tryParse(tag.getString("Combo"));
                if (combo != null) {
                    this.comboSet = registries.holderOrThrow(ResourceKey.create(Keys.COMBO_SET, combo));
                }
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MeleeWeaponHandler handler && handler.tag == this.tag && handler.schema == this.schema;
    }

    @Override
    public int hashCode() {
        return this.tag.hashCode();
    }

    public static class Mutable {
        private final CompoundTag tag;
        private final MeleeWeaponSchema schema;
        @Nullable
        private final HolderLookup.Provider registries;

        public Mutable(MeleeWeaponHandler handler) {
            this.tag = handler.tag;
            this.schema = handler.schema;
            this.registries = handler.registries;
        }

        public void confirmModChanges(Player player, ItemStack stack, WeaponModContainer mods) {
            mods.confirmMods(player, (IModHolder<?>) stack.getItem(), stack);
            this.saveChanges(mods);
        }

        private void saveChanges(WeaponModContainer mods) {
            if (this.registries != null)
                this.tag.put("Mods", mods.serializeNBT(this.registries));
        }

        public MeleeWeaponHandler toImmutable() {
            return new MeleeWeaponHandler(this.tag, this.schema, this.registries);
        }
    }
}
