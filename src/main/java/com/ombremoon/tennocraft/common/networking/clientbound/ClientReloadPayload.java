package com.ombremoon.tennocraft.common.networking.clientbound;

import com.ombremoon.tennocraft.common.api.IRangedModHolder;
import com.ombremoon.tennocraft.common.api.handler.RangedWeaponHandler;
import com.ombremoon.tennocraft.common.init.TCData;
import com.ombremoon.tennocraft.main.CommonClass;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ClientReloadPayload(int reloadTick) implements CustomPacketPayload {
    public static final Type<ClientReloadPayload> TYPE = new Type<>(CommonClass.customLocation("client_reload"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientReloadPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ClientReloadPayload::reloadTick,
            ClientReloadPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ClientReloadPayload payload, final IPayloadContext context) {
        Player player = context.player();
        ItemStack stack = player.getMainHandItem();
        RangedWeaponHandler handler = stack.get(TCData.RANGED_WEAPON_HANDLER);
        if (stack.getItem() instanceof IRangedModHolder && handler != null) {
            handler.reloadTick = payload.reloadTick();
        }
    }
}
