package com.ombremoon.tennocraft.common.api.weapon.schema.data;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public interface ProjectileSerializer<T extends ProjectileType<T>> {

    MapCodec<T> codec();

    StreamCodec<FriendlyByteBuf, T> streamCodec();
}
