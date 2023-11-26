package com.ombremoon.tennocraft.object.item.weapon;

import com.ombremoon.tennocraft.object.item.mod.ModType;
import com.ombremoon.tennocraft.common.network.weapon.WeaponProperties;

public class SecondaryWeapon extends AbstractProjectileWeapon {

    public SecondaryWeapon(Properties pProperties, WeaponProperties weaponProperties) {
        super(pProperties, weaponProperties);
    }

    @Override
    public ModType getModType() {
        return ModType.SECONDARY;
    }
}
