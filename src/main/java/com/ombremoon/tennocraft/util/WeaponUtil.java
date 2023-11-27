package com.ombremoon.tennocraft.util;

import com.ombremoon.tennocraft.common.AttributeHandler;
import com.ombremoon.tennocraft.common.init.custom.FrameAttributes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class WeaponUtil {

    public static float getDamageModifier(ItemStack itemStack) {
        return AttributeHandler.getTagAttributeModifier(FrameAttributes.DAMAGE.get(), itemStack);
    }

    public static float getFireRateModifier(ItemStack itemStack) {
        return AttributeHandler.getTagAttributeModifier(FrameAttributes.FIRE_RATE.get(), itemStack);
    }

    public static float getCritChanceModifier(ItemStack itemStack) {
        return AttributeHandler.getTagAttributeModifier(FrameAttributes.CRIT_CHANCE.get(), itemStack);
    }

    public static float getCritDamageModifier(ItemStack itemStack) {
        return AttributeHandler.getTagAttributeModifier(FrameAttributes.CRIT_DAMAGE.get(), itemStack);
    }

    public static float getStatusModifier(ItemStack itemStack) {
        return AttributeHandler.getTagAttributeModifier(FrameAttributes.STATUS.get(), itemStack);
    }

    public static float getElementalDamageModifier(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return 0;
        } else  {
            float elementalDamage = 0;

            ListTag listTag = AttributeHandler.getFrameAttributeTags(itemStack);
            for (int i = 0; i < listTag.size(); i++) {
                CompoundTag compoundTag = listTag.getCompound(i);
                ResourceLocation resourceLocation = AttributeHandler.getFrameAttributeId(compoundTag);
                if (FrameAttributes.getFrameAttribute(resourceLocation).isElementalAttribute()) {
                    elementalDamage += AttributeHandler.getAttributeModifier(compoundTag);
                }
            }
            return elementalDamage;
        }
    }
}
