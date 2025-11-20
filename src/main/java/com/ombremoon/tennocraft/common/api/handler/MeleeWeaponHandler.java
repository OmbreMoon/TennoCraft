package com.ombremoon.tennocraft.common.api.handler;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.IModHolder;
import com.ombremoon.tennocraft.common.api.mod.Modification;
import com.ombremoon.tennocraft.common.api.mod.WeaponModContainer;
import com.ombremoon.tennocraft.common.api.weapon.schema.AttackSchema;
import com.ombremoon.tennocraft.common.api.weapon.schema.MeleeUtilitySchema;
import com.ombremoon.tennocraft.common.api.weapon.schema.MeleeWeaponSchema;
import com.ombremoon.tennocraft.common.api.weapon.schema.Schema;
import com.ombremoon.tennocraft.common.init.TCData;
import com.ombremoon.tennocraft.util.Loggable;
import com.ombremoon.tennocraft.util.WeaponDamageResult;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import javax.annotation.Nullable;

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
    public static final StreamCodec<RegistryFriendlyByteBuf, MeleeWeaponHandler> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG, handler -> handler.tag,
            Schema.STREAM_CODEC, handler -> handler.schema,
            MeleeWeaponHandler::forCodec
    );

    private final CompoundTag tag;
    private final MeleeWeaponSchema schema;
    private final AttackSchema attacks;
    private final WeaponModContainer mods;
    private final AttributeMap stats;
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
        this.mods = new WeaponModContainer(compatibility, this.schema);
        this.stats = new AttributeMap(createMeleeWeaponAttributes(this.schema.getUtility(), this.attacks));

        if (registries != null && tag.contains("Mods")) {
            this.mods.deserializeNBT(registries, tag.getCompound("Mods"));
        }

        if (tag.contains("Stats", 9)) {
            ListTag listTag = tag.getList("Stats", 10);
            this.stats.load(listTag);
        }
    }

    public void setRegistries(HolderLookup.Provider registries) {
        if (this.registries == null) {
            this.registries = registries;
            if (this.tag.contains("Mods")) {
                this.mods.deserializeNBT(registries, this.tag.getCompound("Mods"));
            }
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

    public void handleComboModifiers(ItemStack stack, LivingEntity target, WeaponDamageResult.Partial partial) {

    }

    public void confirmModChanges(Level level, ItemStack stack) {
        Mutable mutable = new Mutable(this);
        mutable.confirmModChanges(level, stack, this.mods, this.stats);
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

    public AttributeMap getStats() {
        return this.stats;
    }

    private static AttributeSupplier createMeleeWeaponAttributes(MeleeUtilitySchema utility, AttackSchema attacks) {
        return AttributeSupplier.builder()
                .add(Attributes.ATTACK_SPEED, utility.attackSpeed())
                .build();
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

        public void confirmModChanges(Level level, ItemStack stack, WeaponModContainer mods, AttributeMap stats) {
            mods.confirmMods(level,
                    (IModHolder<?>) stack.getItem(), stack);
            this.saveChanges(mods, stats);
        }

        private void saveChanges(WeaponModContainer mods, AttributeMap stats) {
            this.saveMods(mods);
            this.saveStats(stats);
        }

        private void saveMods(WeaponModContainer mods) {
            if (this.registries != null)
                this.tag.put("Mods", mods.serializeNBT(this.registries));
        }

        private void saveStats(AttributeMap stats) {
            ListTag statList = stats.save();
            this.tag.put("Stats", statList);
        }

        public MeleeWeaponHandler toImmutable() {
            return new MeleeWeaponHandler(this.tag, this.schema, this.registries);
        }
    }
}
