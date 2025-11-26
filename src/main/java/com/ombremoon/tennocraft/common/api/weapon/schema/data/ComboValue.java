package com.ombremoon.tennocraft.common.api.weapon.schema.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.weapon.DamageValue;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public record ComboValue(ResourceLocation animation, List<AttackMultiplier> multipliers) {
    public static final Codec<ComboValue> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    ResourceLocation.CODEC.fieldOf("animation").forGetter(ComboValue::animation),
                    AttackMultiplier.CODEC.listOf().fieldOf("multipliers").forGetter(ComboValue::multipliers)
            ).apply(instance, ComboValue::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ComboValue> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, ComboValue::animation,
            AttackMultiplier.STREAM_CODEC.apply(ByteBufCodecs.list()), ComboValue::multipliers,
            ComboValue::new
    );

    public static ComboValue combo(ResourceLocation animation, AttackMultiplier... multipliers) {
        return new ComboValue(animation, Arrays.asList(multipliers));
    }

    public static AttackMultiplier withMultiplier(float multiplier, float bonusStatus, DamageValue... values) {
        return new AttackMultiplier(multiplier, Optional.of(bonusStatus), Optional.of(Arrays.asList(values)), Optional.empty());
    }

    @SuppressWarnings("unchecked")
    public static AttackMultiplier withMultiplier(float multiplier, float bonusStatus, Holder<MobEffect>... mobEffects) {
        return new AttackMultiplier(multiplier, Optional.of(bonusStatus), Optional.empty(), Optional.of(Arrays.asList(mobEffects)));
    }

    public static AttackMultiplier withMultiplier(float multiplier, float bonusStatus) {
        return new AttackMultiplier(multiplier, Optional.of(bonusStatus), Optional.empty(), Optional.empty());
    }

    public static AttackMultiplier withMultiplier(float multiplier, DamageValue... values) {
        return new AttackMultiplier(multiplier, Optional.empty(), Optional.of(Arrays.asList(values)), Optional.empty());
    }

    @SuppressWarnings("unchecked")
    public static AttackMultiplier withMultiplier(float multiplier, Holder<MobEffect>... mobEffects) {
        return new AttackMultiplier(multiplier, Optional.empty(), Optional.empty(), Optional.of(Arrays.asList(mobEffects)));
    }

    public static AttackMultiplier withMultiplier(float multiplier) {
        return new AttackMultiplier(multiplier, Optional.empty(), Optional.empty(), Optional.empty());
    }
}
