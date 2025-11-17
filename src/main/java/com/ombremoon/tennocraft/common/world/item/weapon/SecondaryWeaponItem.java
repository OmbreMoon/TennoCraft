package com.ombremoon.tennocraft.common.world.item.weapon;

import com.ombremoon.tennocraft.common.api.mod.Modification;

public class SecondaryWeaponItem extends RangedWeaponItem {

    public SecondaryWeaponItem(Properties properties) {
        super(properties);
    }

    @Override
    public Modification.Compatibility getModType() {
        return Modification.Compatibility.SECONDARY;
    }
}
