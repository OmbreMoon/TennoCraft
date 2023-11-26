package com.ombremoon.tennocraft.common.network.packet.client;

import com.ombremoon.tennocraft.common.network.packet.IAbstractMessage;
import com.ombremoon.tennocraft.common.network.packet.client.data.TransferenceSyncData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundTransferencePacket implements IAbstractMessage {
    private final boolean hasOnFrame;

    public ClientboundTransferencePacket(boolean hasOnFrame) {
        this.hasOnFrame = hasOnFrame;
    }

    public ClientboundTransferencePacket(FriendlyByteBuf buf) {
        this.hasOnFrame = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(hasOnFrame);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            TransferenceSyncData.setHasOnFrame(hasOnFrame);
        });
        return true;
    }
}
