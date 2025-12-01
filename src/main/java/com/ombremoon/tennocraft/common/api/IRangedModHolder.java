package com.ombremoon.tennocraft.common.api;


import com.ombremoon.tennocraft.common.api.weapon.schema.RangedWeaponSchema;
import com.ombremoon.tennocraft.common.init.TCData;
import com.ombremoon.tennocraft.common.networking.PayloadHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface IRangedModHolder extends IWeaponModHolder<RangedWeaponSchema> {

    default boolean isReloading(LivingEntity entity, ItemStack stack) {
        var handler = stack.get(TCData.RANGED_WEAPON_HANDLER);
        if (handler != null) {
            handler.ensureRegistryAccess(entity.registryAccess());
            return handler.isReloading();
        }
        return false;
    }

    default void reload(LivingEntity entity, ItemStack stack) {
        var handler = stack.get(TCData.RANGED_WEAPON_HANDLER);
        if (handler != null && entity.level() instanceof ServerLevel level) {
            int reloadTime = this.schema(stack).getModdedReloadTime(level, stack, entity, this.getTriggerType(entity, stack));
            handler.reloadTick = entity.tickCount + reloadTime;
            handler.setReloading(true);
            if (entity instanceof ServerPlayer player) {
                PayloadHandler.clientReload(player, handler.reloadTick);
            }
        }
    }

    default void setCharging(LivingEntity entity, ItemStack stack, boolean charging) {
        var handler = stack.get(TCData.RANGED_WEAPON_HANDLER);
        if (handler != null) {
            handler.ensureRegistryAccess(entity.registryAccess());
            handler.isChargingOrChanneling = charging;
        }
    }

    default void cycleAlternateFire(LivingEntity entity, ItemStack stack) {
        var handler = stack.get(TCData.RANGED_WEAPON_HANDLER);
        if (handler != null) {
            handler.ensureRegistryAccess(entity.registryAccess());
            handler.cycleAlternateFire(stack);
        }
    }

    default void consumeAmmo(ItemStack stack, int ammo) {
        stack.set(TCData.MAG_COUNT, Math.max(stack.getOrDefault(TCData.MAG_COUNT, 0) - ammo, 0));
        stack.set(TCData.AMMO_COUNT, Math.max(stack.getOrDefault(TCData.AMMO_COUNT, 0) - ammo, 0));
    }

    default boolean hasAmmo(ItemStack stack) {
        Integer ammo = stack.get(TCData.AMMO_COUNT);
        return ammo == null || ammo > 0;
    }

    default boolean hasEnoughAmmo(ItemStack stack, int ammoCost) {
        Integer ammo = stack.get(TCData.MAG_COUNT);
        return ammo != null && ammo >= ammoCost;
    }
}
