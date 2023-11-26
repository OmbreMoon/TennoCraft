package com.ombremoon.tennocraft.object.item.weapon;

import com.google.common.collect.Maps;
import com.mojang.datafixers.types.templates.Check;
import com.ombremoon.tennocraft.common.AttributeHandler;
import com.ombremoon.tennocraft.object.entity.generic.TCEnemy;
import com.ombremoon.tennocraft.object.item.mod.ModType;
import com.ombremoon.tennocraft.common.network.weapon.WeaponProperties;
import com.ombremoon.tennocraft.object.world.DamageType;
import com.ombremoon.tennocraft.player.FrameAttribute;
import com.ombremoon.tennocraft.util.DamageUtil;
import com.ombremoon.tennocraft.util.WeaponUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.tslat.smartbrainlib.util.RandomUtil;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

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
        ItemStack itemStack = pAttacker.getMainHandItem();
        Item item = pAttacker.getMainHandItem().getItem();
        if (item instanceof MeleeWeapon meleeWeapon) {
            float moddedDamage = meleeWeapon.getModdedDamage(itemStack);
            System.out.println(moddedDamage);

            float critChance = getModdedCritChance(itemStack);
            if (meleeWeapon.isCriticalHit(critChance)) {
                float critMultiplier = calculateCritDamage(itemStack);
                float guaranteedCrits = critChance > 1 ? Mth.floor(critChance) : 1;
                if (critChance > 1) {
                    float potentialCrit = critChance - Mth.floor(critChance);
                    if (isCriticalHit(potentialCrit)) {
                        guaranteedCrits += 1;
                    }
                }
                moddedDamage *= critMultiplier * guaranteedCrits;
            }
            System.out.println(moddedDamage);

            float elementalDamage = moddedDamage * (1 + WeaponUtil.getElementalDamageModifier(itemStack));
            System.out.println(elementalDamage);

            //Armor Resist
            if (pTarget instanceof TCEnemy<?> enemy) {
                elementalDamage = Mth.floor(elementalDamage * ThreadLocalRandom.current().nextFloat(0.25F, 0.55F));
            }
            System.out.println(elementalDamage);
            pTarget.hurt(pTarget.damageSources().generic(), elementalDamage);

            if (RandomUtil.percentChance(meleeWeapon.getModdedStatus(itemStack))) {
                Map<DamageType, Float> damageMap = Maps.newEnumMap(DamageType.class);
                float damageModifier = (1 + WeaponUtil.getDamageModifier(itemStack));
                for (Map.Entry<DamageType, Float> entry : meleeWeapon.getDamageMap().entrySet()) {
                    damageMap.put(entry.getKey(), entry.getValue() * damageModifier); //WORKS
                }
                for (Map.Entry<FrameAttribute, Float> entry : AttributeHandler.getFrameAttributes(itemStack).entrySet()) {
                    if (entry.getKey().getDamageType() != null) {
                        DamageType damageType = entry.getKey().getDamageType();
                        if (damageMap.containsKey(damageType)) {
                            damageMap.put(damageType, damageMap.get(damageType) * (1 + entry.getValue())); //TEST
                            break;
                        }
                        damageMap.put(damageType, moddedDamage * getModdedAttributeDamage(entry.getKey(), itemStack)); //WORKS
                    }
                }
                System.out.println(damageMap);
                float statusDamage = (float)damageMap.values().stream().mapToDouble(Float::doubleValue).sum();
                for (Map.Entry<DamageType, Float> entry : damageMap.entrySet()) {
                    if (RandomUtil.percentChance(entry.getValue() / statusDamage)) {
                        if (!pAttacker.level().isClientSide) {
                            if (entry.getKey().getFrameAttribute() != null) {
                                FrameAttribute frameAttribute = entry.getKey().getFrameAttribute().get();
                                DamageUtil.doPostDamageEffects(frameAttribute, pAttacker, pTarget);
                                DamageUtil.doPostHurtEffects(frameAttribute, pTarget, pAttacker);
                            }
                        }
                    }
                }
            }
        }
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }
}