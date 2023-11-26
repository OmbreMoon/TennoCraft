package com.ombremoon.tennocraft.object.item;

import com.ombremoon.tennocraft.common.AttributeHandler;
import com.ombremoon.tennocraft.util.FrameUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DebugItem extends Item {
    public DebugItem(Properties pProperties) {
        super(pProperties.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemStack = FrameUtil.getFrameStack(pPlayer);
        if (itemStack.getItem() instanceof TransferenceKeyItem) {
            System.out.println(AttributeHandler.getFrameAttributeTags(pPlayer.getItemInHand(InteractionHand.OFF_HAND)));
            System.out.println(AttributeHandler.getFrameAttributeTags(itemStack));
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
