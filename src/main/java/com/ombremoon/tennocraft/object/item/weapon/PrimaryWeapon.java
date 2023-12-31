package com.ombremoon.tennocraft.object.item.weapon;

import com.ombremoon.tennocraft.object.item.mod.ModType;
import com.ombremoon.tennocraft.common.network.weapon.WeaponProperties;
import net.minecraft.world.entity.EntityType;

import java.util.function.Supplier;

public class PrimaryWeapon extends AbstractProjectileWeapon {
    public PrimaryWeapon(Properties pProperties, WeaponProperties weaponProperties) {
        super(pProperties, weaponProperties);
    }

    @Override
    public ModType getModType() {
        return ModType.PRIMARY;
    }
}
