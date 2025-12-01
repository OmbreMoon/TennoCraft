package com.ombremoon.tennocraft.common.api.weapon.ranged.projectile;

import com.mojang.serialization.Codec;
import com.ombremoon.tennocraft.common.api.weapon.ranged.Bullet;
import com.ombremoon.tennocraft.common.api.weapon.schema.data.DamageFalloff;
import com.ombremoon.tennocraft.common.init.TCProjectileSerializers;
import com.ombremoon.tennocraft.main.Constants;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.slf4j.Logger;

import java.util.Optional;

public interface ProjectileType<T extends ProjectileType<T>> {
    Logger LOGGER = Constants.LOG;
    Codec<ProjectileType<?>> CODEC = TCProjectileSerializers.CODEC.dispatchStable(ProjectileType::getSerializer, ProjectileSerializer::codec);
    StreamCodec<RegistryFriendlyByteBuf, ProjectileType<?>> STREAM_CODEC = TCProjectileSerializers.STREAM_CODEC.dispatch(ProjectileType::getSerializer, ProjectileSerializer::streamCodec);

    ProjectileSerializer<T> getSerializer();

    Holder<Bullet> bullet();

    Optional<DamageFalloff> getFalloff();

    default void save(CompoundTag compoundTag) {
        CODEC.encodeStart(NbtOps.INSTANCE, this)
                .resultOrPartial(LOGGER::error)
                .ifPresent(tag -> compoundTag.put("Projectile Type", tag));
    }

    static Optional<ProjectileType<?>> parse(CompoundTag compoundTag) {
        return CODEC.parse(NbtOps.INSTANCE, compoundTag.get("Projectile Type"))
                .resultOrPartial(LOGGER::error);
    }
}
