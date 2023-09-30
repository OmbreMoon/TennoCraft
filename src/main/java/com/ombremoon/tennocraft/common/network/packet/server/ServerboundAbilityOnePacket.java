package com.ombremoon.tennocraft.common.network.packet.server;

import com.ombremoon.tennocraft.common.network.packet.IAbstractMessage;
import com.ombremoon.tennocraft.common.network.packet.client.ClientboundMovementPacket;
import com.ombremoon.tennocraft.object.item.mineframe.FrameArmorItem;
import com.ombremoon.tennocraft.player.ability.AbilityType;
import com.ombremoon.tennocraft.util.FrameUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;
import java.util.stream.StreamSupport;

public class ServerboundAbilityOnePacket implements IAbstractMessage {
    public ServerboundAbilityOnePacket() {

    }

    public ServerboundAbilityOnePacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null)
                return;

            //Check to see if full Frame is on
            if (FrameUtil.hasOnFrame(player)) {
                Iterable<ItemStack> armorSlots = player.getArmorSlots();
                FrameArmorItem.FrameType frameType = ((FrameArmorItem<?>) StreamSupport.stream(armorSlots.spliterator(), false).toList().get(0).getItem()).getFrameType();
                FrameArmorItem<?> frameArmorItem = FrameUtil.getFrameFromType(frameType);

                //Gets 1st ability from ability list
                AbilityType<?> frameAbility = FrameUtil.getFirstAbility(frameArmorItem);
                if (frameArmorItem.getFrameEnergy() > frameAbility.create().getEnergyRequired()) {
                    FrameUtil.initFrameAbility(player, player.level(), player.blockPosition(), frameAbility);
                }
            }
        });
        return true;
    }
}
