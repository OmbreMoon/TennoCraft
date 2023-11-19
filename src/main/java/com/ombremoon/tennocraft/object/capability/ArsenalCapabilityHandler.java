package com.ombremoon.tennocraft.object.capability;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

public class ArsenalCapabilityHandler implements IArsenalCapability, IItemHandlerModifiable, INBTSerializable<CompoundTag> {
    protected NonNullList<ItemStack> itemStacks;

    public ArsenalCapabilityHandler() {
        this(1);
    }

    public ArsenalCapabilityHandler(int size) {
        itemStacks = NonNullList.withSize(size, ItemStack.EMPTY);
    }

    public ArsenalCapabilityHandler(NonNullList<ItemStack> itemStacks) {
        this.itemStacks = itemStacks;
    }

    public void setSize(int size) {
        itemStacks = NonNullList.withSize(size, ItemStack.EMPTY);
    }

    @Override
    public int getSlots() {
        return itemStacks.size();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        validateSlotIndex(slot);
        return this.itemStacks.get(slot);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (stack.isEmpty())
            return ItemStack.EMPTY;

        if (!isItemValid(slot, stack))
            return stack;

        validateSlotIndex(slot);

        ItemStack existing = this.itemStacks.get(slot);

        int limit = getStackLimit(slot, stack);

        if (!existing.isEmpty()) {
            if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
                return stack;

            limit -= existing.getCount();
        }

        if (limit <= 0)
            return stack;

        boolean reachedLimit = stack.getCount() > limit;

        if (!simulate) {
            if (existing.isEmpty()) {
                this.itemStacks.set(slot, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
            } else {
                existing.grow(reachedLimit ? limit : stack.getCount());
            }
            onContentsChanged(slot);
        }

        return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount()- limit) : ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount == 0)
            return ItemStack.EMPTY;

        validateSlotIndex(slot);

        ItemStack existing = this.itemStacks.get(slot);

        if (existing.isEmpty())
            return ItemStack.EMPTY;

        int toExtract = Math.min(amount, existing.getMaxStackSize());

        if (existing.getCount() <= toExtract) {
            if (!simulate) {
                this.itemStacks.set(slot, ItemStack.EMPTY);
                onContentsChanged(slot);
                return existing;
            }
            else {
                return existing.copy();
            }
        } else {
            if (!simulate) {
                this.itemStacks.set(slot, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
                onContentsChanged(slot);
            }

            return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        return 2;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack itemStack) {
        return true;
    }

    protected int getStackLimit(int slot, @NotNull ItemStack itemStack) {
        return Math.min(getSlotLimit(slot), itemStack.getMaxStackSize());
    }

    @Override
    public CompoundTag serializeNBT() {
        ListTag nbtTagList = new ListTag();
        for (int i = 0; i < itemStacks.size(); i++) {
            if (!itemStacks.get(i).isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putInt("Slot", i);
                itemStacks.get(i).save(itemTag);
                nbtTagList.add(itemTag);
            }
        }
        CompoundTag nbt = new CompoundTag();
        nbt.put("Items", nbtTagList);
        nbt.putInt("Size", itemStacks.size());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        setSize(nbt.contains("Size", Tag.TAG_INT) ? nbt.getInt("Size") : itemStacks.size());
        ListTag tagList = nbt.getList("Items", Tag.TAG_COMPOUND);
        for (int i = 0; i < tagList.size(); i++) {
            CompoundTag itemTags = tagList.getCompound(i);
            int slot = itemTags.getInt("Slot");

            if (slot >= 0 && slot < itemStacks.size()) {
                itemStacks.set(slot, ItemStack.of(itemTags));
            }
        }
        onLoad();
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        validateSlotIndex(slot);
        this.itemStacks.set(slot, stack);
        onContentsChanged(slot);
    }

    protected void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= itemStacks.size())
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + itemStacks.size() + ")");
    }

    protected void onLoad() {

    }

    protected void onContentsChanged(int slot) {

    }
}
