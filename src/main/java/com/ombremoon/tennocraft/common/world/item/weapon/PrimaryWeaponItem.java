package com.ombremoon.tennocraft.common.world.item.weapon;

import com.ombremoon.tennocraft.common.api.mod.Modification;

public class PrimaryWeaponItem extends RangedWeaponItem {

    public PrimaryWeaponItem(Properties properties) {
        super(properties);
    }

    @Override
    public Modification.Compatibility getModType() {
        return Modification.Compatibility.PRIMARY;
    }
}
