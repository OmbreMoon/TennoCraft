package com.ombremoon.tennocraft.common.networking.serverbound;

import com.ombremoon.tennocraft.common.api.IRangedModHolder;
import com.ombremoon.tennocraft.common.api.handler.RangedWeaponHandler;
import com.ombremoon.tennocraft.common.init.TCData;
import com.ombremoon.tennocraft.main.CommonClass;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record StopShootingPayload() implements CustomPacketPayload {
    public static final Type<StopShootingPayload> TYPE = new Type<>(CommonClass.customLocation("stop_shooting"));
    public static final StreamCodec<RegistryFriendlyByteBuf, StopShootingPayload> STREAM_CODEC = StreamCodec.unit(new StopShootingPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final StopShootingPayload payload, final IPayloadContext context) {
        Player player = context.player();
        ItemStack stack = player.getMainHandItem();
        RangedWeaponHandler handler = stack.get(TCData.RANGED_WEAPON_HANDLER);
        if (stack.getItem() instanceof IRangedModHolder && handler != null) {
            handler.stopShooting();
        }
    }
}
