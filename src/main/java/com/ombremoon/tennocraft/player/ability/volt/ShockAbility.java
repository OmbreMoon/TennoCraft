package com.ombremoon.tennocraft.player.ability.volt;

import com.ombremoon.tennocraft.common.init.custom.FrameAbilities;
import com.ombremoon.tennocraft.player.ability.AbilityType;
import com.ombremoon.tennocraft.player.ability.AbstractFrameAbility;

public class ShockAbility extends AbstractFrameAbility {

    public ShockAbility(AbilityType<?> abilityType, int energyRequired, float abilityRange) {
        super(abilityType, energyRequired, 0, 0, abilityRange);
    }

    public ShockAbility() {
        this(FrameAbilities.SHOCK_ABILITY.get(), 25, 7.5F);
    }

    @Override
    protected void onStart() {

        super.onStart();
    }
}
