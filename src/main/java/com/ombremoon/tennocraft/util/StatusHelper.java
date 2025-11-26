package com.ombremoon.tennocraft.util;

import com.ombremoon.tennocraft.common.api.IModHolder;
import com.ombremoon.tennocraft.common.init.TCData;
import com.ombremoon.tennocraft.common.world.WorldStatus;
import com.ombremoon.tennocraft.common.world.effect.StatusEffect;
import com.ombremoon.tennocraft.common.world.effect.StatusEffectInstance;
import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class StatusHelper {

    public static boolean procEffect(LivingEntity attacker, LivingEntity entity, IModHolder<?> modHolder, ItemStack stack, Holder<DamageType> damageType, float damage) {
        WorldStatus worldStatus = StatusEffect.getStatusFromType(damageType.getKey());
        Holder<MobEffect> mobEffect = worldStatus.statusEffect();
        if (entity.getType().is(worldStatus.immuneToTag()))
            return false;

        StatusEffect effect = (StatusEffect) mobEffect.value();
        int duration = entity instanceof Player ? effect.getPlayerDuration() : effect.getEntityDuration();
        StatusEffect.Proc proc = new StatusEffect.Proc(modHolder, attacker, stack, damage, entity.level().getGameTime() + duration);
        StatusEffectInstance instance = new StatusEffectInstance(mobEffect, proc, modHolder, duration);
        if (entity.hasEffect(mobEffect)) {
            var statuses = entity.getData(TCData.STATUS_PROCS);
            statuses.addEntry(effect, proc);
            return entity.addEffect(instance, attacker);
        }

        return entity.addEffect(instance, attacker);
    }

    public static boolean procEffect(LivingEntity attacker, LivingEntity entity, IModHolder<?> modHolder, Holder<DamageType> damageType, float damage) {
        return procEffect(attacker, entity, modHolder, null, damageType, damage);
    }

    public static int getProcAmount(LivingEntity entity, Holder<MobEffect> holder) {
        MobEffect effect = holder.value();
        return effect instanceof StatusEffect statusEffect ? getProcAmount(entity, statusEffect) : 0;
    }

    public static int getProcAmount(LivingEntity entity, StatusEffect statusEffect) {
        var statuses = entity.getData(TCData.STATUS_PROCS);
        return statuses.getProcAmount(statusEffect);
    }
}
