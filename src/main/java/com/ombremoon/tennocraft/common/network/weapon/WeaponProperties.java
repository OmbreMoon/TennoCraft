package com.ombremoon.tennocraft.common.network.weapon;

import com.google.common.collect.Maps;
import com.ombremoon.tennocraft.object.world.DamageType;

import java.util.Map;

public class WeaponProperties {
    public WeaponType weaponType;
    public int fireRate;
    public float critChance;
    public float critMultiplier;
    public float impactDamage;
    public float punctureDamage;
    public float slashDamage;
    public float heatDamage;
    public float blastDamage;
    public float statusChance;
    public Map<DamageType, Float> damageMap = Maps.newEnumMap(DamageType.class);

    public WeaponProperties weaponType(WeaponType weaponType) {
        this.weaponType = weaponType;
        return this;
    }

    public WeaponProperties fireRate(int fireRate) {
        this.fireRate = fireRate;
        return this;
    }

    public WeaponProperties critChance(float critChance) {
        this.critChance = critChance;
        return this;
    }

    public WeaponProperties critMultiplier(float critMultiplier) {
        this.critMultiplier = critMultiplier;
        return this;
    }

    public WeaponProperties impactDamage(float impactDamage) {
        this.impactDamage = impactDamage;
        damageMap.put(DamageType.IMPACT, impactDamage);
        return this;
    }

    public WeaponProperties punctureDamage(float punctureDamage) {
        this.punctureDamage = punctureDamage;
        damageMap.put(DamageType.PUNCTURE, punctureDamage);
        return this;
    }

    public WeaponProperties slashDamage(float slashDamage) {
        this.slashDamage = slashDamage;
        damageMap.put(DamageType.SLASH, slashDamage);
        return this;
    }

    public WeaponProperties heatDamage(float heatDamage) {
        this.heatDamage = heatDamage;
        damageMap.put(DamageType.HEAT, heatDamage);
        return this;
    }

    public WeaponProperties blastDamage(float blastDamage) {
        this.blastDamage = blastDamage;
        damageMap.put(DamageType.BLAST, blastDamage);
        return this;
    }

    public WeaponProperties statusChance(float statusChance) {
        this.statusChance = statusChance;
        return this;
    }
}
