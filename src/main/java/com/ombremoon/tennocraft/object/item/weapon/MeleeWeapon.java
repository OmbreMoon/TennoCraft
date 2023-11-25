package com.ombremoon.tennocraft.object.item.weapon;

import com.ombremoon.tennocraft.object.entity.generic.TCEnemy;
import com.ombremoon.tennocraft.object.item.mod.ModType;
import com.ombremoon.tennocraft.object.world.DamageType;
import com.ombremoon.tennocraft.player.weapon.WeaponProperties;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.Random;

public class MeleeWeapon extends AbstractWeaponItem {
    public MeleeWeapon(Properties pProperties, WeaponProperties weaponProperties) {
        super(pProperties, weaponProperties);
    }

    @Override
    public ModType getModType() {
        return ModType.MELEE;
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        Random random = new Random();
        float baseDamage = this.getTotalBaseDamage();
        if (pTarget instanceof TCEnemy<?> enemy) {
            float armorResistedDamage = baseDamage * random.nextFloat(0.25F, 0.55F);
            baseDamage = armorResistedDamage;
        }
        System.out.println(baseDamage);
        pTarget.hurt(pTarget.damageSources().generic(), baseDamage);
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }
}
