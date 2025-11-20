package com.ombremoon.tennocraft.util;

import com.ombremoon.tennocraft.common.world.WorldStatus;
import com.ombremoon.tennocraft.common.world.effect.StatusEffect;
import com.ombremoon.tennocraft.common.world.effect.StatusEffectInstance;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class StatusHelper {

    public static boolean procEffect(@Nullable LivingEntity attacker, LivingEntity entity, Holder<DamageType> damageType, float damage) {
        WorldStatus worldStatus = StatusEffect.getStatusFromType(damageType.getKey());
        Holder<MobEffect> mobEffect = worldStatus.statusEffect();
        if (entity.getType().is(worldStatus.immuneToTag())) {
            return false;
        }

        StatusEffect.Proc proc = new StatusEffect.Proc(damageType.getKey(), damage, entity.level().getGameTime());
        StatusEffect effect = (StatusEffect) mobEffect.value();
        MobEffectInstance oldInstance = entity.getEffect(mobEffect);
        int duration = entity instanceof Player ? effect.getPlayerDuration() : effect.getEntityDuration();
        if (oldInstance instanceof StatusEffectInstance statusInstance) {
            duration += statusInstance.getDuration();
        }

        StatusEffectInstance instance = new StatusEffectInstance(mobEffect, proc, duration);
        return entity.addEffect(instance, attacker);
    }

    public static boolean procEffect(LivingEntity entity, Holder<DamageType> damageType, float damage) {
        return procEffect(null, entity, damageType, damage);
    }
}
