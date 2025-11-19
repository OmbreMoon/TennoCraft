package com.ombremoon.tennocraft.common.world.item;

import com.ombremoon.tennocraft.common.api.handler.FrameHandler;
import com.ombremoon.tennocraft.common.init.schemas.TCFrames;
import com.ombremoon.tennocraft.util.FrameUtil;
import com.ombremoon.tennocraft.util.Loggable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DebugItem extends Item implements Loggable {
    public DebugItem(Properties pProperties) {
        super(pProperties.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide) {
            Map<Integer, List<Float>> map = new HashMap<>();
            var list = map.computeIfAbsent(0, integer -> new ArrayList<>());
            list.add(2.0F);
            log(map);
        } else {
        }
        FrameHandler handler = FrameUtil.getFrameHandler(pPlayer);
        handler.selectFrame(TCFrames.VOLT);
        handler.toggleTransference();
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);

    }
}
