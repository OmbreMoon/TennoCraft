package com.ombremoon.tennocraft.common.modholder;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.init.TCAttributes;
import com.ombremoon.tennocraft.common.init.TCData;
import com.ombremoon.tennocraft.common.modholder.api.mod.ModContainer;
import com.ombremoon.tennocraft.common.modholder.api.mod.Modification;
import com.ombremoon.tennocraft.common.modholder.api.weapon.TriggerType;
import com.ombremoon.tennocraft.common.modholder.api.weapon.schema.RangedAttackSchema;
import com.ombremoon.tennocraft.common.modholder.api.weapon.schema.RangedWeaponSchema;
import com.ombremoon.tennocraft.common.modholder.api.weapon.schema.Schema;
import com.ombremoon.tennocraft.common.world.item.IModHolder;
import com.ombremoon.tennocraft.util.Loggable;
import com.ombremoon.tennocraft.util.ModHelper;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import javax.annotation.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class RangedWeaponHandler implements Loggable {
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

    private final CompoundTag tag;
    private final RangedWeaponSchema schema;
    private final List<RangedAttackSchema> attacks = new ObjectArrayList<>();
    private final ModContainer mods;
    private final AttributeMap stats;
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
        this.mods = new ModContainer(compatibility);
        if (tag.contains("Mods", 9) && registries != null) {
            ListTag listTag = tag.getList("Mods", 10);
            this.mods.load(listTag, registries);
        }

        this.stats = new AttributeMap(createRangedWeaponAttributes(this.attacks.getFirst()));
        if (tag.contains("Stats")) {
            CompoundTag nbt = tag.getCompound("Stats");
            String trigger = this.triggerType.getSerializedName();
            if (nbt.contains(trigger, 9)) {
                ListTag listTag = nbt.getList(this.triggerType.getSerializedName(), 10);
                this.stats.load(listTag);
            }
        }
    }

    public void setRegistries(HolderLookup.Provider registries) {
        if (this.registries == null) {
            this.registries = registries;
            if (this.tag.contains("Mods", 9)) {
                ListTag listTag = this.tag.getList("Mods", 10);
                this.mods.load(listTag, registries);
            }
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

    public void cycleAlternateFire(ItemStack stack) {
        Mutable mutable = new Mutable(this);
        mutable.cycleAlternateFire(stack, this.triggerType);
        stack.set(TCData.RANGED_WEAPON_HANDLER, mutable.toImmutable());
    }

    public void confirmModChanges(ItemStack stack) {
        Mutable mutable = new Mutable(this);
        mutable.confirmModChanges(stack, this.mods, this.stats, this.triggerType);
        stack.set(TCData.RANGED_WEAPON_HANDLER, mutable.toImmutable());
    }

    public CompoundTag getTag() {
        return this.tag;
    }

    public RangedWeaponSchema getSchema() {
        return this.schema;
    }

    public ModContainer getMods() {
        return this.mods;
    }

    public AttributeMap getStats() {
        return this.stats;
    }

    public TriggerType getTriggerType() {
        if (this.tag.contains("TriggerType")) {
            return TriggerType.byName(this.tag.getString("TriggerType"));
        }

        return schema.getGeneral().triggerTypes().getFirst();
    }

    private static AttributeSupplier createRangedWeaponAttributes(RangedAttackSchema schema) {
        return AttributeSupplier.builder()
                .add(TCAttributes.CRIT_CHANCE, schema.getCritChance())
                .build();
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
        @Nullable
        private final HolderLookup.Provider registries;

        public Mutable(RangedWeaponHandler handler) {
            this.tag = handler.tag;
            this.schema = handler.schema;
            this.registries = handler.registries;
        }

        public void confirmModChanges(ItemStack stack, ModContainer mods, AttributeMap stats, TriggerType triggerType) {
            mods.confirmMods((IModHolder<?>) stack.getItem(), stack);
            ListTag modList = new ListTag();
            modList = mods.save(modList, this.registries);
            this.tag.put("Mods", modList);
            this.saveStats(stats, triggerType);
        }

        public <T extends TriggerType> void cycleAlternateFire(ItemStack stack, T triggerType) {
            var type = findNextTypeInList(this.schema.getProperties().triggers(), triggerType);
            if (type != triggerType) {
                this.tag.putString("TriggerType", type.getSerializedName());
                this.setAlternateFireAttributes(stack, type);
            }
        }

        private void setAlternateFireAttributes(ItemStack stack, TriggerType type) {
            var attacks = this.schema.getProperties().getAttacks(type);
            var stats = new AttributeMap(createRangedWeaponAttributes(attacks.getFirst()));
            ModHelper.forEachModifier(stack, (attributeHolder, attributeModifier) -> {
                AttributeInstance instance = stats.getInstance(attributeHolder);
                if (instance != null) {
                    instance.removeModifier(attributeModifier);
                }

                //Stop Location Based Effects
            });
            ModHelper.forEachModifier(stack, (attributeHolder, attributeModifier) -> {
                AttributeInstance instance = stats.getInstance(attributeHolder);
                if (instance != null) {
                    instance.removeModifier(attributeModifier.id());
                    instance.addPermanentModifier(attributeModifier);
                }

                //Run Location Changed Effects
            });

            this.saveStats(stats, type);
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

        private void saveStats(AttributeMap stats, TriggerType triggerType) {
            CompoundTag nbt = new CompoundTag();
            ListTag statList = stats.save();
            nbt.put(triggerType.getSerializedName(), statList);
            this.tag.put("Stats", nbt);
        }

        public RangedWeaponHandler toImmutable() {
            return new RangedWeaponHandler(this.tag, this.schema, this.registries);
        }
    }
}
