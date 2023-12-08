package com.ombremoon.tennocraft.common.network.packet.server;

import com.ombremoon.tennocraft.common.network.packet.IAbstractMessage;
import com.ombremoon.tennocraft.common.network.weapon.WeaponHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundShootPacket implements IAbstractMessage {
    private float yawAmount;
    private float pitchAmount;

    public ServerboundShootPacket(Player player) {
        yawAmount = player.getXRot();
        pitchAmount = player.getYHeadRot();
    }

    public ServerboundShootPacket(FriendlyByteBuf friendlyByteBuf) {
        yawAmount = friendlyByteBuf.readFloat();
        pitchAmount = friendlyByteBuf.readFloat();
    }

    @Override
    public void toBytes( FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeFloat(yawAmount);
        friendlyByteBuf.writeFloat(pitchAmount);
    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null)
                return;

            player.setYRot(pitchAmount);
            player.setXRot(yawAmount);
            WeaponHandler.createBullet(player);

        });
        return true;
    }
}
