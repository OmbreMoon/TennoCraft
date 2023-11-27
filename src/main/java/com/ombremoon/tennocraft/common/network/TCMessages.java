package com.ombremoon.tennocraft.common.network;

import com.ombremoon.tennocraft.TennoCraft;
import com.ombremoon.tennocraft.common.network.packet.client.ClientboundMovementPacket;
import com.ombremoon.tennocraft.common.network.packet.client.ClientboundTransferencePacket;
import com.ombremoon.tennocraft.common.network.packet.server.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class TCMessages {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(TennoCraft.customLocation("messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(ServerboundOpenArsenalPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ServerboundOpenArsenalPacket::new)
                .encoder(ServerboundOpenArsenalPacket::toBytes)
                .consumerMainThread(ServerboundOpenArsenalPacket::handle)
                .add();

        net.messageBuilder(ServerboundTransferencePacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ServerboundTransferencePacket::new)
                .encoder(ServerboundTransferencePacket::toBytes)
                .consumerMainThread(ServerboundTransferencePacket::handle)
                .add();

        net.messageBuilder(ServerboundAbilityOnePacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ServerboundAbilityOnePacket::new)
                .encoder(ServerboundAbilityOnePacket::toBytes)
                .consumerMainThread(ServerboundAbilityOnePacket::handle)
                .add();

        net.messageBuilder(ServerboundAbilityTwoPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ServerboundAbilityTwoPacket::new)
                .encoder(ServerboundAbilityTwoPacket::toBytes)
                .consumerMainThread(ServerboundAbilityTwoPacket::handle)
                .add();

        net.messageBuilder(ServerboundAbilityThreePacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ServerboundAbilityThreePacket::new)
                .encoder(ServerboundAbilityThreePacket::toBytes)
                .consumerMainThread(ServerboundAbilityThreePacket::handle)
                .add();

        net.messageBuilder(ServerboundAbilityFourPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ServerboundAbilityFourPacket::new)
                .encoder(ServerboundAbilityFourPacket::toBytes)
                .consumerMainThread(ServerboundAbilityFourPacket::handle)
                .add();

        net.messageBuilder(ServerboundShootPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ServerboundShootPacket::new)
                .encoder(ServerboundShootPacket::toBytes)
                .consumerMainThread(ServerboundShootPacket::handle)
                .add();

        net.messageBuilder(ClientboundMovementPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientboundMovementPacket::new)
                .encoder(ClientboundMovementPacket::toBytes)
                .consumerMainThread(ClientboundMovementPacket::handle)
                .add();

        net.messageBuilder(ClientboundTransferencePacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientboundTransferencePacket::new)
                .encoder(ClientboundTransferencePacket::toBytes)
                .consumerMainThread(ClientboundTransferencePacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToClients(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
}
