package com.ombremoon.tennocraft.common.modholder.api.mod;

import com.ombremoon.tennocraft.common.world.item.IModHolder;
import com.ombremoon.tennocraft.util.ModHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.item.ItemStack;

public class ModContainer {
    public static final int AURA_SLOT = 0;
    public static final int EXILUS_SLOT = 1;
    public static final int STANCE_SLOT = 0;
    public final NonNullList<ModInstance> mods;
    public final Modification.Compatibility type;
    public ModContainer modCache;
    private int maxCapacity;
    private int capacity;

    public ModContainer(Modification.Compatibility type, boolean cache) {
        this.type = type;
        this.mods = NonNullList.withSize(type.getMaxSlots(), ModInstance.EMPTY);

        if (!cache) {
            this.modCache = new ModContainer(type, true);
        }
    }

    public ModContainer(Modification.Compatibility type) {
        this(type, false);
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

    public ListTag save(ListTag listTag) {
        for (int i = 0; i < this.mods.size(); i++) {
            ModInstance mod = this.mods.get(i);
            if (!mod.isEmpty()) {
                CompoundTag tag = new CompoundTag();
                tag.putByte("Slot", (byte) i);
                tag.put("ModInstance", mod.save());
                listTag.add(tag);
            }
        }

        return listTag;
    }

    public void load(ListTag listTag) {
        this.mods.clear();

//        for (int i = 0; i < listTag.size(); i++) {
//            CompoundTag compoundTag = listTag.getCompound(i);
//            ModInstance mod = ModInstance.load(compoundTag);
//            this.mods.set(compoundTag.getByte("Slot"), mod);
//        }
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

    public void confirmMods(IModHolder<?> holder) {
        this.confirmMods(holder, null);
    }

    public void confirmMods(IModHolder<?> holder, ItemStack stack) {
        this.replaceWith(this.modCache);
        this.collectModdedAttributes(holder, stack);
        this.clearCachedMods();
    }

    private void collectModdedAttributes(IModHolder<?> holder, ItemStack stack) {
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
                    instance.addTransientModifier(attributeModifier);
                }

                //Run Location Changed Effects
            });
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
                    instance.addTransientModifier(attributeModifier);
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
