package com.ombremoon.tennocraft.common.network.packet.server;

import com.ombremoon.tennocraft.common.network.packet.IAbstractMessage;
import com.ombremoon.tennocraft.object.world.inventory.PlayerArsenalMenu;
import com.ombremoon.tennocraft.util.FrameUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;

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
            if (serverPlayer.getTags().contains(FrameUtil.TRANSFERENCE)) {
                serverPlayer.getTags().remove(FrameUtil.TRANSFERENCE);
            } else {
                serverPlayer.getTags().add(FrameUtil.TRANSFERENCE);
            }

        });
        return true;
    }
}
