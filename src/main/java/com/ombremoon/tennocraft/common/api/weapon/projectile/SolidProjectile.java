package com.ombremoon.tennocraft.common.api.weapon.projectile;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.weapon.schema.data.DamageFalloff;
import com.ombremoon.tennocraft.common.init.TCProjectileSerializers;
import com.ombremoon.tennocraft.main.Keys;
import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public record SolidProjectile(
        float projectileSpeed,
        Vec3 dimensions,
        Optional<Integer> lifetime,
        Optional<Integer> maxProjectiles,
        Optional<Float> gravity,
        Optional<Float> range,
        Optional<DamageFalloff> falloff,
        Holder<Bullet> bullet
) implements ProjectileType<SolidProjectile> {

    public SolidProjectile(float projectileSpeed, Vec3 dimensions, Holder<Bullet> bullet) {
        this(projectileSpeed, dimensions, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), bullet);
    }

    @Override
    public ProjectileSerializer<SolidProjectile> getSerializer() {
        return TCProjectileSerializers.PROJECTILE.get();
    }

    @Override
    public Optional<DamageFalloff> getFalloff() {
        return this.falloff;
    }

    public static class Serializer implements ProjectileSerializer<SolidProjectile> {
        public static final MapCodec<SolidProjectile> MAP_CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        ExtraCodecs.POSITIVE_FLOAT.fieldOf("projectile_speed").forGetter(SolidProjectile::projectileSpeed),
                        Vec3.CODEC.fieldOf("dimensions").forGetter(SolidProjectile::dimensions),
                        Codec.INT.optionalFieldOf("lifetime").forGetter(SolidProjectile::lifetime),
                        ExtraCodecs.POSITIVE_INT.optionalFieldOf("max_projectiles").forGetter(SolidProjectile::maxProjectiles),
                        ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("gravity").forGetter(SolidProjectile::gravity),
                        ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("range").forGetter(SolidProjectile::range),
                        DamageFalloff.CODEC.optionalFieldOf("falloff").forGetter(SolidProjectile::falloff),
                        Bullet.CODEC.fieldOf("bullet").forGetter(SolidProjectile::bullet)
                ).apply(instance, SolidProjectile::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, SolidProjectile> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public SolidProjectile decode(RegistryFriendlyByteBuf buffer) {
                float projectileSpeed = buffer.readFloat();
                Vec3 dimensions = buffer.readVec3();
                Optional<Integer> lifetime = buffer.readOptional(FriendlyByteBuf::readVarInt);
                Optional<Integer> maxProjectiles = buffer.readOptional(FriendlyByteBuf::readVarInt);
                Optional<Float> gravity = buffer.readOptional(FriendlyByteBuf::readFloat);
                Optional<Float> range = buffer.readOptional(FriendlyByteBuf::readFloat);
                Optional<DamageFalloff> falloff = ByteBufCodecs.optional(DamageFalloff.STREAM_CODEC).decode(buffer);
                ResourceKey<Bullet> key = buffer.readResourceKey(Keys.BULLET);
                Holder<Bullet> bullet = buffer.registryAccess().holderOrThrow(key);
                return new SolidProjectile(projectileSpeed, dimensions, lifetime, maxProjectiles, gravity, range, falloff, bullet);
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buffer, SolidProjectile value) {
                buffer.writeFloat(value.projectileSpeed);
                buffer.writeVec3(value.dimensions);
                buffer.writeOptional(value.lifetime, FriendlyByteBuf::writeVarInt);
                buffer.writeOptional(value.maxProjectiles, FriendlyByteBuf::writeVarInt);
                buffer.writeOptional(value.gravity, FriendlyByteBuf::writeFloat);
                buffer.writeOptional(value.range, FriendlyByteBuf::writeFloat);
                ByteBufCodecs.optional(DamageFalloff.STREAM_CODEC).encode(buffer, value.falloff);
                ResourceKey<Bullet> key = value.bullet.unwrapKey().orElseThrow();
                buffer.writeResourceKey(key);
            }
        };

        @Override
        public MapCodec<SolidProjectile> codec() {
            return MAP_CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SolidProjectile> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
