package com.ombremoon.tennocraft.common.event;

import com.ombremoon.tennocraft.common.world.effect.StatusEffectInstance;
import com.ombremoon.tennocraft.main.Constants;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class TCEvents {

    @SubscribeEvent
    public static void onEffectRemoved(MobEffectEvent.Remove event) {
        LivingEntity livingEntity = event.getEntity();
        if (event.getEffectInstance() instanceof StatusEffectInstance effectInstance && !livingEntity.level().isClientSide) {
            effectInstance.removeStatusProc(livingEntity);
        }
    }

    @SubscribeEvent
    public static void onEffectExpired(MobEffectEvent.Expired event) {
        LivingEntity livingEntity = event.getEntity();
        if (event.getEffectInstance() instanceof StatusEffectInstance effectInstance && !livingEntity.level().isClientSide) {
            effectInstance.removeStatusProc(livingEntity);
        }
    }
}
