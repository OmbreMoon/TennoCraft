package com.ombremoon.tennocraft.event;

import com.ombremoon.tennocraft.TennoCraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TennoCraft.MOD_ID)
public class MovementEvents {
    static int jumpCount = 0;

    @SubscribeEvent
    public static void onPlayerJumpEvent(final LivingEvent.LivingJumpEvent event) {
        /*if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            jumpCount++;
            if (jumpCount % 1 == 0 && serverPlayer.in) {
                serverPlayer.setOnGround(true);
            }
        }*/
    }
}
