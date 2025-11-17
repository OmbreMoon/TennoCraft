package com.ombremoon.tennocraft.common.api.handler;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.mod.ModContainer;
import com.ombremoon.tennocraft.common.api.mod.Modification;
import com.ombremoon.tennocraft.common.api.weapon.schema.AttackSchema;
import com.ombremoon.tennocraft.common.api.weapon.schema.MeleeUtilitySchema;
import com.ombremoon.tennocraft.common.api.weapon.schema.MeleeWeaponSchema;
import com.ombremoon.tennocraft.common.api.weapon.schema.Schema;
import com.ombremoon.tennocraft.common.init.TCData;
import com.ombremoon.tennocraft.common.world.item.IModHolder;
import com.ombremoon.tennocraft.util.DamageResult;
import com.ombremoon.tennocraft.util.Loggable;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
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

    private final CompoundTag tag;
    private final MeleeWeaponSchema schema;
    private final AttackSchema attacks;
    private final ModContainer mods;
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
        this.mods = new ModContainer(compatibility);
        this.stats = new AttributeMap(createMeleeWeaponAttributes(this.schema.getUtility(), this.attacks));

        if (registries != null) {
            this.mods.deserializeNBT(registries, tag);
        }

        if (tag.contains("Stats")) {
            ListTag listTag = tag.getList("Stats", 10);
            this.stats.load(listTag);
        }
    }

    public void setRegistries(HolderLookup.Provider registries) {
        if (this.registries == null) {
            this.registries = registries;
            this.mods.deserializeNBT(registries, this.tag);
        }
    }

    public void ensureRegistryAccess(ItemStack stack) {
        if (this.registries == null) {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                setRegistries(server.registryAccess());
            }
        }
    }

    public void handleComboModifiers(ItemStack stack, DamageResult.Partial partial) {

    }

    public void confirmModChanges(ItemStack stack) {
        Mutable mutable = new Mutable(this);
        mutable.confirmModChanges(stack, this.mods, this.stats);
        stack.set(TCData.MELEE_WEAPON_HANDLER, mutable.toImmutable());
    }

    public CompoundTag getTag() {
        return this.tag;
    }

    public MeleeWeaponSchema getSchema() {
        return this.schema;
    }

    public ModContainer getMods() {
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

        public void confirmModChanges(ItemStack stack, ModContainer mods, AttributeMap stats) {
            mods.confirmMods((IModHolder<?>) stack.getItem(), stack);
            this.saveChanges(mods, stats);
        }

        private void saveChanges(ModContainer mods, AttributeMap stats) {
            this.saveMods(mods);
            this.saveStats(stats);
        }

        private void saveMods(ModContainer mods) {
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
