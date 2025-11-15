package com.ombremoon.tennocraft.common.modholder.api.weapon.schema.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.init.TCProjectileSerializers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Optional;

public record Projectile(boolean hasGravity, Optional<Float> gravity, Optional<Integer> range, Optional<DamageFalloff> falloff) implements ProjectileType<Projectile> {

    @Override
    public ProjectileSerializer<Projectile> getSerializer() {
        return TCProjectileSerializers.PROJECTILE.get();
    }

    public static class Serializer implements ProjectileSerializer<Projectile> {
        public static final MapCodec<Projectile> MAP_CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        Codec.BOOL.fieldOf("hasGravity").forGetter(projectile -> projectile.hasGravity),
                        Codec.FLOAT.optionalFieldOf("gravity").forGetter(projectile -> projectile.gravity),
                        Codec.INT.optionalFieldOf("range").forGetter(projectile -> projectile.range),
                        DamageFalloff.CODEC.optionalFieldOf("falloff").forGetter(projectile -> projectile.falloff)
                ).apply(instance, Projectile::new)
        );
        public static final StreamCodec<FriendlyByteBuf, Projectile> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.BOOL, Projectile::hasGravity,
                ByteBufCodecs.optional(ByteBufCodecs.FLOAT), Projectile::gravity,
                ByteBufCodecs.optional(ByteBufCodecs.INT), Projectile::range,
                ByteBufCodecs.optional(DamageFalloff.STREAM_CODEC), Projectile::falloff,
                Projectile::new
        );

        @Override
        public MapCodec<Projectile> codec() {
            return MAP_CODEC;
        }

        @Override
        public StreamCodec<FriendlyByteBuf, Projectile> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
