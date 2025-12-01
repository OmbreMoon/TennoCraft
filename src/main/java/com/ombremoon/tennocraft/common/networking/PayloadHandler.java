package com.ombremoon.tennocraft.common.networking;

import com.ombremoon.tennocraft.common.networking.clientbound.ClientReloadPayload;
import com.ombremoon.tennocraft.common.networking.clientbound.UpdateBulletHandlerPayload;
import com.ombremoon.tennocraft.common.networking.serverbound.*;
import com.ombremoon.tennocraft.main.Constants;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class PayloadHandler {

    //SERVER
    public static void shootProjectile(boolean useFallback, boolean updateReloadTime) {
        PacketDistributor.sendToServer(new ShootProjectilePayload(useFallback, updateReloadTime));
    }

    public static void reload() {
        PacketDistributor.sendToServer(new ReloadPayload());
    }

    public static void stopShooting() {
        PacketDistributor.sendToServer(new StopShootingPayload());
    }

    public static void activateDeployables() {
        PacketDistributor.sendToServer(new ActivateDeployablePayload());
    }

    public static void setChargingOrChanneling(boolean charging) {
        PacketDistributor.sendToServer(new ChargeOrChannelPayload(charging));
    }

    public static void cycleAlternateFire() {
        PacketDistributor.sendToServer(new CycleFirePayload());
    }

    //CLIENT
    public static void updateBulletHandler(ServerPlayer player, boolean updateFireRate) {
        PacketDistributor.sendToPlayer(player, new UpdateBulletHandlerPayload(updateFireRate));
    }

    public static void clientReload(ServerPlayer player, int reloadTick) {
        PacketDistributor.sendToPlayer(player, new ClientReloadPayload(reloadTick));
    }

    public static <PACKET extends CustomPacketPayload> void sendToAll(MinecraftServer server, PACKET packet) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (player.connection.hasChannel(packet)) {
                PacketDistributor.sendToPlayer(player, packet);
            }
        }
    }

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(Constants.MOD_ID).optional();
        //SERVER
        registrar.playToServer(
                ShootProjectilePayload.TYPE,
                ShootProjectilePayload.STREAM_CODEC,
                ShootProjectilePayload::handle
        );
        registrar.playToServer(
                ReloadPayload.TYPE,
                ReloadPayload.STREAM_CODEC,
                ReloadPayload::handle
        );
        registrar.playToServer(
                ChargeOrChannelPayload.TYPE,
                ChargeOrChannelPayload.STREAM_CODEC,
                ChargeOrChannelPayload::handle
        );
        registrar.playToServer(
                ActivateDeployablePayload.TYPE,
                ActivateDeployablePayload.STREAM_CODEC,
                ActivateDeployablePayload::handle
        );
        registrar.playToServer(
                CycleFirePayload.TYPE,
                CycleFirePayload.STREAM_CODEC,
                CycleFirePayload::handle
        );

        //CLIENT
        registrar.playToClient(
                UpdateBulletHandlerPayload.TYPE,
                UpdateBulletHandlerPayload.STREAM_CODEC,
                UpdateBulletHandlerPayload::handle
        );
        registrar.playToClient(
                ClientReloadPayload.TYPE,
                ClientReloadPayload.STREAM_CODEC,
                ClientReloadPayload::handle
        );

        //BIDIRECTIONAL
        registrar.playBidirectional(
                StopShootingPayload.TYPE,
                StopShootingPayload.STREAM_CODEC,
                StopShootingPayload::handle
        );
    }
}
