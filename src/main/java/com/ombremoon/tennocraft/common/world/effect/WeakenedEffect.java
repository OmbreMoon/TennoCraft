package com.ombremoon.tennocraft.common.world.effect;

import com.ombremoon.tennocraft.common.init.TCDamageTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectCategory;

public class WeakenedEffect extends StatusEffect {

    public WeakenedEffect(MobEffectCategory category, int maxStacks, int entityDuration, int color) {
        super(category, maxStacks, entityDuration, color);
    }

    public WeakenedEffect(MobEffectCategory category, int maxStacks, int entityDuration, int playerDuration, int color) {
        super(category, maxStacks, entityDuration, playerDuration, color);
    }

    @Override
    public ResourceKey<DamageType> damageProc() {
        return TCDamageTypes.PUNCTURE;
    }
}
