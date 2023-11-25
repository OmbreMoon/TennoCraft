package com.ombremoon.tennocraft.object.world;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public abstract class StatusEffect extends MobEffect {
    public StatusEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    protected abstract int getMaxStacks();

    protected abstract void applyDeathEffect(LivingEntity livingEntity);
}
