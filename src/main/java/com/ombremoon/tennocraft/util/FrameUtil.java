package com.ombremoon.tennocraft.util;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.ombremoon.tennocraft.common.AttributeHandler;
import com.ombremoon.tennocraft.common.init.custom.FrameAbilities;
import com.ombremoon.tennocraft.common.init.custom.FrameAttributes;
import com.ombremoon.tennocraft.object.item.mineframe.TransferenceKeyItem;
import com.ombremoon.tennocraft.player.ability.AbilityManager;
import com.ombremoon.tennocraft.player.ability.AbilityType;
import com.ombremoon.tennocraft.player.ability.AbstractFrameAbility;
import com.ombremoon.tennocraft.player.attribute.FrameAttribute;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.RegistryObject;
import top.theillusivec4.curios.api.CuriosCapability;

import java.util.Map;

public class FrameUtil {
    public static final String CURIO_SLOT = "tenno";
    public static final String TRANSFERENCE = "transference";
    public static final String FRAME_ATTR = "Frame Attributes";
    public static final byte TRANSFERENCE_SLOTS = 7;


    public static boolean hasOnFrame(LivingEntity livingEntity) {
        return livingEntity.getTags().contains(TRANSFERENCE);
    }

    public static ItemStack getFrameStack(Player player) {
        return player.getCapability(CuriosCapability.INVENTORY).orElseThrow(NullPointerException::new).getStacksHandler(CURIO_SLOT).get().getStacks().getStackInSlot(0);
    }

    public static AbstractFrameAbility getAbilityByName(ResourceLocation location) {
        for (RegistryObject<AbilityType<?>> abilityType : FrameAbilities.FRAME_ABILITY.getEntries()) {
            if (abilityType.getId().equals(location)) {
                return abilityType.get().getSupplier();
            }
        }
        return null;
    }

    public static void initFrameAbility(Player player, Level level, BlockPos blockPos, ItemStack itemStack, AbilityType<?> abilityType) {
        if (!level.isClientSide()) {
            AbstractFrameAbility frameAbility = abilityType.getSupplier();
            if (frameAbility != null) {
                frameAbility.setUser(player);
                frameAbility.setLevel((ServerLevel) level);
                frameAbility.setBlockPos(blockPos);
                AbilityManager.addAbility(frameAbility);
                frameAbility.start();
                decreaseEnergy(player, itemStack, frameAbility);
            }
        }
    }

    public static void initFrameAttributes(ItemStack frameStack, TransferenceKeyItem.FrameType frameType) {
        ListTag attributeList = new ListTag();
        CompoundTag compoundTag = frameStack.getOrCreateTag();
        if (!compoundTag.contains("Frame Attributes", 9)) {
            compoundTag.put("Frame Attributes", attributeList);
            initFrameAttributes(frameType, attributeList);
        }
    }

    public static void decreaseEnergy(Player player, ItemStack itemStack, AbstractFrameAbility frameAbility) {
        AttributeHandler.setTagAttributeModifier(FrameAttributes.ENERGY.get(), itemStack, AttributeHandler.getAttributeModifier(FrameAttributes.ENERGY.get(), player) - frameAbility.getEnergyRequired());
    }

    private static void initFrameAttributes(TransferenceKeyItem.FrameType frameType, ListTag listTag) {
        listTag.add(AttributeHandler.storeFrameAttribute(AttributeHandler.getFrameAttributeId(FrameAttributes.HEALTH.get()), frameType.getFrameHealth()));
        listTag.add(AttributeHandler.storeFrameAttribute(AttributeHandler.getFrameAttributeId(FrameAttributes.SHIELD.get()), frameType.getFrameShield()));
        listTag.add(AttributeHandler.storeFrameAttribute(AttributeHandler.getFrameAttributeId(FrameAttributes.ARMOR.get()), frameType.getFrameArmor()));
        listTag.add(AttributeHandler.storeFrameAttribute(AttributeHandler.getFrameAttributeId(FrameAttributes.ENERGY.get()), frameType.getFrameEnergy()));
    }

