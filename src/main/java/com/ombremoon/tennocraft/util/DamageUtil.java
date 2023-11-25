package com.ombremoon.tennocraft.util;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.ombremoon.tennocraft.common.AttributeHandler;
import com.ombremoon.tennocraft.common.init.custom.FrameAttributes;
import com.ombremoon.tennocraft.player.attribute.FrameAttribute;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosCapability;

import java.util.Map;

public class DamageUtil {

    public static void doPostHurtEffects(LivingEntity target, Entity attacker) {
        DamageUtil.AttributeVisitor attribute$visitor = (frameAttribute, amount) -> {
            frameAttribute.doPostHurt(target, attacker, amount);
        };
        if (target != null) {
            runIterationOnInventory(attribute$visitor, getTransferenceSlotItems((Player) target));
        }

        if (false)
        if (attacker instanceof Player) {
            runIterationOnItem(attribute$visitor, target.getMainHandItem());
        }
    }

    public static void doPostDamageEffects(LivingEntity attacker, Entity target) {
        DamageUtil.AttributeVisitor attribute$visitor = (frameAttribute, amount) -> {
            frameAttribute.doPostAttack(attacker, target, amount);
        };
        if (attacker != null) {
            runIterationOnInventory(attribute$visitor, getTransferenceSlotItems((Player) attacker));
        }

        if (false)
        if (target instanceof Player) {
            runIterationOnItem(attribute$visitor, attacker.getMainHandItem());
        }
    }

    public static Iterable<ItemStack> getTransferenceSlotItems(Player player) {
        Map<Integer, ItemStack> map = Maps.newHashMap();
        for (int i = 0; i < FrameUtil.TRANSFERENCE_SLOTS; i++) {
            ItemStack itemStack = player.getCapability(CuriosCapability.INVENTORY).orElseThrow(NullPointerException::new).getStacksHandler(FrameUtil.CURIO_SLOT).get().getStacks().getStackInSlot(i);
            if (!itemStack.isEmpty()) {
                map.put(i, itemStack);
            }
        }
        return map.values();
    }

    private static void runIterationOnItem(DamageUtil.AttributeVisitor attributeVisitor, ItemStack itemStack) {
        if (!itemStack.isEmpty()) {
            if (true) {
                for (Map.Entry<FrameAttribute, Float> entry : AttributeHandler.getFrameAttributes(itemStack).entrySet()) {
                    attributeVisitor.accept(entry.getKey(), entry.getValue());
                }
                return;
            }
            ListTag listTag = AttributeHandler.getFrameAttributeTags(itemStack);

            for (int i = 0; i < listTag.size(); i++) {
                CompoundTag compoundTag = listTag.getCompound(i);
                FrameAttributes.REGISTRY.get().getHolder(AttributeHandler.getFrameAttributeId(compoundTag)).ifPresent(frameAttributeHolder -> {
                    attributeVisitor.accept(frameAttributeHolder.get(), AttributeHandler.getAttributeModifier(compoundTag));
                });
            }
        }
    }

    private static void runIterationOnInventory(DamageUtil.AttributeVisitor attributeVisitor, Iterable<ItemStack> itemStacks) {
        for(ItemStack itemStack : itemStacks) {
            runIterationOnItem(attributeVisitor, itemStack);
        }
    }

    @FunctionalInterface
    interface AttributeVisitor {
        void accept(FrameAttribute frameAttribute, float amount);
    }
}
