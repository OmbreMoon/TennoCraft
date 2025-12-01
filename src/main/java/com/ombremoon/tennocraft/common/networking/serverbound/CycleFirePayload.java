package com.ombremoon.tennocraft.common.networking.serverbound;

import com.ombremoon.tennocraft.common.api.IRangedModHolder;
import com.ombremoon.tennocraft.main.CommonClass;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CycleFirePayload() implements CustomPacketPayload {
    public static final Type<CycleFirePayload> TYPE = new Type<>(CommonClass.customLocation("cycle_fire"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CycleFirePayload> STREAM_CODEC = StreamCodec.unit(new CycleFirePayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final CycleFirePayload payload, final IPayloadContext context) {
        Player player = context.player();
        ItemStack stack = player.getMainHandItem();
        if (stack.getItem() instanceof IRangedModHolder modHolder) {
            modHolder.cycleAlternateFire(player, stack);
        }
    }
}
