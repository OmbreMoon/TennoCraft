package com.ombremoon.tennocraft.common.network.packet.server;

import com.ombremoon.tennocraft.common.network.packet.IAbstractMessage;
import com.ombremoon.tennocraft.object.world.inventory.PlayerArsenalMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;

import java.util.function.Supplier;

public class ServerboundOpenArsenalPacket implements IAbstractMessage {
    public ServerboundOpenArsenalPacket() {

    }

    public ServerboundOpenArsenalPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer serverPlayer = context.getSender();
            if (serverPlayer == null)
                return;

            NetworkHooks.openScreen(serverPlayer, new SimpleMenuProvider((screenId, inventory, player) -> new PlayerArsenalMenu(screenId, inventory), Component.translatable("")));
        });
        return true;
    }
}
