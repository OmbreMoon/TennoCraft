package com.ombremoon.tennocraft.common.network.packet.server;

import com.ombremoon.tennocraft.common.network.packet.IAbstractMessage;
import com.ombremoon.tennocraft.common.network.packet.server.data.RotationSyncData;
import com.ombremoon.tennocraft.common.network.weapon.WeaponHandler;
import com.ombremoon.tennocraft.object.item.TransferenceKeyItem;
import com.ombremoon.tennocraft.player.data.AbilityType;
import com.ombremoon.tennocraft.util.FrameUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundShootPacket implements IAbstractMessage {
    private static ServerboundShootPacket packet;
    private float pitchAmount;
    private float yawAmount;

    public ServerboundShootPacket(float pitchAmount, float yawAmount) {
        this.pitchAmount = pitchAmount;
        this.yawAmount = yawAmount;
    }

    public ServerboundShootPacket(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeFloat(pitchAmount);
        friendlyByteBuf.writeFloat(yawAmount);
    }

    @Override
    public void toBytes( FriendlyByteBuf friendlyByteBuf) {
//        this.pitchAmount = friendlyByteBuf.readFloat();
//        this.yawAmount = friendlyByteBuf.readFloat();
    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null)
                return;

            RotationSyncData.setData(pitchAmount, yawAmount);
            WeaponHandler.createBullet(this, player);

        });
        return true;
    }

    public float getPitchAmount() {
        return this.pitchAmount;
    }

    public float getYawAmount() {
        return this.yawAmount;
    }
}
