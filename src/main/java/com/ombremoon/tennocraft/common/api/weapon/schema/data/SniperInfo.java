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

public record SniperInfo(Optional<Float> comboDecay, int minCombo, Map<Float, DataComponentType<?>> zoomBuffs) {
    public static final Codec<SniperInfo> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.FLOAT.optionalFieldOf("comboDecay").forGetter(SniperInfo::comboDecay),
                    Codec.INT.fieldOf("minCombo").forGetter(SniperInfo::minCombo),
                    Codec.unboundedMap(Codec.FLOAT, DataComponentType.CODEC).fieldOf("zoomBuffs").forGetter(SniperInfo::zoomBuffs)
            ).apply(instance, SniperInfo::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, SniperInfo> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.optional(ByteBufCodecs.FLOAT), SniperInfo::comboDecay,
            ByteBufCodecs.INT, SniperInfo::minCombo,
            ByteBufCodecs.map(HashMap::new, ByteBufCodecs.FLOAT, DataComponentType.STREAM_CODEC), SniperInfo::zoomBuffs,
            SniperInfo::new
    );
}
