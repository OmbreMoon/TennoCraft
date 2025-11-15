package com.ombremoon.tennocraft.common.modholder.api.mod.effects.value;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.modholder.api.mod.RankBasedValue;
import com.ombremoon.tennocraft.common.modholder.api.mod.effects.ModValueEffect;
import net.minecraft.util.RandomSource;

public record MultiplyValue(RankBasedValue factor) implements ModValueEffect {
    public static final MapCodec<MultiplyValue> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    RankBasedValue.CODEC.fieldOf("factor").forGetter(MultiplyValue::factor)
            ).apply(instance, MultiplyValue::new)
    );

    @Override
    public float process(int modRank, RandomSource random, float value) {
        return value * this.factor.calculate(modRank);
    }

    @Override
    public MapCodec<? extends ModValueEffect> codec() {
        return CODEC;
    }
}
