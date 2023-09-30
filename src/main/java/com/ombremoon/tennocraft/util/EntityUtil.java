package com.ombremoon.tennocraft.util;

import com.ombremoon.tennocraft.common.network.packet.client.ClientboundMovementPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class EntityUtil {

    public static void sendMovementPacket(float x, float y, float z, ClientboundMovementPacket.MovementOperation movementOperation) {
        Player player = Minecraft.getInstance().player;
        Vec3 vec3 = player.getDeltaMovement();

        switch (movementOperation) {
            case SET -> player.setDeltaMovement(x, y, z);
            case ADD -> player.setDeltaMovement(vec3.add(x, y, z));
            case MULTIPLY -> player.setDeltaMovement(vec3.multiply(x, y, z));
        }
    }
}
