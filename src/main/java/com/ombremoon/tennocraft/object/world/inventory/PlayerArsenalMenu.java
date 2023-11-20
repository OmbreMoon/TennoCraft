package com.ombremoon.tennocraft.object.world.inventory;

import com.ombremoon.tennocraft.common.init.TCMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import top.theillusivec4.curios.api.CuriosCapability;

public class PlayerArsenalMenu extends AbstractContainerMenu {
    private final Player player;

    public static PlayerArsenalMenu fromNetwork(int screenId, Inventory inventory, FriendlyByteBuf friendlyByteBuf) {
        return new PlayerArsenalMenu(screenId, inventory);
    }

    public PlayerArsenalMenu(int id, Inventory inventory) {
        super(TCMenuTypes.PLAYER_ARSENAL_MENU.get(), id);
        checkContainerSize(inventory, 2);
        this.player = inventory.player;

        player.getCapability(CuriosCapability.INVENTORY).ifPresent(capability -> {
            this.addSlot(new SlotItemHandler(capability.getEquippedCurios(), 0, 20, 36));
        });
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);

        if (slot != null && slot.hasItem()) {
            int slotSize = this.slots.size();
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (pIndex < pPlayer.getInventory().items.size()) {
                if (!this.moveItemStackTo(itemstack1, pPlayer.getInventory().items.size(), slotSize, false))
                    return ItemStack.EMPTY;
            } else if (!this.moveItemStackTo(itemstack1, 0, pPlayer.getInventory().items.size(), false)) {
                return ItemStack.EMPTY;
            }
            if (itemstack1.isEmpty()) slot.set(ItemStack.EMPTY); else slot.setChanged();
        }
        return itemstack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }
}
