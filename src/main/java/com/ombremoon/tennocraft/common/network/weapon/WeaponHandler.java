package com.ombremoon.tennocraft.common.network.weapon;

import com.ombremoon.tennocraft.common.init.entity.TCProjectiles;
import com.ombremoon.tennocraft.object.entity.projectile.AbstractBullet;
import com.ombremoon.tennocraft.object.item.weapon.AbstractProjectileWeapon;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.Level;

public class WeaponHandler {

    public static void createBullet(ServerPlayer serverPlayer) {
        if (serverPlayer.getUseItem().getItem() instanceof ShieldItem) {
            return;
        }

        Level level = serverPlayer.level();
        ItemStack weaponStack = serverPlayer.getMainHandItem();
        if (weaponStack.getItem() instanceof AbstractProjectileWeapon projectileWeapon) {
            AbstractBullet abstractBullet = TCProjectiles.ProjectileSpawner.getInstance().getProjectile(projectileWeapon.getWeaponLocation()).create(level, serverPlayer, weaponStack, projectileWeapon);
            AbstractBullet abstractBullet1 = new AbstractBullet(TCProjectiles.ABSTRACT_BULLET.get(), level, serverPlayer, weaponStack, projectileWeapon);
            abstractBullet1.shootFromRotation(serverPlayer, serverPlayer.getXRot(), serverPlayer.getYRot(), 0, projectileWeapon.getProjectileSpeed(), projectileWeapon.getAccuracy());
            abstractBullet1.setWeaponStack(weaponStack);
            abstractBullet1.setProjectileWeapon(projectileWeapon);
            level.addFreshEntity(abstractBullet1);
        }
    }
}
