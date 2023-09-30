package com.ombremoon.tennocraft.common.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public interface IAbstractMessage {

    void toBytes(FriendlyByteBuf friendlyByteBuf);

    boolean handle(Supplier<NetworkEvent.Context> supplier);
}
