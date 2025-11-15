package com.ombremoon.tennocraft.common.modholder.api.weapon.schema;

import com.ombremoon.tennocraft.common.modholder.api.weapon.DamageValue;
import com.ombremoon.tennocraft.common.modholder.api.weapon.NoiseLevel;

import java.util.List;

public class MeleeAttackSchema extends AttackSchema {
    MeleeAttackSchema(List<DamageValue> damageValues, float critChance, float critMultiplier, float statusChance, NoiseLevel noiseLevel) {
        super(damageValues, critChance, critMultiplier, statusChance, noiseLevel);
    }
}
