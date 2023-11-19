package com.ombremoon.tennocraft.common.network.packet.server;

import com.ombremoon.tennocraft.common.network.packet.IAbstractMessage;
import com.ombremoon.tennocraft.object.item.mineframe.FrameArmorItem;
import com.ombremoon.tennocraft.object.item.mineframe.TransferenceTokenItem;
import com.ombremoon.tennocraft.object.item.mineframe.helmet.FrameHelmetItem;
import com.ombremoon.tennocraft.player.ability.AbilityType;
import com.ombremoon.tennocraft.player.ability.AbstractFrameAbility;
import com.ombremoon.tennocraft.util.FrameUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
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
                ItemStack frameStack = FrameUtil.getFrameStack(player);
                TransferenceTokenItem tokenItem = (TransferenceTokenItem) frameStack.getItem();

                //Gets 1st ability from ability list
                AbilityType<?> frameAbility = FrameUtil.getFirstAbility(tokenItem);
                if (hasEnoughEnergy(frameStack, tokenItem.getFrameType(), frameAbility.getSupplier())) {
                    FrameUtil.initFrameAbility(player, player.level(), player.blockPosition(), frameAbility);
                }
            }
        });
        return true;
    }

    private boolean hasEnoughEnergy(ItemStack itemStack, TransferenceTokenItem.FrameType frameType, AbstractFrameAbility abstractFrameAbility) {
        return frameType.getFrameEnergy() * (1 + FrameUtil.getFrameEnergy(itemStack)) > abstractFrameAbility.getEnergyRequired();
    }
}
