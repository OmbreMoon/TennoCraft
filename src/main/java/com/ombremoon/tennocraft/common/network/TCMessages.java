package com.ombremoon.tennocraft.common.network;

import com.ombremoon.tennocraft.TennoCraft;
import com.ombremoon.tennocraft.common.network.packet.client.ClientboundMovementPacket;
import com.ombremoon.tennocraft.common.network.packet.server.ServerboundAbilityFourPacket;
import com.ombremoon.tennocraft.common.network.packet.server.ServerboundAbilityOnePacket;
import com.ombremoon.tennocraft.common.network.packet.server.ServerboundAbilityThreePacket;
import com.ombremoon.tennocraft.common.network.packet.server.ServerboundAbilityTwoPacket;
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

        net.messageBuilder(ClientboundMovementPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientboundMovementPacket::new)
                .encoder(ClientboundMovementPacket::toBytes)
                .consumerMainThread(ClientboundMovementPacket::handle)
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
