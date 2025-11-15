package com.ombremoon.tennocraft.common.world.item;

import com.ombremoon.tennocraft.common.init.schemas.TCFrames;
import com.ombremoon.tennocraft.common.modholder.FrameHandler;
import com.ombremoon.tennocraft.main.Keys;
import com.ombremoon.tennocraft.util.FrameUtil;
import com.ombremoon.tennocraft.util.Loggable;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class DebugItem extends Item implements Loggable {
    public DebugItem(Properties pProperties) {
        super(pProperties.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide) {
            ItemStack stack = pPlayer.getItemInHand(pUsedHand);
            ResourceLocation location = ResourceLocation.parse("tennocraft:pistol_gambit");
            var key = ResourceKey.create(Keys.MOD, location);
            log(pLevel.registryAccess().holderOrThrow(key));
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
