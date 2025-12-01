package com.ombremoon.tennocraft.client.event;


import com.ombremoon.tennocraft.client.KeyBinds;
import com.ombremoon.tennocraft.common.api.IRangedModHolder;
import com.ombremoon.tennocraft.common.api.handler.RangedWeaponHandler;
import com.ombremoon.tennocraft.common.api.weapon.ranged.reload.DurationReloadType;
import com.ombremoon.tennocraft.common.api.weapon.ranged.reload.NoReloadType;
import com.ombremoon.tennocraft.common.api.weapon.ranged.reload.ReloadType;
import com.ombremoon.tennocraft.common.api.weapon.ranged.trigger.*;
import com.ombremoon.tennocraft.common.api.weapon.schema.data.RangedAttack;
import com.ombremoon.tennocraft.common.init.TCData;
import com.ombremoon.tennocraft.common.networking.PayloadHandler;
import com.ombremoon.tennocraft.main.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.ArrayList;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class TriggerEvents {

    /**
     * Implement 1 of each trigger type & test
     * Implement hitscan
     * Implement damage ramp/decay & fire rate damage
     * Finish Ranged Attack attributes
     * Finish Projectile Types
     * Implement bullet system
     */

    @SubscribeEvent
    public static void onInputTriggered(InputEvent.InteractionKeyMappingTriggered event) {
        if (event.isCanceled()) return;

        Player player = Minecraft.getInstance().player;
        if (player != null) {
            /*if (isStunned) {
                event.setSwingHand(false);
                event.setCanceled(true);
                return;
            }*/

            ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
            if (stack.getItem() instanceof IRangedModHolder) {
                if (KeyBinds.getShootMapping().isDown()) {
                    event.setSwingHand(false);
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void shootOrChargeWeapon(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        Level level = player.level();

        if (!level.isClientSide) return;

        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);

        if (!canShootProjectile() || !(stack.getItem() instanceof IRangedModHolder modHolder)) return;

        TriggerType<?> triggerType = modHolder.getTriggerType(player, stack);
        RangedWeaponHandler handler = stack.get(TCData.RANGED_WEAPON_HANDLER);
        RangedAttack attack = modHolder.schema(stack).getAttack(triggerType);
        ReloadType<?> reloadType = attack.reloadType();

        checkForReload(player, stack, modHolder, handler, reloadType, attack.ammoCost());
        if (handler == null/* || !canShootProjectile(player, stack, modHolder, triggerType, attack.ammoCost())*/) return;

        if (handler.chargedShotDelay != 0) {
            if (player.tickCount >= handler.chargedShotDelay) {
                shootBullet();
                handler.chargedShotDelay = 0;
            }

            return;
        }

        boolean flag = KeyBinds.getShootMapping().isDown();
        if (flag) {
            if (!handler.isFiring)
                handler.shotCounter = 0;

            int fireRate = getFireRate(handler, triggerType);
            if (handler.lastShotTick == 0 || player.tickCount - handler.lastShotTick >= fireRate) {
                if (triggerType.isAutoFire()) {
                    shootBullet();
                } else if (triggerType.shouldCharge()) {
                    if (!handler.isChargingOrChanneling) {
                        if (triggerType.is(DuplexTrigger.TYPE)) {
                            shootBullet(false, false);
                        }

                        handler.isChargingOrChanneling = true;
                        PayloadHandler.setChargingOrChanneling(true);
                    } else if (triggerType instanceof ChargeTrigger charge) {
                        chargeShot(handler, charge);
                    }
                } else {
                    shootBullet();
                    KeyBinds.getShootMapping().setDown(triggerType instanceof BurstTrigger burst && (burst.burstCount() > 0 && handler.shotCounter != burst.burstCount() || stack.getOrDefault(TCData.MAG_COUNT, 0) != 0));
                }
            }
        } else if (handler.isChargingOrChanneling && triggerType instanceof ChargeTrigger charge && charge.type().chargesAutomatically()) {
            chargeShot(handler, charge);
        } else if (handler.isChargingOrChanneling) {
            releaseChargedShot(handler, triggerType);
        } else if (handler.isFiring) {
            handler.stopShooting();
            PayloadHandler.stopShooting();
        }

        while (KeyBinds.ALTERNATE_FIRE_BINDING.consumeClick()) {
            var list = stack.getOrDefault(TCData.ACTIVE_DEPLOYABLES, new ArrayList<>());
            if (triggerType.is(ActiveTrigger.TYPE) && !list.isEmpty()) {
                PayloadHandler.activateDeployables();
            } else {
                PayloadHandler.cycleAlternateFire();
            }
        }
    }

    private static int getFireRate(RangedWeaponHandler handler, TriggerType<?> triggerType) {
        float fireRate = 20.0F / handler.getFireRate();
        if (triggerType instanceof AutoSpoolTrigger autoSpool) {
            fireRate = autoSpool.spoolFireRate(handler.shotCounter, fireRate);
        } else if (triggerType instanceof BurstTrigger burst) {
            fireRate = handler.shotCounter < burst.burstCount() ? burst.delayTicks() : 1 / handler.getFireRate();
        }

        return Math.max(Math.round(fireRate), 1);
    }

    //MOVE TO SERVER
    private static void chargeShot(RangedWeaponHandler handler, ChargeTrigger charge) {
        handler.chargeTick++;
        if (handler.chargeTick >= charge.getMinCharge() && charge.releaseDelay() >= 0) {
            releaseChargedShot(handler, charge);
        }
    }

    private static void releaseChargedShot(RangedWeaponHandler handler, TriggerType<?> triggerType) {
        if (triggerType instanceof ChargeTrigger charge) {
            if (handler.chargeTick > charge.getMinCharge()) {
                if (charge.releaseDelay() > 0) {
                    handler.chargedShotDelay = charge.releaseDelay();
                } else {
                    shootBullet(handler.chargeTick >= charge.chargeTime(), true);
                }

                handler.chargeTick = 0;
            }
        } else {
            shootBullet();
        }

        KeyBinds.getShootMapping().setDown(false);
        handler.isChargingOrChanneling = false;
        PayloadHandler.setChargingOrChanneling(false);
    }

    private static void checkForReload(Player player, ItemStack stack, IRangedModHolder modHolder, RangedWeaponHandler handler, ReloadType<?> reloadType, int ammoCost) {
        if (!(reloadType instanceof DurationReloadType || reloadType instanceof NoReloadType) && modHolder.hasAmmo(stack) && !modHolder.hasEnoughAmmo(stack, ammoCost) && !handler.isReloading()) {
            handler.setReloading(true);
            KeyBinds.getShootMapping().setDown(false);
            handler.stopShooting();
            PayloadHandler.reload();
        } else if (handler.isReloading() && player.tickCount >= handler.reloadTick) {
            handler.setReloading(false);
        }
    }

    private static boolean canShootProjectile(Player player, ItemStack stack, IRangedModHolder modHolder, TriggerType<?> type, int ammoCost) {
        if (player.isSpectator())
            return false;

        boolean triggerFlag = true;
        if (type instanceof ActiveTrigger active) {
            var deployables = stack.getOrDefault(TCData.ACTIVE_DEPLOYABLES, new ArrayList<>());
            triggerFlag = deployables.size() < active.maxDeployables();
        }

        return triggerFlag && !modHolder.isReloading(player, stack) && modHolder.hasEnoughAmmo(stack, ammoCost);
    }

    private static boolean canShootProjectile() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.getOverlay() != null) return false;
        if (minecraft.screen != null) return false;
        if (!minecraft.mouseHandler.isMouseGrabbed()) return false;
        return minecraft.isWindowActive();
    }

    public static void shootBullet(boolean chargeFlag, boolean duplexFlag) {
        PayloadHandler.shootProjectile(chargeFlag, duplexFlag);
    }

    public static void shootBullet() {
        shootBullet(false, true);
    }
}
