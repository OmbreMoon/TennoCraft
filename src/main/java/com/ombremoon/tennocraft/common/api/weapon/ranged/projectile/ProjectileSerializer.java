package com.ombremoon.tennocraft.common.api.weapon.ranged.projectile;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public interface ProjectileSerializer<T extends ProjectileType<T>> {

    ResourceLocation id();

    MapCodec<T> codec();

    StreamCodec<RegistryFriendlyByteBuf, T> streamCodec();
}
