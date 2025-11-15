package com.ombremoon.tennocraft.common.modholder.api.weapon.schema;

import com.ombremoon.tennocraft.common.modholder.api.weapon.DamageValue;
import com.ombremoon.tennocraft.common.modholder.api.weapon.NoiseLevel;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.List;

public abstract class AttackSchema {
    protected final List<DamageValue> damage = new ObjectArrayList<>();
    protected final float critChance;
    protected final float critMultiplier;
    protected final float status;
    protected final NoiseLevel noise;

    AttackSchema(List<DamageValue> damage, float critChance, float critMultiplier, float status, NoiseLevel noise) {
        this.damage.addAll(damage);
        this.critChance = critChance;
        this.critMultiplier = critMultiplier;
        this.status = status;
        this.noise = noise;
    }

    public List<DamageValue> getDamage() {
        return this.damage;
    }

    public float getCritChance() {
        return this.critChance;
    }

    public float getCritMultiplier() {
        return this.critMultiplier;
    }

    public float getStatus() {
        return this.status;
    }

    public NoiseLevel getNoise() {
        return this.noise;
    }
}
