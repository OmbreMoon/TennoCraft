package com.ombremoon.tennocraft.common.modholder.api.mod;

import com.mojang.serialization.Codec;
import com.ombremoon.tennocraft.common.modholder.api.mod.effects.ModAttributeEffect;
import com.ombremoon.tennocraft.main.CommonClass;
import com.ombremoon.tennocraft.main.Constants;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class TCModEffectComponents {
    public static final ResourceKey<Registry<DataComponentType<?>>> RESOURCE_KEY = ResourceKey.createRegistryKey(CommonClass.customLocation("mod_effect_component_type"));
    public static final Registry<DataComponentType<?>> REGISTRY = new RegistryBuilder<>(RESOURCE_KEY).sync(true).create();
    public static final DeferredRegister<DataComponentType<?>> MOD_EFFECT_COMPONENT_TYPES = DeferredRegister.createDataComponents(RESOURCE_KEY, Constants.MOD_ID);
    public static final Codec<DataComponentType<?>> COMPONENT_CODEC = Codec.lazyInitialized(REGISTRY::byNameCodec);
    public static Codec<DataComponentMap> CODEC = DataComponentMap.makeCodec(COMPONENT_CODEC);

    public static final Supplier<DataComponentType<List<ModAttributeEffect>>> ATTRIBUTES = register("attributes", builder -> builder.persistent(ModAttributeEffect.CODEC.codec().listOf()));

    private static <T> Supplier<DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> operator) {
        return MOD_EFFECT_COMPONENT_TYPES.register(name, () -> operator.apply(DataComponentType.builder()).build());
    }

    public static void register(IEventBus modEventBus) {
        MOD_EFFECT_COMPONENT_TYPES.register(modEventBus);
    }
}
