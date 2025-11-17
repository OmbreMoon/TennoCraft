package com.ombremoon.tennocraft.common.api.weapon.schema.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record DamageFalloff(float minRange, float maxRange, float amount) {
    public static final Codec<DamageFalloff> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.FLOAT.fieldOf("minRange").forGetter(DamageFalloff::minRange),
                    Codec.FLOAT.fieldOf("maxRange").forGetter(DamageFalloff::maxRange),
                    Codec.FLOAT.fieldOf("amount").forGetter(DamageFalloff::amount)
            ).apply(instance, DamageFalloff::new)
    );
    public static final StreamCodec<FriendlyByteBuf, DamageFalloff> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, DamageFalloff::minRange,
            ByteBufCodecs.FLOAT, DamageFalloff::maxRange,
            ByteBufCodecs.FLOAT, DamageFalloff::amount,
            DamageFalloff::new
    );
}
