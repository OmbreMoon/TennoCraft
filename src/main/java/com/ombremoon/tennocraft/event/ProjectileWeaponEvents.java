package com.ombremoon.tennocraft.event;

import com.ombremoon.tennocraft.client.KeyBinds;
import com.ombremoon.tennocraft.common.network.TCMessages;
import com.ombremoon.tennocraft.common.network.packet.server.ServerboundShootPacket;
import com.ombremoon.tennocraft.object.item.weapon.AbstractProjectileWeapon;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

//Credit to MrCrayfish
public class ProjectileWeaponEvents {

    private static ProjectileWeaponEvents instance;

    public static ProjectileWeaponEvents getInstance() {
        if (instance == null) {
            instance = new ProjectileWeaponEvents();
        }
        return instance;
    }

    private boolean isActivePlayer() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.getOverlay() != null)
            return false;
        if (minecraft.screen != null)
            return false;
        if (!minecraft.mouseHandler.isMouseGrabbed())
            return false;
        return minecraft.isWindowActive();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onPlayerShoot(InputEvent.InteractionKeyMappingTriggered event) {

        if (event.isCanceled())
            return;

        Player player = Minecraft.getInstance().player;
        if (player != null) {
            ItemStack weaponStack = player.getMainHandItem();
            if (weaponStack.getItem() instanceof AbstractProjectileWeapon projectileWeapon) {
                event.setSwingHand(false);
                event.setCanceled(true);
                this.shootBullet(player, weaponStack);
                if (!projectileWeapon.getWeaponType().isAutoFire()) {
                    KeyBinds.getShootKeyBind().setDown(false);
                }
            }
        }
    }

    @SubscribeEvent
    public void onAutomaticFire(TickEvent.ClientTickEvent event) {

        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        if (!isActivePlayer()) {
            return;
        }

        Player player = Minecraft.getInstance().player;

    }

    public void shootBullet(Player player, ItemStack weaponStack) {
        if (!(weaponStack.getItem() instanceof AbstractProjectileWeapon projectileWeapon)) {
            return;
        }

        if (player.getUseItem().getItem() instanceof ShieldItem) {
            return;
        }

        ItemCooldowns weaponCooldown = player.getCooldowns();
        if (!weaponCooldown.isOnCooldown(projectileWeapon)) {
            int fireRate = Math.round(projectileWeapon.getModdedFireRate(weaponStack));
            weaponCooldown.addCooldown(projectileWeapon, fireRate);
            TCMessages.sendToServer(new ServerboundShootPacket(player));
        }

    }
}
