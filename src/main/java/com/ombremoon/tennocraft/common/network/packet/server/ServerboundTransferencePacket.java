package com.ombremoon.tennocraft.common.network.packet.server;

import com.ombremoon.tennocraft.common.network.TCMessages;
import com.ombremoon.tennocraft.common.network.packet.IAbstractMessage;
import com.ombremoon.tennocraft.common.network.packet.client.ClientboundTransferencePacket;
import com.ombremoon.tennocraft.object.item.TransferenceKeyItem;
import com.ombremoon.tennocraft.util.FrameUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundTransferencePacket implements IAbstractMessage {
    public ServerboundTransferencePacket() {

    }

    public ServerboundTransferencePacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer serverPlayer = context.getSender();
            if (serverPlayer == null)
                return;

            if (FrameUtil.getFrameStack(serverPlayer).getItem() instanceof TransferenceKeyItem) {
                if (FrameUtil.hasOnFrame(serverPlayer)) {
                    serverPlayer.getTags().remove(FrameUtil.TRANSFERENCE);
                    TCMessages.sendToPlayer(new ClientboundTransferencePacket(false), serverPlayer);
                } else {
                    serverPlayer.getTags().add(FrameUtil.TRANSFERENCE);
                    TCMessages.sendToPlayer(new ClientboundTransferencePacket(true), serverPlayer);
                }
            }
        });
        return true;
    }
}
