package com.ombremoon.tennocraft.object.custom.ability.frame.volt;

import com.ombremoon.tennocraft.common.init.custom.FrameAbilities;
import com.ombremoon.tennocraft.player.data.AbilityType;
import com.ombremoon.tennocraft.player.AbstractFrameAbility;
import com.ombremoon.tennocraft.util.FrameUtil;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class SpeedAbility extends AbstractFrameAbility {
    public SpeedAbility(AbilityType<?> abilityType, int energyRequired, int durationInTicks) {
        super(abilityType, energyRequired, 0, durationInTicks, 0);
    }

    public SpeedAbility() {
        this(FrameAbilities.SPEED_ABILITY.get(), 25, 180);
    }

    @Override
    protected void onStart() {
        Player player = level.getPlayerByUUID(userID);
        if (player != null) {
            ItemStack itemStack = FrameUtil.getFrameStack(player);
            System.out.println(this.getAbilityDuration());
            addModifier(player, Attributes.MOVEMENT_SPEED, UUID.fromString("61073932-801c-4107-940c-18cbe661a042"), "movement_speed", 0.75F, AttributeModifier.Operation.MULTIPLY_TOTAL);
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        Player player = level.getPlayerByUUID(userID);
        if (player != null) {
            removeModifier(player, Attributes.MOVEMENT_SPEED, UUID.fromString("61073932-801c-4107-940c-18cbe661a042"));
        }
        super.onStop();
    }
}
