package com.ombremoon.tennocraft.common.world.item.weapon;

import com.ombremoon.tennocraft.common.modholder.api.mod.Modification;

public class SecondaryWeapon extends RangedWeapon {

    public SecondaryWeapon(Properties properties) {
        super(properties);
    }

    @Override
    public Modification.Compatibility getModType() {
        return Modification.Compatibility.SECONDARY;
    }
}
