package com.ombremoon.tennocraft.common.networking.clientbound;

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

public record UpdateBulletHandlerPayload(boolean updateFireRate) implements CustomPacketPayload {
    public static final Type<UpdateBulletHandlerPayload> TYPE = new Type<>(CommonClass.customLocation("update_bullet_handler"));
    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateBulletHandlerPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, UpdateBulletHandlerPayload::updateFireRate,
            UpdateBulletHandlerPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final UpdateBulletHandlerPayload payload, final IPayloadContext context) {
        Player player = context.player();
        ItemStack stack = player.getMainHandItem();
        RangedWeaponHandler handler = stack.get(TCData.RANGED_WEAPON_HANDLER);
        if (stack.getItem() instanceof IRangedModHolder && handler != null) {
            handler.isFiring = true;
            handler.shotCounter++;

            //Exclusive for duplex trigger to not rely on fire rate for first shot
            if (payload.updateFireRate)
                handler.lastShotTick = player.tickCount;
        }
    }
}
