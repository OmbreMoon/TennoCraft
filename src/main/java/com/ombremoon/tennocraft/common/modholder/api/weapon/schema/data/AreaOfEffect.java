package com.ombremoon.tennocraft.common.modholder.api.weapon.schema.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.init.TCProjectileSerializers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record AreaOfEffect(float range, DamageFalloff falloff) implements ProjectileType<AreaOfEffect> {

    @Override
    public ProjectileSerializer<AreaOfEffect> getSerializer() {
        return TCProjectileSerializers.AOE.get();
    }

    public static class Serializer implements ProjectileSerializer<AreaOfEffect> {
        public static final MapCodec<AreaOfEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        Codec.FLOAT.fieldOf("range").forGetter(AreaOfEffect::range),
                        DamageFalloff.CODEC.fieldOf("falloff").forGetter(AreaOfEffect::falloff)
                ).apply(instance, AreaOfEffect::new)
        );
        public static final StreamCodec<FriendlyByteBuf, AreaOfEffect> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.FLOAT, AreaOfEffect::range,
                DamageFalloff.STREAM_CODEC, AreaOfEffect::falloff,
                AreaOfEffect::new
        );

        @Override
        public MapCodec<AreaOfEffect> codec() {
            return MAP_CODEC;
        }

        @Override
        public StreamCodec<FriendlyByteBuf, AreaOfEffect> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
