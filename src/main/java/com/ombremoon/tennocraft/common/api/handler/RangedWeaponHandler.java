package com.ombremoon.tennocraft.common.api.handler;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.IModHolder;
import com.ombremoon.tennocraft.common.api.mod.ModContainer;
import com.ombremoon.tennocraft.common.api.mod.Modification;
import com.ombremoon.tennocraft.common.api.mod.WeaponModContainer;
import com.ombremoon.tennocraft.common.api.weapon.TriggerType;
import com.ombremoon.tennocraft.common.api.weapon.schema.RangedAttackSchema;
import com.ombremoon.tennocraft.common.api.weapon.schema.RangedWeaponSchema;
import com.ombremoon.tennocraft.common.api.weapon.schema.Schema;
import com.ombremoon.tennocraft.common.init.TCAttributes;
import com.ombremoon.tennocraft.common.init.TCData;
import com.ombremoon.tennocraft.util.Loggable;
import com.ombremoon.tennocraft.util.ModHelper;
import com.ombremoon.tennocraft.util.WeaponDamageResult;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class RangedWeaponHandler implements ModHandler, Loggable {
    public static final Codec<RangedWeaponHandler> CODEC = Codec.withAlternative(
            RecordCodecBuilder.create(
                    instance -> instance.group(
                            CompoundTag.CODEC.fieldOf("tag").forGetter(handler -> handler.tag),
                            Schema.DIRECT_CODEC.fieldOf("schema").forGetter(handler -> handler.schema)
                    ).apply(instance, RangedWeaponHandler::forCodec)
            ),
            RecordCodecBuilder.create(
                    instance -> instance.group(
                            CompoundTag.CODEC.fieldOf("tag").forGetter(handler -> handler.tag),
                            Schema.DIRECT_CODEC.fieldOf("schema").forGetter(handler -> handler.schema)
                    ).apply(instance, RangedWeaponHandler::forCodec)
            )
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, RangedWeaponHandler> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG, handler -> handler.tag,
            Schema.STREAM_CODEC, handler -> handler.schema,
            RangedWeaponHandler::forCodec
    );

    private final CompoundTag tag;
    private final RangedWeaponSchema schema;
    private final List<RangedAttackSchema> attacks = new ObjectArrayList<>();
    private final WeaponModContainer mods;
    private TriggerType triggerType;
    @Nullable
    private HolderLookup.Provider registries;

    private static RangedWeaponHandler forCodec(CompoundTag tag, Schema schema) {
        return new RangedWeaponHandler(tag, schema, null);
    }

    public RangedWeaponHandler(CompoundTag tag, Schema schema, @Nullable HolderLookup.Provider registries) {
        this.tag = tag;
        this.schema = (RangedWeaponSchema) schema;
        this.registries = registries;

        Modification.Compatibility compatibility = this.schema.getGeneral().layout().compatibility();
        this.triggerType = this.getTriggerType();
        this.attacks.addAll(this.schema.getProperties().getAttacks(this.triggerType));
        this.mods = new WeaponModContainer(compatibility, this.schema);
        this.loadFromTag(tag, registries);
    }

    public void setRegistries(HolderLookup.Provider registries) {
        if (this.registries == null) {
            this.registries = registries;
            this.mods.deserializeNBT(registries, this.tag);
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

    public void cycleAlternateFire(ItemStack stack) {
        Mutable mutable = new Mutable(this);
        mutable.cycleAlternateFire(this.triggerType);
        stack.set(TCData.RANGED_WEAPON_HANDLER, mutable.toImmutable());
    }

    public void handleBulletDamage(ItemStack stack, LivingEntity target, WeaponDamageResult.Partial partial) {
    }

    public void confirmModChanges(Player player, ItemStack stack) {
        Mutable mutable = new Mutable(this);
        mutable.confirmModChanges(player, stack, this.mods);
        stack.set(TCData.RANGED_WEAPON_HANDLER, mutable.toImmutable());
    }

    public CompoundTag getTag() {
        return this.tag;
    }

    public RangedWeaponSchema getSchema() {
        return this.schema;
    }

    public WeaponModContainer getMods() {
        return this.mods;
    }

    public TriggerType getTriggerType() {
        if (this.tag.contains("TriggerType")) {
            TriggerType type = TriggerType.byName(this.tag.getString("TriggerType"));
            if (this.triggerType != type) {
                this.triggerType = type;
            }

            return this.triggerType;
        }

        return schema.getGeneral()
                .triggerTypes()
                .map(List::getFirst)
                .orElse(TriggerType.AUTO);
    }

    private void loadFromTag(CompoundTag tag, HolderLookup.Provider registries) {
        if (registries != null) {
            this.mods.deserializeNBT(registries, tag);
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof RangedWeaponHandler handler && handler.tag == this.tag && handler.schema == this.schema;
    }

    @Override
    public int hashCode() {
        return this.tag.hashCode();
    }

    public static class Mutable {
        private final CompoundTag tag;
        private final RangedWeaponSchema schema;
        private final HolderLookup.Provider registries;

        public Mutable(RangedWeaponHandler handler) {
            this.tag = handler.tag;
            this.schema = handler.schema;
            this.registries = handler.registries;
        }

        public void confirmModChanges(Player player, ItemStack stack, WeaponModContainer mods) {
            mods.confirmMods(player, (IModHolder<?>) stack.getItem(), stack);
            this.saveChanges(mods);
        }

        public <T extends TriggerType> void cycleAlternateFire(T triggerType) {
            var type = findNextTypeInList(this.schema.getProperties().triggers(), triggerType);
            if (type != triggerType) {
                this.tag.putString("TriggerType", type.getSerializedName());
            }
        }

        private static <T> T findNextTypeInList(Collection<T> list, T current) {
            Iterator<T> iterator = list.iterator();

            while (iterator.hasNext()) {
                if (iterator.next().equals(current)) {
                    if (iterator.hasNext()) {
                        return iterator.next();
                    }
                    return list.iterator().next();
                }
            }

            return list.isEmpty() ? current : list.iterator().next();
        }

        private void saveChanges(WeaponModContainer mods) {
            if (this.registries != null)
                this.tag.put("Mods", mods.serializeNBT(this.registries));
        }

        public RangedWeaponHandler toImmutable() {
            return new RangedWeaponHandler(this.tag, this.schema, this.registries);
        }
    }
}
