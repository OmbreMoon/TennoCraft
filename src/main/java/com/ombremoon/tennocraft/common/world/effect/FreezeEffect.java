package com.ombremoon.tennocraft.common.world.effect;

import com.ombremoon.tennocraft.common.init.TCDamageTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectCategory;

public class FreezeEffect extends StatusEffect {

    public FreezeEffect(MobEffectCategory category, int maxStacks, int duration, int color) {
        super(category, maxStacks, duration, color);
    }

    @Override
    public ResourceKey<DamageType> damageProc() {
        return TCDamageTypes.COLD;
    }
}
