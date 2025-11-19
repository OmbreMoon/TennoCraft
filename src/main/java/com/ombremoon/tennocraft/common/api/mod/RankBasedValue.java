package com.ombremoon.tennocraft.common.api.mod;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.main.CommonClass;
import com.ombremoon.tennocraft.main.Constants;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public interface RankBasedValue {
    ResourceKey<Registry<MapCodec<? extends RankBasedValue>>> RESOURCE_KEY = ResourceKey.createRegistryKey(CommonClass.customLocation("rank_based_value_types"));
    Registry<MapCodec<? extends RankBasedValue>> REGISTRY = new RegistryBuilder<>(RESOURCE_KEY).sync(true).create();
    DeferredRegister<MapCodec<? extends RankBasedValue>> RANK_BASED_VALUE_TYPES = DeferredRegister.create(REGISTRY, Constants.MOD_ID);
    Codec<RankBasedValue> DISPATCH_CODEC = REGISTRY
            .byNameCodec()
            .dispatch(RankBasedValue::codec, mapCodec -> mapCodec);
    Codec<RankBasedValue> CODEC = Codec.either(Constant.CODEC, DISPATCH_CODEC)
            .xmap(
                    either -> either.map(constant -> constant, rankBasedValue -> rankBasedValue),
                    value -> value instanceof Constant constant
                            ? Either.left(constant)
                            : Either.right(value)
            );

    static Supplier<MapCodec<? extends RankBasedValue>> bootstrap(DeferredRegister<MapCodec<? extends RankBasedValue>> registry) {
        registry.register("clamped", () -> Clamped.CODEC);
        return registry.register("linear", () -> Linear.CODEC);
    }

    static Linear perLevel(float base, float perLevel) {
        return new Linear(base, perLevel);
    }

    static Linear perLevel(float perLevel) {
        return perLevel(perLevel, perLevel);
    }

    float calculate(int rank);

    MapCodec<? extends RankBasedValue> codec();

    record Constant(float value) implements RankBasedValue {
        public static final Codec<Constant> CODEC = Codec.FLOAT.xmap(Constant::new, Constant::value);
        public static final MapCodec<Constant> TYPED_CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        Codec.FLOAT.fieldOf("value").forGetter(Constant::value)
                ).apply(instance, Constant::new)
        );

        @Override
        public float calculate(int rank) {
            return this.value;
        }

        @Override
        public MapCodec<? extends RankBasedValue> codec() {
            return TYPED_CODEC;
        }
    }

    record Clamped(RankBasedValue value, float min, float max) implements RankBasedValue {
        public static final MapCodec<Clamped> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        RankBasedValue.CODEC.fieldOf("value").forGetter(Clamped::value),
                        Codec.FLOAT.fieldOf("min").forGetter(Clamped::min),
                        Codec.FLOAT.fieldOf("max").forGetter(Clamped::max)
                ).apply(instance, Clamped::new)
        );


        @Override
        public float calculate(int rank) {
            return Mth.clamp(this.value.calculate(rank), this.min, this.max);
        }

        @Override
        public MapCodec<? extends RankBasedValue> codec() {
            return CODEC;
        }
    }

    record Linear(float base, float perRank) implements RankBasedValue {
        public static final MapCodec<Linear> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                                Codec.FLOAT.fieldOf("base").forGetter(Linear::base),
                                Codec.FLOAT.fieldOf("per_rank").forGetter(Linear::perRank)
                        )
                        .apply(instance, Linear::new)
        );

        @Override
        public float calculate(int rank) {
            return this.base + this.perRank * rank;
        }

        @Override
        public MapCodec<? extends RankBasedValue> codec() {
            return CODEC;
        }
    }
}
