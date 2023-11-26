package com.ombremoon.tennocraft.object.custom.ability.frame.volt;

import com.ombremoon.tennocraft.common.init.custom.FrameAbilities;
import com.ombremoon.tennocraft.player.data.AbilityType;
import com.ombremoon.tennocraft.player.AbstractFrameAbility;

public class ShockAbility extends AbstractFrameAbility {

    public ShockAbility(AbilityType<?> abilityType, int energyRequired, float abilityRange) {
        super(abilityType, energyRequired, 0, 1, abilityRange);
    }

    public ShockAbility() {
        this(FrameAbilities.SHOCK_ABILITY.get(), 25, 7.5F);
    }

    @Override
    protected void onStart() {

        super.onStart();
    }
}
