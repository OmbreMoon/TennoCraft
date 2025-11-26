package com.ombremoon.tennocraft.common.api.weapon.projectile;

import com.mojang.serialization.MapCodec;
import com.ombremoon.tennocraft.common.api.weapon.schema.data.DamageFalloff;
import com.ombremoon.tennocraft.common.init.TCProjectileSerializers;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;

import java.util.Optional;

public interface ProjectileType<T extends ProjectileType<T>> {
    MapCodec<ProjectileType<?>> CODEC = TCProjectileSerializers.REGISTRY
            .byNameCodec()
            .dispatchMap(ProjectileType::getSerializer, ProjectileSerializer::codec);

    StreamCodec<RegistryFriendlyByteBuf, ProjectileType<?>> STREAM_CODEC = ByteBufCodecs.registry(TCProjectileSerializers.RESOURCE_KEY)
            .dispatch(ProjectileType::getSerializer, ProjectileSerializer::streamCodec);

    ProjectileSerializer<T> getSerializer();

    Holder<Bullet> bullet();

    Optional<DamageFalloff> getFalloff();
}
