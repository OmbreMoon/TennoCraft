package com.ombremoon.tennocraft.object.item.weapon;

import com.google.common.collect.Maps;
import com.ombremoon.tennocraft.object.item.IModHolder;
import com.ombremoon.tennocraft.object.world.DamageType;
import com.ombremoon.tennocraft.player.weapon.WeaponProperties;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public abstract class AbstractWeaponItem extends Item implements IModHolder {
    private final float impactDamage;
    private final float punctureDamage;
    private final float slashDamage;
    private final float statusChance;
    private float totalBaseDamage;
    private Map<DamageType, Float> damageMap;
    private final Map<DamageType, Float> statusMap = Maps.newEnumMap(DamageType.class);

    private boolean isProcApplied;

    public AbstractWeaponItem(Properties pProperties, WeaponProperties weaponProperties) {
        super(pProperties.stacksTo(1));
        this.impactDamage = weaponProperties.impactDamage;
        this.punctureDamage = weaponProperties.punctureDamage;
        this.slashDamage = weaponProperties.slashDamage;
        this.statusChance = weaponProperties.statusChance;
        this.totalBaseDamage = impactDamage + punctureDamage + slashDamage;
        this.damageMap = weaponProperties.damageMap;
    }

    public float getImpactDamage() {
        return this.impactDamage;
    }

    public float getPunctureDamage() {
        return this.punctureDamage;
    }

    public float getSlashDamage() {
        return this.slashDamage;
    }

    public float getTotalBaseDamage() {
        return this.totalBaseDamage;
    }

    public float getStatusChance() {
        return this.statusChance;
    }

    public Map<DamageType, Float> getDamageMap() {
        return this.damageMap;
    }

    public Map<DamageType, Float> getStatusMap() {
        return this.statusMap;
    }

    private float chanceForEffectToApply(float damageAmount) {
        return damageAmount / this.getTotalBaseDamage() * this.getStatusChance();
    }

    public void gatherStatusChances() {
        for (Map.Entry<DamageType, Float> entry : this.getDamageMap().entrySet()) {
            statusMap.put(entry.getKey(), chanceForEffectToApply(entry.getValue()));
            if (2 > 1) {

            }
        }
    }

    public void setProcApplied(boolean procApplied) {
        this.isProcApplied = procApplied;
    }

    public boolean isProcApplied() {
        return false;
    }

    //GET MODDED DAMAGE METHOD

    //GET MOD LIST

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        gatherStatusChances();
        return true;
    }
}
