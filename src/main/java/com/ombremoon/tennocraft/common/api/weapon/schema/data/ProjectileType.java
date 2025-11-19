package com.ombremoon.tennocraft.common.api.weapon.schema.data;

import com.mojang.serialization.MapCodec;
import com.ombremoon.tennocraft.common.init.TCProjectileSerializers;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public interface ProjectileType<T extends ProjectileType<T>> {
    MapCodec<ProjectileType<?>> CODEC = TCProjectileSerializers.REGISTRY
            .byNameCodec()
            .dispatchMap(ProjectileType::getSerializer, ProjectileSerializer::codec);

    StreamCodec<RegistryFriendlyByteBuf, ProjectileType<?>> STREAM_CODEC = ByteBufCodecs.registry(TCProjectileSerializers.RESOURCE_KEY)
            .dispatch(ProjectileType::getSerializer, ProjectileSerializer::streamCodec);

    ProjectileSerializer<T> getSerializer();
}
