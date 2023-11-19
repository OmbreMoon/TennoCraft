package com.ombremoon.tennocraft.object.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

@AutoRegisterCapability
public interface IArsenalCapability extends INBTSerializable<CompoundTag>, IItemHandler {

    int getSlots();

    @NotNull
    ItemStack getStackInSlot(int slot);

    @NotNull
    ItemStack insertItem(int slot, @NotNull ItemStack itemStack, boolean simulate);

    @NotNull
    ItemStack extractItem(int slot, int amount, boolean simulate);

    int getSlotLimit(int slot);

    boolean isItemValid(int slot, @NotNull ItemStack itemStack);
}
