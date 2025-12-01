package com.ombremoon.tennocraft.common.networking.serverbound;

import com.ombremoon.tennocraft.common.api.IRangedModHolder;
import com.ombremoon.tennocraft.common.init.TCData;
import com.ombremoon.tennocraft.common.world.entity.DeployableProjectile;
import com.ombremoon.tennocraft.main.CommonClass;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;

public record ActivateDeployablePayload() implements CustomPacketPayload {
    public static final Type<ActivateDeployablePayload> TYPE = new Type<>(CommonClass.customLocation("activate_deployable"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ActivateDeployablePayload> STREAM_CODEC = StreamCodec.unit(new ActivateDeployablePayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ActivateDeployablePayload payload, final IPayloadContext context) {
        Player player = context.player();
        Level level = player.level();
        ItemStack stack = player.getMainHandItem();
        if (stack.getItem() instanceof IRangedModHolder) {
            List<Integer> deployables = stack.getOrDefault(TCData.ACTIVE_DEPLOYABLES, new ArrayList<>());
            for (int id : deployables) {
                Entity entity = level.getEntity(id);
                if (entity instanceof DeployableProjectile deployable) {
                    deployables.remove(deployable.getId());
                    //Activate deployable
                }
            }
        }
    }
}
