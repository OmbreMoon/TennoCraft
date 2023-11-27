package com.ombremoon.tennocraft.common.network.weapon;

import com.ombremoon.tennocraft.common.init.entity.TCProjectiles;
import com.ombremoon.tennocraft.common.network.packet.server.ServerboundShootPacket;
import com.ombremoon.tennocraft.object.entity.projectile.AbstractBullet;
import com.ombremoon.tennocraft.object.item.weapon.AbstractProjectileWeapon;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.Level;

public class WeaponHandler {

    public static void createBullet(ServerboundShootPacket packet, ServerPlayer serverPlayer) {
        if (serverPlayer.getUseItem().getItem() instanceof ShieldItem) {
            return;
        }

        Level level = serverPlayer.level();
        ItemStack weaponStack = serverPlayer.getMainHandItem();
        if (weaponStack.getItem() instanceof AbstractProjectileWeapon projectileWeapon) {
            serverPlayer.setYRot(packet.getPitchAmount());
            serverPlayer.setXRot(packet.getYawAmount());

            AbstractBullet abstractBullet = TCProjectiles.ProjectileSpawner.getInstance().getProjectile(projectileWeapon.getWeaponLocation()).create(level, serverPlayer, weaponStack, projectileWeapon);
            abstractBullet.setWeaponStack(weaponStack);
            abstractBullet.setProjectileWeapon(projectileWeapon);
            level.addFreshEntity(abstractBullet);
            abstractBullet.tick();
        }
    }
}