    /*public static void setAttributeModifier(CompoundTag compoundTag, double attributeModifier) {
        compoundTag.putFloat("modifier", (float) attributeModifier);
    }*/

    /*public static float getAttributeModifier(CompoundTag compoundTag) {
        return compoundTag.getFloat("modifier");
    }

    public static ResourceLocation getFrameAttributeId(CompoundTag compoundTag) {
        return ResourceLocation.tryParse(compoundTag.getString("attribute"));
    }*/

    /*public static ResourceLocation getFrameAttributeId(FrameAttribute frameAttribute) {
        return frameAttribute.getResourceLocation();
    }*/

    /*public static ListTag getFrameAttributeTags(ItemStack itemStack) {
        return itemStack.getTag() != null ? itemStack.getTag().getList(FRAME_ATTR, 10) : new ListTag();
    }*/

    //Returns specific attribute modifier for a specified frame stack
    /*public static float getAttributeModifier(FrameAttribute frameAttribute, Player player) {
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
    }*/

    /*public static float getTagAttributeModifier(FrameAttribute frameAttribute, ItemStack itemStack) {
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
    }*/

    /*public static void setTagAttributeModifier(FrameAttribute frameAttribute, ItemStack itemStack, float amount) {
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
    }*/

    //Returns all attribute modifiers for a specified frame stack
    /*public static Map<FrameAttribute, Float> getFrameAttributes(ItemStack itemStack) {
        ListTag listTag = getFrameAttributeTags(itemStack);
        return deserializeAttributes(listTag);
    }*/

    /*private static Map<FrameAttribute, Float> deserializeAttributes(ListTag listTag) {
        Map<FrameAttribute, Float> map = Maps.newLinkedHashMap();

        for (int i = 0; i < listTag.size(); i++) {
            CompoundTag compoundTag = listTag.getCompound(i);
            map.put(FrameAttributes.getFrameAttribute(getFrameAttributeId(compoundTag)), getAttributeModifier(compoundTag));
        }
        return map;
    }*/

    public static int getFrameEnergy(ItemStack itemStack) {
        int moddedEnergy = 0;
        Map<FrameAttribute, Float> frameAttributes = AttributeHandler.getFrameAttributes(itemStack);
        for (Map.Entry<FrameAttribute, Float> entry : frameAttributes.entrySet()) {
            FrameAttribute frameAttribute = entry.getKey();
            float attributeModifier = entry.getValue();
            if (frameAttribute == FrameAttributes.ENERGY.get()) {
                moddedEnergy += attributeModifier;
            }
        }
        return moddedEnergy;
    }

    public static AbilityType<?> getFirstAbility(TransferenceKeyItem tokenItem) {
        return hasAbility(tokenItem, 1) ? tokenItem.getAbilityList().get().get(0) : FrameAbilities.EMPTY.get();
    }

    public static AbilityType<?> getSecondAbility(TransferenceKeyItem tokenItem) {
        return hasAbility(tokenItem, 2) ? tokenItem.getAbilityList().get().get(1) : FrameAbilities.EMPTY.get();
    }

    public static AbilityType<?> getThirdAbility(TransferenceKeyItem tokenItem) {
        return hasAbility(tokenItem, 3) ? tokenItem.getAbilityList().get().get(2) : FrameAbilities.EMPTY.get();
    }

    public static AbilityType<?> getUltimateAbility(TransferenceKeyItem tokenItem) {
        return hasAbility(tokenItem, 4) ? tokenItem.getAbilityList().get().get(3) : FrameAbilities.EMPTY.get();
    }

    private static boolean hasAbility(TransferenceKeyItem tokenItem, int i) {
        return tokenItem.getAbilityList().get().size() >= i;
    }
}
