package com.ombremoon.tennocraft.common.api.mod;

import com.ombremoon.tennocraft.common.api.mod.effects.ModValueEffect;
import com.ombremoon.tennocraft.common.api.weapon.DamageValue;
import com.ombremoon.tennocraft.common.api.weapon.schema.Schema;
import com.ombremoon.tennocraft.common.api.IModHolder;
import com.ombremoon.tennocraft.util.ModHelper;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.List;
import java.util.Map;

public class ModContainer implements INBTSerializable<CompoundTag> {
    public static final int AURA_SLOT = 0;
    public static final int EXILUS_SLOT = 1;
    public static final int STANCE_SLOT = 0;
    public final NonNullList<ModInstance> mods;
    public final Modification.Compatibility type;
    protected final Schema schema;
    public final List<DamageValue> damageModifiers = new ObjectArrayList<>();
    public final Map<DataComponentType<List<ModValueEffect>>, Float> absoluteModifiers = new Object2FloatOpenHashMap<>();
    public ModContainer modCache;
    private int maxCapacity;
    private int capacity;

    public ModContainer(Modification.Compatibility type, Schema schema) {
        this(type, schema, false);
    }
/**/
    ModContainer(Modification.Compatibility type, Schema schema, boolean cache) {
        this.type = type;
        this.mods = NonNullList.withSize(type.getMaxSlots(), ModInstance.EMPTY);
        this.schema = schema;

        if (!cache) {
            this.modCache = new ModContainer(type, schema, true);
        }
    }

    public Modification.Compatibility getCompatibility() {
        return this.type;
    }

    public int getFreeSlot() {
        for(int i = 0; i < this.mods.size(); ++i) {
            if (this.mods.get(i).isEmpty()) {
                return i;
            }
        }

        return -1;
    }

    public boolean isAuraSlot(int slot) {
        return this.getCompatibility() == Modification.Compatibility.FRAME && slot == AURA_SLOT;
    }

    public void setMod(int slot, ModInstance mod) {
        this.mods.set(slot, mod);
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag compoundTag = new CompoundTag();
        ListTag modList = new ListTag();
        for (int i = 0; i < this.mods.size(); i++) {
            ModInstance mod = this.mods.get(i);
            if (!mod.isEmpty()) {
                CompoundTag tag = new CompoundTag();
                tag.putByte("Slot", (byte) i);
                tag.put("ModInstance", mod.save(provider));
                modList.add(tag);
            }
        }
        compoundTag.put("Mods", modList);

        ListTag modifierList = new ListTag();
        for (var modifier : this.damageModifiers) {
            CompoundTag tag = new CompoundTag();
            ResourceKey<DamageType> resourcekey = modifier.damageType().getKey();
            tag.putString("DamageType", resourcekey.location().toString());
            tag.putFloat("Modifier", modifier.amount());
            modifierList.add(tag);
        }
        compoundTag.put("Damage Modifiers", modifierList);
        return compoundTag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        this.mods.clear();

        if (nbt.contains("Mods", 9)) {
            ListTag listTag = nbt.getList("Mods", 10);
            for (int i = 0; i < listTag.size(); i++) {
                CompoundTag compoundTag = listTag.getCompound(i);
                if (compoundTag.contains("ModInstance")) {
                    ModInstance mod = ModInstance.load(compoundTag, provider);
                    if (!mod.isEmpty())
                        this.mods.set(compoundTag.getByte("Slot"), mod);
                }
            }
        }

        if (nbt.contains("Damage Modifiers", 9)) {
            ListTag listTag = nbt.getList("Damage Modifiers", 10);
            for (int i = 0; i < listTag.size(); i++) {
                CompoundTag compoundTag = listTag.getCompound(i);
                ResourceKey<DamageType> damageType = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.parse(compoundTag.getString("DamageType")));
                float modifier = compoundTag.getFloat("Modifier");
                this.damageModifiers.add(new DamageValue(provider.holderOrThrow(damageType), modifier));
            }
        }
    }

    public void loadCache() {
        this.modCache.replaceWith(this);
    }

    public int getContainerSize() {
        return this.mods.size();
    }

    public boolean isEmpty() {
        for (ModInstance mod : this.mods) {
            if (!mod.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    public ModInstance getMod(int slot) {
        return this.mods.get(slot);
    }

    public void replaceWith(ModContainer container) {
        for (int i = 0; i < this.getContainerSize(); i++) {
            this.setMod(i, container.getMod(i));
        }
    }

    public void confirmMods(Level level, IModHolder<?> holder) {
        this.confirmMods(level, holder, null);
    }

    public void confirmMods(Level level, IModHolder<?> holder, ItemStack stack) {
        this.replaceWith(this.modCache);
        this.collectModdedAttributes(holder, stack);
        this.clearCachedMods();
    }

    public void collectModdedAttributes(IModHolder<?> holder, ItemStack stack) {
        if (stack != null) {
            AttributeMap attributes = holder.getStats(stack);
            ModHelper.forEachModifier(stack, (attributeHolder, attributeModifier) -> {
                AttributeInstance instance = attributes.getInstance(attributeHolder);
                if (instance != null) {
                    instance.removeModifier(attributeModifier);
                }

                //Stop Location Based Effects
            });
            ModHelper.forEachModifier(stack, (attributeHolder, attributeModifier) -> {
                AttributeInstance instance = holder.getStats(stack).getInstance(attributeHolder);
                if (instance != null) {
                    instance.removeModifier(attributeModifier.id());
                    instance.addPermanentModifier(attributeModifier);
                }

                //Run Location Changed Effects
            });

            //Run Elemental Attribute Visitor
            //Add Elemental Attributes to list
            //Compare Attribute to previous Attribute
        } else {
            AttributeMap attributes = holder.getStats();
            ModHelper.forEachModifier(holder, (attributeHolder, attributeModifier) -> {
                AttributeInstance instance = attributes.getInstance(attributeHolder);
                if (instance != null) {
                    instance.removeModifier(attributeModifier);
                }

                //Stop Location Based Effects
            });
            ModHelper.forEachModifier(holder, (attributeHolder, attributeModifier) -> {
                AttributeInstance instance = holder.getStats().getInstance(attributeHolder);
                if (instance != null) {
                    instance.removeModifier(attributeModifier.id());
                    instance.addPermanentModifier(attributeModifier);
                }

                //Run Location Changed Effects
            });
        }
    }

    public void clearMods() {
        this.mods.clear();
    }

    public void clearCachedMods() {
        this.modCache.clearMods();
    }

    public int getMaxCapacity() {
        return this.maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = Mth.clamp(capacity, 0, this.maxCapacity);
    }

    @Override
    public String toString() {
        return this.mods.toString();
    }
}
