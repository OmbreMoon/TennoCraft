package com.ombremoon.tennocraft.object.item.weapon;

import com.ombremoon.tennocraft.object.item.mod.ModType;
import com.ombremoon.tennocraft.object.world.DamageType;
import com.ombremoon.tennocraft.player.weapon.WeaponProperties;
import com.ombremoon.tennocraft.player.weapon.WeaponType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

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
