package com.ombremoon.tennocraft.common;

import com.google.common.collect.Maps;
import com.ombremoon.tennocraft.common.init.custom.FrameAttributes;
import com.ombremoon.tennocraft.player.attribute.FrameAttribute;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class AttributeHandler {
    public static final String FRAME_ATTR = "Frame Attributes";

    public static CompoundTag storeFrameAttribute(ResourceLocation attribute, double attributeModifier) {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString("attribute", String.valueOf((Object) attribute));
        compoundTag.putFloat("modifier", (float) attributeModifier);
        return compoundTag;
    }

    public static void setAttributeModifier(CompoundTag compoundTag, double attributeModifier) {
        compoundTag.putFloat("modifier", (float) attributeModifier);
    }

    public static float getAttributeModifier(CompoundTag compoundTag) {
        return compoundTag.getFloat("modifier");
    }

    public static ResourceLocation getFrameAttributeId(CompoundTag compoundTag) {
        return ResourceLocation.tryParse(compoundTag.getString("attribute"));
    }

    public static ResourceLocation getFrameAttributeId(FrameAttribute frameAttribute) {
        return frameAttribute.getResourceLocation();
    }

    public static ListTag getFrameAttributeTags(ItemStack itemStack) {
        return itemStack.getTag() != null ? itemStack.getTag().getList(FRAME_ATTR, 10) : new ListTag();
    }

    //Returns specific attribute modifier for a specified frame stack
    public static float getAttributeModifier(FrameAttribute frameAttribute, Player player) {
        Iterable<ItemStack> iterable = frameAttribute.getTransferenceSlotItems(player).values();
        if (iterable == null) {
            return 0;
        } else {
            float i = 0;

            for(ItemStack itemstack : iterable) {
                float j = getTagAttributeModifier(frameAttribute, itemstack);
                if (j > i) {
                    i = j;
                }
            }

            return i;
        }
    }

    public static float getTagAttributeModifier(FrameAttribute frameAttribute, ItemStack itemStack) {
        if (!itemStack.isEmpty()) {
            ResourceLocation resourceLocation = getFrameAttributeId(frameAttribute);
            ListTag listTag = getFrameAttributeTags(itemStack);

            for (int i = 0; i < listTag.size(); i++) {
                CompoundTag compoundTag = listTag.getCompound(i);
                ResourceLocation resourceLocation1 = getFrameAttributeId(compoundTag);
                if (resourceLocation1 != null && resourceLocation1.equals(resourceLocation)) {
                    return getAttributeModifier(compoundTag);
                }
            }
        }
        return 0;
    }

    public static void setTagAttributeModifier(FrameAttribute frameAttribute, ItemStack itemStack, float amount) {
        if (!itemStack.isEmpty()) {
            ResourceLocation resourceLocation = getFrameAttributeId(frameAttribute);
            ListTag listTag = getFrameAttributeTags(itemStack);

            for (int i = 0; i < listTag.size(); i++) {
                CompoundTag compoundTag = listTag.getCompound(i);
                ResourceLocation resourceLocation1 = getFrameAttributeId(compoundTag);
                if (resourceLocation1 != null && resourceLocation1.equals(resourceLocation)) {
                    setAttributeModifier(compoundTag, amount);
                }
            }
        }
    }

    //Returns all attribute modifiers for a specified frame stack
    public static Map<FrameAttribute, Float> getFrameAttributes(ItemStack itemStack) {
        ListTag listTag = getFrameAttributeTags(itemStack);
        return deserializeAttributes(listTag);
    }

    private static Map<FrameAttribute, Float> deserializeAttributes(ListTag listTag) {
        Map<FrameAttribute, Float> map = Maps.newLinkedHashMap();

        for (int i = 0; i < listTag.size(); i++) {
            CompoundTag compoundTag = listTag.getCompound(i);
            map.put(FrameAttributes.getFrameAttribute(getFrameAttributeId(compoundTag)), getAttributeModifier(compoundTag));
        }
        return map;
    }
}
