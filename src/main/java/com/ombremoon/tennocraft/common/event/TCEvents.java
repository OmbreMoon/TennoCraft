package com.ombremoon.tennocraft.common.event;

import com.ombremoon.tennocraft.common.api.IModHolder;
import com.ombremoon.tennocraft.common.api.IWeaponModHolder;
import com.ombremoon.tennocraft.common.init.TCData;
import com.ombremoon.tennocraft.common.world.PlayerCombo;
import com.ombremoon.tennocraft.common.world.SlotGroup;
import com.ombremoon.tennocraft.common.world.TennoSlots;
import com.ombremoon.tennocraft.common.world.effect.StatusEffectInstance;
import com.ombremoon.tennocraft.main.Constants;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class TCEvents {

    @SubscribeEvent
    public static void onPlayerJoinedWorld(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        Level level = event.getLevel();
        if (!level.isClientSide) {
            if (entity instanceof Player player) {
                ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
                if (stack.getItem() instanceof IModHolder<?> modHolder) {
                    TennoSlots slots = player.getData(TCData.TENNO_SLOTS);
                    slots.switchSlots(player, SlotGroup.WEAPON, modHolder, stack);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        Level level = player.level();
        if (!level.isClientSide) {
            ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
            if (stack.getItem() instanceof IModHolder<?> modHolder) {
                PlayerCombo combo = player.getData(TCData.PLAYER_COMBO);
                combo.tick(player, stack);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerEquipmentChange(LivingEquipmentChangeEvent event) {
        Entity entity = event.getEntity();
        Level level = entity.level();
        if (!level.isClientSide) {
            if (entity instanceof Player player && event.getSlot() == EquipmentSlot.MAINHAND) {
                TennoSlots slots = player.getData(TCData.TENNO_SLOTS);
                ItemStack toStack = event.getTo();
                ItemStack fromStack = event.getFrom();
                if (toStack.getItem() instanceof IWeaponModHolder<?> modHolder) {
                    slots.switchSlots(player, SlotGroup.WEAPON, modHolder, toStack);
                } else if (fromStack.getItem() instanceof IModHolder<?>) {
                    if (!(toStack.getItem() instanceof IModHolder<?>))
                        slots.unequipFromSlot(SlotGroup.WEAPON);

                    PlayerCombo combo = player.getData(TCData.PLAYER_COMBO);
                    combo.resetCombo(player, fromStack);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEffectRemoved(MobEffectEvent.Remove event) {
        LivingEntity livingEntity = event.getEntity();
        if (event.getEffectInstance() instanceof StatusEffectInstance effectInstance && !livingEntity.level().isClientSide) {
            effectInstance.onEffectRemoved(livingEntity);
            effectInstance.removeStatusProc(livingEntity);
        }
    }

    @SubscribeEvent
    public static void onEffectExpired(MobEffectEvent.Expired event) {
        LivingEntity livingEntity = event.getEntity();
        if (event.getEffectInstance() instanceof StatusEffectInstance effectInstance && !livingEntity.level().isClientSide) {
            effectInstance.onEffectRemoved(livingEntity);
            effectInstance.removeStatusProc(livingEntity);
        }
    }
}
