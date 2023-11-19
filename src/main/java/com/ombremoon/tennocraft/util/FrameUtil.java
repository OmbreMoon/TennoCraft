package com.ombremoon.tennocraft.util;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.ombremoon.tennocraft.common.init.custom.FrameAbilities;
import com.ombremoon.tennocraft.common.init.custom.FrameAttributes;
import com.ombremoon.tennocraft.object.item.mineframe.FrameArmorItem;
import com.ombremoon.tennocraft.object.item.mineframe.TransferenceTokenItem;
import com.ombremoon.tennocraft.object.item.mineframe.helmet.FrameHelmetItem;
import com.ombremoon.tennocraft.player.ability.AbilityManager;
import com.ombremoon.tennocraft.player.ability.AbilityType;
import com.ombremoon.tennocraft.player.ability.AbstractFrameAbility;
import com.ombremoon.tennocraft.player.attribute.FrameAttribute;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.RegistryObject;
import top.theillusivec4.curios.api.CuriosCapability;

import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class FrameUtil {
    public static final String CURIO_SLOT = "tenno";
    public static final String TRANSFERENCE = "transference";
    public static final String FRAME_ATTR = "Frame Attributes";
    private static int moddedEnergy;

/*
    public static boolean hasOnFrame(LivingEntity entity) {
        Iterable<ItemStack> armorSlots = entity.getArmorSlots();
        Stream<ItemStack> frameSlots = StreamSupport.stream(armorSlots.spliterator(), false);
        boolean flag = frameSlots.allMatch(itemStack -> itemStack.getItem() instanceof FrameArmorItem<?>);
        if (flag) {
           FrameArmorItem.FrameType frameType = ((FrameArmorItem<?>)StreamSupport.stream(armorSlots.spliterator(), false).toList().get(0).getItem()).getFrameType();
            for (ItemStack itemStack : entity.getArmorSlots()) {
                FrameArmorItem<?> armorItem = (FrameArmorItem<?>) itemStack.getItem();
                if (!armorItem.getFrameType().equals(frameType)) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }
*/

    public static boolean hasOnFrame(LivingEntity livingEntity) {
        return livingEntity.getTags().contains(TRANSFERENCE);
    }

    public static FrameHelmetItem<?> getFrameFromType(FrameArmorItem.FrameType frameType) {
        return (FrameHelmetItem<?>) frameType.getFrameArmorItem().get();
    }

    public static FrameHelmetItem<?> getFrameFromAbility(AbilityType<?> ability) {
        return (FrameHelmetItem<?>) ability.getFrameType().getFrameArmorItem().get();
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

    public static void initFrameAbility(Player player, Level level, BlockPos blockPos, AbilityType<?> abilityType) {
        if (!level.isClientSide()) {
            AbstractFrameAbility frameAbility = abilityType.getSupplier();
            if (frameAbility != null) {
                frameAbility.setUser(player);
                frameAbility.setLevel((ServerLevel) level);
                frameAbility.setBlockPos(blockPos);
                AbilityManager.addAbility(frameAbility);
//                initFrameAttributes(player);
                frameAbility.start();
            }
        }
    }

    private static void initFrameAttributes(Player player) {
        ItemStack frameStack = getFrameStack(player);
        CompoundTag compoundTag = frameStack.getOrCreateTag();
        if (!compoundTag.contains("Frame Attributes", 9)) {
            compoundTag.put("Frame Attributes", new ListTag());
        }
    }

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

    //Returns all attribute modifiers for a specified frame stack
    public static Multimap<FrameAttribute, Float> getFrameAttributes(ItemStack itemStack) {
        ListTag listTag = getFrameAttributeTags(itemStack);
        return deserializeAttributes(listTag);
    }

    public static Multimap<FrameAttribute, Float> deserializeAttributes(ListTag listTag) {
        Multimap<FrameAttribute, Float> map = ArrayListMultimap.create();

        for (int i = 0; i < listTag.size(); i++) {
            CompoundTag compoundTag = listTag.getCompound(i);
            map.put(FrameAttributes.getFrameAttribute(getFrameAttributeId(compoundTag)), getAttributeModifier(compoundTag));
        }
        return map;
    }

    public static int getFrameEnergy(ItemStack itemStack) {
        Multimap<FrameAttribute, Float> frameAttributes = FrameUtil.getFrameAttributes(itemStack);
        for (Map.Entry<FrameAttribute, Float> entry : frameAttributes.entries()) {
            FrameAttribute frameAttribute = entry.getKey();
            float attributeModifier = entry.getValue();
            if (frameAttribute == FrameAttributes.ENERGY.get()) {
                moddedEnergy += attributeModifier;
            }
        }
        return moddedEnergy;
    }

    public static AbilityType<?> getSecondAbility(FrameHelmetItem<?> frameHelmetItem) {
        return hasAbility(frameHelmetItem, 2) ? frameHelmetItem.getAbilityList().get().get(1) : FrameAbilities.EMPTY.get();
    }

    public static AbilityType<?> getThirdAbility(FrameHelmetItem<?> frameHelmetItem) {
        return hasAbility(frameHelmetItem, 3) ? frameHelmetItem.getAbilityList().get().get(2) : FrameAbilities.EMPTY.get();
    }

    public static AbilityType<?> getUltimateAbility(FrameHelmetItem<?> frameHelmetItem) {
        return hasAbility(frameHelmetItem, 4) ? frameHelmetItem.getAbilityList().get().get(3) : FrameAbilities.EMPTY.get();
    }

    public static AbilityType<?> getFirstAbility(TransferenceTokenItem tokenItem) {
        return hasAbility(tokenItem, 1) ? tokenItem.getAbilityList().get().get(0) : FrameAbilities.EMPTY.get();
    }

    public static AbilityType<?> getSecondAbility(TransferenceTokenItem tokenItem) {
        return hasAbility(tokenItem, 2) ? tokenItem.getAbilityList().get().get(1) : FrameAbilities.EMPTY.get();
    }

    public static AbilityType<?> getThirdAbility(TransferenceTokenItem tokenItem) {
        return hasAbility(tokenItem, 3) ? tokenItem.getAbilityList().get().get(2) : FrameAbilities.EMPTY.get();
    }

    public static AbilityType<?> getUltimateAbility(TransferenceTokenItem tokenItem) {
        return hasAbility(tokenItem, 4) ? tokenItem.getAbilityList().get().get(3) : FrameAbilities.EMPTY.get();
    }

    private static boolean hasAbility(TransferenceTokenItem tokenItem, int i) {
        return tokenItem.getAbilityList().get().size() >= i;
    }

    private static boolean hasAbility(FrameHelmetItem<?> frameHelmetItem, int i) {
        return frameHelmetItem.getAbilityList().get().size() >= i;
    }
}
