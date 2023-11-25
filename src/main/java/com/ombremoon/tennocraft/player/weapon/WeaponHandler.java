package com.ombremoon.tennocraft.player.weapon;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WeaponHandler {

    private static WeaponHandler instance;

    public static WeaponHandler getInstance() {
        if (instance == null) {
            instance = new WeaponHandler();
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

        }

    }

    @SubscribeEvent
    public void onAutomaticFire(TickEvent.ClientTickEvent event) {

    }

    public void shootBullet(Player player, ItemStack weaponStack) {

    }
}
