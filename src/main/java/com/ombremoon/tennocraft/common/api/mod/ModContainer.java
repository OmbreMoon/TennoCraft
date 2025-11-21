package com.ombremoon.tennocraft.common.api.mod;

import com.ombremoon.tennocraft.common.api.IModHolder;
import com.ombremoon.tennocraft.common.api.weapon.schema.Schema;
import com.ombremoon.tennocraft.util.ModHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

public abstract class ModContainer implements INBTSerializable<CompoundTag> {
    public static final int AURA_SLOT = 0;
    public static final int EXILUS_SLOT = 1;
    public static final int STANCE_SLOT = 0;
    public final NonNullList<ModInstance> mods;
    public final Modification.Compatibility type;
    protected final Schema schema;
    public ModContainer modCache;
    private int maxCapacity;
    private int capacity;

    ModContainer(Modification.Compatibility type, Schema schema) {
        this(type, schema, false);
    }

    ModContainer(Modification.Compatibility type, Schema schema, boolean cache) {
        this.type = type;
        this.mods = NonNullList.withSize(type.getMaxSlots(), ModInstance.EMPTY);
        this.schema = schema;

        if (!cache) {
            this.modCache = this.createCache();
        }
    }

    public Modification.Compatibility getCompatibility() {
        return this.type;
    }

    protected abstract ModContainer createCache();

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

    public void confirmMods(Player player, IModHolder<?> holder) {
        this.confirmMods(player, holder, null);
    }

    public void confirmMods(Player player, IModHolder<?> holder, ItemStack stack) {
        this.replaceWith(this.modCache);
        this.clearCachedMods();
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
