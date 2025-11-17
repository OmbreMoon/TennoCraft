package com.ombremoon.tennocraft.common.world;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;

public enum WorldDamage {
    IMPACT,
    PUNCTURE,
    SLASH,
    HEAT,
    COLD,
    ELECTRICITY,
    TOXIN,
    BLAST,
    CORROSIVE,
    GAS,
    MAGNETIC,
    RADIATION,
    VIRAL;

    private final ResourceKey<DamageType> damageType;
    private final Holder<MobEffect> proc;

    WorldDamage() {

    }

    public ResourceKey<DamageType> getDamageType() {
        return this.damageType;
    }

    public Holder<MobEffect> getProc() {
        return this.proc;
    }
}
