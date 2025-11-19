package com.ombremoon.tennocraft.common.api.mod.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.ombremoon.tennocraft.common.api.mod.effects.value.AddValue;
import com.ombremoon.tennocraft.common.api.mod.effects.value.MultiplyValue;
import com.ombremoon.tennocraft.main.CommonClass;
import com.ombremoon.tennocraft.main.Constants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Function;
import java.util.function.Supplier;

public interface ModValueEffect {
    ResourceKey<Registry<MapCodec<? extends ModValueEffect>>> RESOURCE_KEY = ResourceKey.createRegistryKey(CommonClass.customLocation("mod_value_effect_types"));
    Registry<MapCodec<? extends ModValueEffect>> REGISTRY = new RegistryBuilder<>(RESOURCE_KEY).sync(true).create();
    DeferredRegister<MapCodec<? extends ModValueEffect>> MOD_VALUE_EFFECT_TYPES = DeferredRegister.create(REGISTRY, Constants.MOD_ID);
    Codec<ModValueEffect> CODEC = REGISTRY
            .byNameCodec()
            .dispatch(ModValueEffect::codec, Function.identity());

    static Supplier<MapCodec<? extends ModValueEffect>> bootstrap(DeferredRegister<MapCodec<? extends ModValueEffect>> registry) {
        registry.register("add", () -> AddValue.CODEC);
        return registry.register("multiply", () -> MultiplyValue.CODEC);
    }

    float process(int modRank, RandomSource random, float value);

    MapCodec<? extends ModValueEffect> codec();
}
