package com.ombremoon.tennocraft.player.ability.volt;

import com.ombremoon.tennocraft.common.init.custom.FrameAbilities;
import com.ombremoon.tennocraft.common.network.TCMessages;
import com.ombremoon.tennocraft.common.network.packet.client.ClientboundMovementPacket;
import com.ombremoon.tennocraft.player.ability.AbilityType;
import com.ombremoon.tennocraft.player.ability.AbstractFrameAbility;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class SpeedAbility extends AbstractFrameAbility {
    private int temp;

    public SpeedAbility(AbilityType<?> abilityType, int energyRequired) {
        super(abilityType, energyRequired, 0);
    }

    public SpeedAbility() {
        this(FrameAbilities.SPEED_ABILITY.get(), 25);
    }

    @Override
    protected void onStart() {
        Player player = level.getPlayerByUUID(userID);
        if (player != null) {
            addModifier(player, Attributes.MOVEMENT_SPEED, UUID.fromString("61073932-801c-4107-940c-18cbe661a042"), "movement_speed", 0.7F, AttributeModifier.Operation.MULTIPLY_TOTAL);
        }
        super.onStart();
    }

    @Override
    protected void onTick() {
        Player player = level.getPlayerByUUID(userID);

        if (ticks % 200 == 0) {
            endAbility();
        }
        super.onTick();
    }

    @Override
    protected void onStop() {
        Player player = level.getPlayerByUUID(userID);
        if (player != null) {
            removeModifier(player, Attributes.MOVEMENT_SPEED, UUID.fromString("61073932-801c-4107-940c-18cbe661a042"));
        }
        super.onStop();
    }

    @Override
    public int getDurationInTicks() {
        return 200;
    }
}
