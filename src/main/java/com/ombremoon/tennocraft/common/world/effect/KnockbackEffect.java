package com.ombremoon.tennocraft.common.world.effect;

import com.ombremoon.tennocraft.common.api.IModHolder;
import com.ombremoon.tennocraft.common.init.TCDamageTypes;
import com.ombremoon.tennocraft.util.StatusHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class KnockbackEffect extends StatusEffect {

    public KnockbackEffect(MobEffectCategory category, int maxStacks, int entityDuration, int color) {
        super(category, maxStacks, entityDuration, color);
    }

    @Override
    public void onEffectStarted(LivingEntity livingEntity, IModHolder<?> modHolder, int amplifier) {
        super.onEffectStarted(livingEntity, modHolder, amplifier);
        //Play stagger anim with duration based on procs
        int procs = StatusHelper.getProcAmount(livingEntity, this);
        float mercyThreshold = 0.8F * procs;
        //Set entity mercy threshold
    }

    @Override
    public void onEffectRemoved(LivingEntity livingEntity, IModHolder<?> modHolder) {
        //Reset entity mercy threshold
    }

    @Override
    public ResourceKey<DamageType> damageProc() {
        return TCDamageTypes.IMPACT;
    }
}
