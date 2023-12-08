package com.ombremoon.tennocraft.object.custom.ability.excalibur;

import com.ombremoon.tennocraft.common.init.custom.FrameAbilities;
import com.ombremoon.tennocraft.player.data.AbilityType;
import com.ombremoon.tennocraft.player.AbstractFrameAbility;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ExaltedBladeAbility extends AbstractFrameAbility {
    public ExaltedBladeAbility(AbilityType<?> abilityType, int energyRequired, float energyPerSecond, int baseDamage, int durationInTicks, float initialRange) {
        super(abilityType, energyRequired, energyPerSecond, baseDamage, durationInTicks, initialRange);
    }

    public ExaltedBladeAbility() {
        this(FrameAbilities.EXALTED_BLADE_ABILITY.get(), 25, 2.5F, 100, 6, 2.5F);
    }

    @Override
    protected void onStart(Player player, Level level, BlockPos blockPos) {

    }
}
