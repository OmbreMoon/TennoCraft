package com.ombremoon.tennocraft.common.world.item;

import com.ombremoon.tennocraft.common.api.mod.ModContainer;
import com.ombremoon.tennocraft.common.api.mod.ModInstance;
import com.ombremoon.tennocraft.common.api.mod.Modification;
import com.ombremoon.tennocraft.common.init.TCAttributes;
import com.ombremoon.tennocraft.common.init.mods.TCFrameMods;
import com.ombremoon.tennocraft.common.init.mods.TCMeleeWeaponMods;
import com.ombremoon.tennocraft.common.init.schemas.TCFrames;
import com.ombremoon.tennocraft.common.api.handler.FrameHandler;
import com.ombremoon.tennocraft.common.api.MineFrame;
import com.ombremoon.tennocraft.util.FrameUtil;
import com.ombremoon.tennocraft.util.Loggable;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DebugItem extends Item implements Loggable {
    public DebugItem(Properties pProperties) {
        super(pProperties.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide) {
            log((int)(2.25));
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
