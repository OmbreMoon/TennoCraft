package com.ombremoon.tennocraft.common.api.weapon.ranged.trigger;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public interface TriggerSerializer<T extends TriggerType<T>> {

    ResourceLocation id();

    MapCodec<T> codec();

    StreamCodec<RegistryFriendlyByteBuf, T> streamCodec();
}
