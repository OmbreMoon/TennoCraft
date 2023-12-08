package com.ombremoon.tennocraft.object.custom.ability.excalibur;

import com.ombremoon.tennocraft.common.init.custom.FrameAbilities;
import com.ombremoon.tennocraft.player.data.AbilityType;
import com.ombremoon.tennocraft.player.AbstractFrameAbility;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class RadialBlindAbility extends AbstractFrameAbility {
    public RadialBlindAbility(AbilityType<?> abilityType, int energyRequired, int durationInTicks, float initialRange) {
        super(abilityType, energyRequired, 0, 0, durationInTicks, initialRange);
    }

    public RadialBlindAbility() {
        this(FrameAbilities.RADIAL_BLIND_ABILITY.get(), 50, 7, 4);
    }

    @Override
    protected void onStart(Player player, Level level, BlockPos blockPos) {

    }
}
