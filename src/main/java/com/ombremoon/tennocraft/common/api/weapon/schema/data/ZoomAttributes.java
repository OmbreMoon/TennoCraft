package com.ombremoon.tennocraft.common.api.weapon.schema.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public record ZoomAttributes(Optional<Float> comboDecay, int minCombo, Map<Float, DataComponentType<?>> zoomBuffs) {
    public static final Codec<ZoomAttributes> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.FLOAT.optionalFieldOf("comboDecay").forGetter(ZoomAttributes::comboDecay),
                    Codec.INT.fieldOf("minCombo").forGetter(ZoomAttributes::minCombo),
                    Codec.unboundedMap(Codec.FLOAT, DataComponentType.CODEC).fieldOf("zoomBuffs").forGetter(ZoomAttributes::zoomBuffs)
            ).apply(instance, ZoomAttributes::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ZoomAttributes> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.optional(ByteBufCodecs.FLOAT), ZoomAttributes::comboDecay,
            ByteBufCodecs.INT, ZoomAttributes::minCombo,
            ByteBufCodecs.map(HashMap::new, ByteBufCodecs.FLOAT, DataComponentType.STREAM_CODEC), ZoomAttributes::zoomBuffs,
            ZoomAttributes::new
    );
}
