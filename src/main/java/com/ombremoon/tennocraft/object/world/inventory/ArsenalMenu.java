package com.ombremoon.tennocraft.object.world.inventory;

import com.ombremoon.tennocraft.common.init.TCMenuTypes;
import com.ombremoon.tennocraft.common.init.block.TCBlocks;
import com.ombremoon.tennocraft.object.block.entity.ArsenalBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;

public class ArsenalMenu extends AbstractContainerMenu {
    public final ArsenalBlockEntity arsenalBlockEntity;
    private final Level level;

    public ArsenalMenu(int id, Inventory inventory, FriendlyByteBuf friendlyByteBuf) {
        this(id, inventory, inventory.player.level().getBlockEntity(friendlyByteBuf.readBlockPos()));
    }

    public ArsenalMenu(int id, Inventory inventory, BlockEntity blockEntity) {
        super(TCMenuTypes.ARSENAL_MENU.get(), id);
        checkContainerSize(inventory, 30);
        this.arsenalBlockEntity = (ArsenalBlockEntity) blockEntity;
        this.level = inventory.player.level();

        this.arsenalBlockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            this.addSlot(new SlotItemHandler(handler, 0, 20, 36));
        });

        addPlayerSlots(inventory);
    }

    private void addPlayerSlots(Inventory inventory) {
        //Hotbar
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(inventory, col, 8 + col * 18, 142));
        }

        //Player Inventory
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(inventory, col + row * 9 + 9, 8 + col * 18,  84 + row * 18));
            }
        }
    }

    public ArsenalBlockEntity getArsenalBlockEntity() {
        return this.arsenalBlockEntity;
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return null;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, arsenalBlockEntity.getBlockPos()), pPlayer, TCBlocks.ARSENAL_BLOCK.get());
    }
}
