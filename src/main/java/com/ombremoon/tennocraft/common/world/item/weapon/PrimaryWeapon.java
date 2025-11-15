package com.ombremoon.tennocraft.common.world.item.weapon;

import com.ombremoon.tennocraft.common.modholder.api.mod.Modification;

public class PrimaryWeapon extends RangedWeapon {

    public PrimaryWeapon(Properties properties) {
        super(properties);
    }

    @Override
    public Modification.Compatibility getModType() {
        return Modification.Compatibility.PRIMARY;
    }
}
