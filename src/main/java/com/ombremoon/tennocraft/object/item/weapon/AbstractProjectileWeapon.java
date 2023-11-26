package com.ombremoon.tennocraft.object.item.weapon;

import com.ombremoon.tennocraft.common.network.weapon.WeaponProperties;
import com.ombremoon.tennocraft.common.network.weapon.WeaponType;

public abstract class AbstractProjectileWeapon extends AbstractWeaponItem {
    private final WeaponType weaponType;

    public AbstractProjectileWeapon(Properties pProperties, WeaponProperties weaponProperties) {
        super(pProperties, weaponProperties);
        this.weaponType = weaponProperties.weaponType;
    }

    public WeaponType getWeaponType() {
        return this.weaponType;
    }
}
