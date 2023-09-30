package com.ombremoon.tennocraft.util;

import com.ombremoon.tennocraft.object.item.mineframe.FrameArmorItem;
import com.ombremoon.tennocraft.player.ability.AbilityManager;
import com.ombremoon.tennocraft.player.ability.AbilityType;
import com.ombremoon.tennocraft.player.ability.AbstractFrameAbility;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class FrameUtil {

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

    public static FrameArmorItem<?> getFrameFromType(FrameArmorItem.FrameType frameType) {
        return (FrameArmorItem<?>) frameType.getFrameArmorItem();
    }

    public static FrameArmorItem<?> getFrameFromAbility(AbilityType<?> ability) {
        return (FrameArmorItem<?>) ability.getFrameType().getFrameArmorItem();
    }

    public static void initFrameAbility(Player player, Level level, BlockPos blockPos, AbilityType<?> abilityType) {
        if (!level.isClientSide()) {
            AbstractFrameAbility frameAbility = abilityType.create();
            if (frameAbility != null) {
                frameAbility.setUser(player);
                frameAbility.setLevel((ServerLevel) level);
                frameAbility.setBlockPos(blockPos);
                AbilityManager.addAbility(frameAbility);
                frameAbility.start();
                System.out.println(frameAbility.getDurationInTicks());
            }
        }
    }

    public static AbilityType<?> getFirstAbility(FrameArmorItem<?> frameArmorItem) {
        return frameArmorItem.getAbilityList().get().get(0);
    }

    public static AbilityType<?> getSecondAbility(FrameArmorItem<?> frameArmorItem) {
        return frameArmorItem.getAbilityList().get().get(1);
    }

    public static AbilityType<?> getThirdAbility(FrameArmorItem<?> frameArmorItem) {
        return frameArmorItem.getAbilityList().get().get(2);
    }

    public static AbilityType<?> getUltimateAbility(FrameArmorItem<?> frameArmorItem) {
        return frameArmorItem.getAbilityList().get().get(3);
    }
}
