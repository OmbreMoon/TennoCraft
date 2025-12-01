package com.ombremoon.tennocraft.common.networking.serverbound;

import com.ombremoon.tennocraft.common.api.IRangedModHolder;
import com.ombremoon.tennocraft.common.api.handler.RangedWeaponHandler;
import com.ombremoon.tennocraft.common.api.weapon.ranged.projectile.ProjectileType;
import com.ombremoon.tennocraft.common.api.weapon.ranged.projectile.SolidProjectile;
import com.ombremoon.tennocraft.common.api.weapon.ranged.reload.ReloadType;
import com.ombremoon.tennocraft.common.api.weapon.ranged.trigger.*;
import com.ombremoon.tennocraft.common.api.weapon.schema.RangedWeaponSchema;
import com.ombremoon.tennocraft.common.api.weapon.schema.data.RangedAttack;
import com.ombremoon.tennocraft.common.init.TCData;
import com.ombremoon.tennocraft.common.networking.PayloadHandler;
import com.ombremoon.tennocraft.common.world.entity.BulletProjectile;
import com.ombremoon.tennocraft.main.CommonClass;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.tslat.smartbrainlib.util.RandomUtil;

import java.util.ArrayList;
import java.util.List;

public record ReloadPayload() implements CustomPacketPayload {
    public static final Type<ReloadPayload> TYPE = new Type<>(CommonClass.customLocation("reload"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ReloadPayload> STREAM_CODEC = StreamCodec.unit(new ReloadPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ReloadPayload payload, final IPayloadContext context) {
        ServerPlayer player = (ServerPlayer) context.player();
        ItemStack stack = player.getMainHandItem();
        RangedWeaponHandler handler = stack.get(TCData.RANGED_WEAPON_HANDLER);
        if (stack.getItem() instanceof IRangedModHolder modHolder && handler != null) {
            handler.stopShooting();
            modHolder.reload(player, stack);
        }
    }
}
