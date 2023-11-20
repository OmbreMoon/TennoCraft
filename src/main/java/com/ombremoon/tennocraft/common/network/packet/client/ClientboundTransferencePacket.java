package com.ombremoon.tennocraft.common.network.packet.client;

import com.ombremoon.tennocraft.common.network.packet.IAbstractMessage;
import com.ombremoon.tennocraft.common.network.packet.client.data.TransferenceSyncData;
import com.ombremoon.tennocraft.object.item.mineframe.TransferenceKeyItem;
import com.ombremoon.tennocraft.player.ability.AbilityType;
import com.ombremoon.tennocraft.player.ability.AbstractFrameAbility;
import com.ombremoon.tennocraft.util.FrameUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
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
