package com.ombremoon.tennocraft.object.item.weapon;

import com.ombremoon.tennocraft.common.network.weapon.WeaponProperties;
import com.ombremoon.tennocraft.common.network.weapon.WeaponType;
import com.ombremoon.tennocraft.util.WeaponUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public abstract class AbstractProjectileWeapon extends AbstractWeaponItem {
    private final int fireRate;
    private final int projectileLife;
    private final float projectileSpeed;

    public AbstractProjectileWeapon(Properties pProperties, WeaponProperties weaponProperties) {
        super(pProperties, weaponProperties);
        this.fireRate = weaponProperties.fireRate;
        this.projectileLife = this.getWeaponType().getProjectileLife();
        this.projectileSpeed = this.getWeaponType().getProjectileSpeed();
    }

    public float getModdedFireRate(ItemStack itemStack) {
        return (this.getFireRate() * (1 + WeaponUtil.getFireRateModifier(itemStack)));
    }

    public int getFireRate() {
        return this.fireRate;
    }

    public int getProjectileLife() {
        return this.projectileLife;
    }

    public float getProjectileSpeed() {
        return this.projectileSpeed;
    }
}
