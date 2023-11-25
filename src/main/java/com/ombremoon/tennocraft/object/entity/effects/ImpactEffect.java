package com.ombremoon.tennocraft.object.entity.effects;

import com.ombremoon.tennocraft.object.world.StatusEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class ImpactEffect extends StatusEffect {
    public ImpactEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    protected int getMaxStacks() {
        return 5;
    }

    @Override
    protected void applyDeathEffect(LivingEntity livingEntity) {

    }
}
