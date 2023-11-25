package com.ombremoon.tennocraft.player.weapon;

import com.google.common.collect.Maps;
import com.ombremoon.tennocraft.object.world.DamageType;

import java.util.Map;

public class WeaponProperties {
    public WeaponType weaponType;
    public float impactDamage;
    public float punctureDamage;
    public float slashDamage;
    public float blastDamage;
    public float statusChance;
    public Map<DamageType, Float> damageMap = Maps.newEnumMap(DamageType.class);

    public WeaponProperties weaponType(WeaponType weaponType) {
        this.weaponType = weaponType;
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
