package com.ombremoon.tennocraft.common.api.mod.effects.value;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.mod.RankBasedValue;
import com.ombremoon.tennocraft.common.api.mod.effects.ModValueEffect;
import net.minecraft.util.RandomSource;

public record AddValue(RankBasedValue value) implements ModValueEffect {
    public static final MapCodec<AddValue> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    RankBasedValue.CODEC.fieldOf("value").forGetter(AddValue::value)
            ).apply(instance, AddValue::new)
    );


    @Override
    public float process(int modRank, RandomSource random, float value) {
        return value + this.value.calculate(modRank);
    }

    @Override
    public MapCodec<? extends ModValueEffect> codec() {
        return CODEC;
    }
}
