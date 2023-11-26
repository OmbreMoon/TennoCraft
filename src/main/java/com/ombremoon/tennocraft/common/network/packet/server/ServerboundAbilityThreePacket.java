package com.ombremoon.tennocraft.common.network.packet.server;

import com.ombremoon.tennocraft.common.network.packet.IAbstractMessage;
import com.ombremoon.tennocraft.object.item.TransferenceKeyItem;
import com.ombremoon.tennocraft.player.data.AbilityType;
import com.ombremoon.tennocraft.player.AbstractFrameAbility;
import com.ombremoon.tennocraft.util.FrameUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundAbilityThreePacket implements IAbstractMessage {
    public ServerboundAbilityThreePacket() {

    }

    public ServerboundAbilityThreePacket(FriendlyByteBuf buf) {

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
                TransferenceKeyItem tokenItem = (TransferenceKeyItem) frameStack.getItem();

                //Gets 3rd ability from ability list
                AbilityType<?> frameAbility = FrameUtil.getThirdAbility(tokenItem);
                if (hasEnoughEnergy(player, frameAbility.getSupplier())) {
                    FrameUtil.initFrameAbility(player, player.level(), player.blockPosition(), frameStack, frameAbility);
                }
            }
        });
        return true;
    }

    private boolean hasEnoughEnergy(Player player, AbstractFrameAbility abstractFrameAbility) {
        return FrameUtil.getFrameEnergy(player) > abstractFrameAbility.getEnergyRequired();
    }
}
