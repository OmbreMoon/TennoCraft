package com.ombremoon.tennocraft.common.api.weapon.schema.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.init.TCProjectileSerializers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Optional;

public record Hitscan(int maxDistance, Optional<DamageFalloff> falloff) implements ProjectileType<Hitscan> {

    public Hitscan(int maxDistance) {
        this(maxDistance, Optional.empty());
    }

    @Override
    public ProjectileSerializer<Hitscan> getSerializer() {
        return TCProjectileSerializers.HITSCAN.get();
    }

    public static class Serializer implements ProjectileSerializer<Hitscan> {
        public static final MapCodec<Hitscan> MAP_CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        Codec.INT.fieldOf("maxDistance").forGetter(hitscan -> hitscan.maxDistance),
                        DamageFalloff.CODEC.optionalFieldOf("falloff").forGetter(hitscan -> hitscan.falloff)
                ).apply(instance, Hitscan::new)
        );
        public static final StreamCodec<FriendlyByteBuf, Hitscan> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, Hitscan::maxDistance,
                ByteBufCodecs.optional(DamageFalloff.STREAM_CODEC), Hitscan::falloff,
                Hitscan::new
        );

        @Override
        public MapCodec<Hitscan> codec() {
            return MAP_CODEC;
        }

        @Override
        public StreamCodec<FriendlyByteBuf, Hitscan> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
