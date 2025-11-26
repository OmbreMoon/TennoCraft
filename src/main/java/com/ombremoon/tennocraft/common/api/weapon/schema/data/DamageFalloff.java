package com.ombremoon.tennocraft.common.api.weapon.schema.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record DamageFalloff(float minRange, float maxRange, float maxFalloff) {
    public static final Codec<DamageFalloff> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.FLOAT.fieldOf("minRange").forGetter(DamageFalloff::minRange),
                    Codec.FLOAT.fieldOf("maxRange").forGetter(DamageFalloff::maxRange),
                    Codec.FLOAT.fieldOf("maxFalloff").forGetter(DamageFalloff::maxFalloff)
            ).apply(instance, DamageFalloff::new)
    );
    public static final StreamCodec<FriendlyByteBuf, DamageFalloff> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, DamageFalloff::minRange,
            ByteBufCodecs.FLOAT, DamageFalloff::maxRange,
            ByteBufCodecs.FLOAT, DamageFalloff::maxFalloff,
            DamageFalloff::new
    );

    public float calculate(float distance) {
        if (distance <= this.minRange) {
            return 1.0F;
        } else {
            float falloffRange = this.maxRange - this.minRange;
            float falloffDistance = falloffRange - (distance - this.minRange);
            return Math.max(1.0F - falloffDistance / falloffRange, maxFalloff);
        }
    }
}
