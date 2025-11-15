package com.ombremoon.tennocraft.common.modholder.api.weapon.schema.data;

import com.mojang.serialization.Codec;
import com.ombremoon.tennocraft.common.init.TCProjectileSerializers;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public interface ProjectileType<T extends ProjectileType<T>> {
    Codec<ProjectileType<?>> CODEC = TCProjectileSerializers.REGISTRY
            .byNameCodec()
            .dispatch(ProjectileType::getSerializer, ProjectileSerializer::codec);

    StreamCodec<RegistryFriendlyByteBuf, ProjectileType<?>> STREAM_CODEC = ByteBufCodecs.registry(TCProjectileSerializers.RESOURCE_KEY)
            .dispatch(ProjectileType::getSerializer, ProjectileSerializer::streamCodec);

    ProjectileSerializer<T> getSerializer();
}
