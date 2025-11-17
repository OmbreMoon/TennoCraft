package com.ombremoon.tennocraft.common.api.mod;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record AuraEffect(ModTarget target, float range) {
    public static final Codec<AuraEffect> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    ModTarget.CODEC.fieldOf("target").forGetter(AuraEffect::target),
                    Codec.FLOAT.fieldOf("range").forGetter(AuraEffect::range)
            ).apply(instance, AuraEffect::new)
    );
}
