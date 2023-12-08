package com.ombremoon.tennocraft.object.custom.ability.excalibur;

import com.ombremoon.tennocraft.common.init.custom.FrameAbilities;
import com.ombremoon.tennocraft.player.data.AbilityType;
import com.ombremoon.tennocraft.player.AbstractFrameAbility;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class RadialJavelinAbility extends AbstractFrameAbility {
    public RadialJavelinAbility(AbilityType<?> abilityType, int energyRequired, int baseStrength, float initialRange) {
        super(abilityType, energyRequired, 0, baseStrength, 1, initialRange);
    }

    public RadialJavelinAbility() {
        this(FrameAbilities.RADIAL_JAVELIN_ABILITY.get(), 50, 250, 7.5F);
    }

    @Override
    protected void onStart(Player player, Level level, BlockPos blockPos) {

    }
}
