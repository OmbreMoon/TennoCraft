package com.ombremoon.tennocraft.util;

import com.ombremoon.tennocraft.common.AttributeHandler;
import com.ombremoon.tennocraft.common.init.custom.FrameAbilities;
import com.ombremoon.tennocraft.common.init.custom.FrameAttributes;
import com.ombremoon.tennocraft.object.item.TransferenceKeyItem;
import com.ombremoon.tennocraft.player.data.AbilityManager;
import com.ombremoon.tennocraft.player.data.AbilityType;
import com.ombremoon.tennocraft.player.AbstractFrameAbility;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.RegistryObject;
import top.theillusivec4.curios.api.CuriosCapability;

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
        System.out.println(AttributeHandler.getAttributeModifier(FrameAttributes.ENERGY.get(), player));
        AttributeHandler.setTagAttributeModifier(FrameAttributes.ENERGY.get(), itemStack, AttributeHandler.getAttributeModifier(FrameAttributes.ENERGY.get(), player) - frameAbility.getEnergyRequired());
    }

    private static void initFrameAttributes(TransferenceKeyItem.FrameType frameType, ListTag listTag) {
        listTag.add(AttributeHandler.storeFrameAttribute(AttributeHandler.getFrameAttributeId(FrameAttributes.HEALTH.get()), frameType.getFrameHealth()));
        listTag.add(AttributeHandler.storeFrameAttribute(AttributeHandler.getFrameAttributeId(FrameAttributes.SHIELD.get()), frameType.getFrameShield()));
        listTag.add(AttributeHandler.storeFrameAttribute(AttributeHandler.getFrameAttributeId(FrameAttributes.ARMOR.get()), frameType.getFrameArmor()));
        listTag.add(AttributeHandler.storeFrameAttribute(AttributeHandler.getFrameAttributeId(FrameAttributes.ENERGY.get()), frameType.getFrameEnergy()));
    }

    public static float getFrameEnergy(Player player) {
        return AttributeHandler.getAttributeModifier(FrameAttributes.ENERGY.get(), player);
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
