package com.ombremoon.tennocraft.common.api.weapon.schema.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record BurstData(int count, float delay) {
    public static final Codec<BurstData> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.INT.fieldOf("count").forGetter(BurstData::count),
                    Codec.FLOAT.fieldOf("delay").forGetter(BurstData::delay)
            ).apply(instance, BurstData::new)
    );
    public static final StreamCodec<FriendlyByteBuf, BurstData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, BurstData::count,
            ByteBufCodecs.FLOAT, BurstData::delay,
            BurstData::new
    );
}
