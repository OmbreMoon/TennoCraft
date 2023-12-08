package com.ombremoon.tennocraft.object.custom.ability.volt;

import com.ombremoon.tennocraft.common.init.custom.FrameAbilities;
import com.ombremoon.tennocraft.player.data.AbilityType;
import com.ombremoon.tennocraft.player.AbstractFrameAbility;
import com.ombremoon.tennocraft.util.FrameUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class SpeedAbility extends AbstractFrameAbility {
    public SpeedAbility(AbilityType<?> abilityType, int energyRequired, int durationInTicks) {
        super(abilityType, energyRequired, 0, 0, durationInTicks, 0);
    }

    public SpeedAbility() {
        this(FrameAbilities.SPEED_ABILITY.get(), 25, 180);
    }

    @Override
    protected void onStart(Player player, Level level, BlockPos blockPos) {
        System.out.println(this.getAbilityDuration());
        addModifier(player, Attributes.MOVEMENT_SPEED, UUID.fromString("61073932-801c-4107-940c-18cbe661a042"), "movement_speed", 0.75F, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    protected void onStop(Player player, Level level, BlockPos blockPos) {
        super.onStop(player, level, blockPos);
        removeModifier(player, Attributes.MOVEMENT_SPEED, UUID.fromString("61073932-801c-4107-940c-18cbe661a042"));
    }
}
