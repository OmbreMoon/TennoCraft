package com.ombremoon.tennocraft.player.ability.volt;

import com.ombremoon.tennocraft.common.init.custom.FrameAbilities;
import com.ombremoon.tennocraft.player.ability.AbilityType;
import com.ombremoon.tennocraft.player.ability.AbstractFrameAbility;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class SpeedAbility extends AbstractFrameAbility {
    public SpeedAbility(AbilityType<?> abilityType, int energyRequired) {
        super(abilityType, energyRequired, 0);
    }

    public SpeedAbility() {
        this(FrameAbilities.SPEED_ABILITY.get(), 25);
    }

    @Override
    protected void onTick() {
        Player player = level.getPlayerByUUID(userID);
        System.out.println(player);
        player.setDeltaMovement(5, 5, 5);
        if (ticks % 200 == 0) {
            endAbility();
        }
        super.onTick();
    }
}
