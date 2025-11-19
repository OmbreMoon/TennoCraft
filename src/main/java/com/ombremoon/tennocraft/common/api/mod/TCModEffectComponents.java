package com.ombremoon.tennocraft.common.api.mod;

import com.mojang.serialization.Codec;
import com.ombremoon.tennocraft.common.api.mod.effects.ModAttributeEffect;
import com.ombremoon.tennocraft.common.api.mod.effects.ModValueEffect;
import com.ombremoon.tennocraft.common.api.mod.effects.ModifyItemValue;
import com.ombremoon.tennocraft.common.world.level.loot.ModContextParamSets;
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

    public static final Supplier<DataComponentType<List<ModAttributeEffect>>> ATTRIBUTES = register(
            "attributes",
            builder -> builder.persistent(ModAttributeEffect.CODEC.codec().listOf())
    );
    public static final Supplier<DataComponentType<List<ModAttributeEffect>>> DAMAGE_ATTRIBUTES = register(
            "damage_attributes",
            builder -> builder.persistent(ModAttributeEffect.CODEC.codec().listOf())
    );
    public static final Supplier<DataComponentType<List<ConditionalModEffect<ModifyItemValue>>>> MODIFY_ITEM = register(
            "modify_item",
            builder -> builder.persistent(ConditionalModEffect.codec(ModifyItemValue.CODEC.codec(), ModContextParamSets.MODDED_ITEM).listOf())
    );
    public static final Supplier<DataComponentType<List<ConditionalModEffect<ModValueEffect>>>> DAMAGE = register(
            "damage",
            builder -> builder.persistent(ConditionalModEffect.codec(ModValueEffect.CODEC, ModContextParamSets.MODDED_DAMAGE).listOf())
    );
    public static final Supplier<DataComponentType<List<ConditionalModEffect<ModValueEffect>>>> CRIT_CHANCE = register(
            "crit_chance",
            builder -> builder.persistent(ConditionalModEffect.codec(ModValueEffect.CODEC, ModContextParamSets.MODDED_DAMAGE).listOf())
    );
    public static final Supplier<DataComponentType<List<ConditionalModEffect<ModValueEffect>>>> CRIT_MULTIPLIER = register(
            "crit_multiplier",
            builder -> builder.persistent(ConditionalModEffect.codec(ModValueEffect.CODEC, ModContextParamSets.MODDED_DAMAGE).listOf())
    );
    public static final Supplier<DataComponentType<List<ConditionalModEffect<ModValueEffect>>>> STATUS_CHANCE = register(
            "status_chance",
            builder -> builder.persistent(ConditionalModEffect.codec(ModValueEffect.CODEC, ModContextParamSets.MODDED_DAMAGE).listOf())
    );

    private static <T> Supplier<DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> operator) {
        return MOD_EFFECT_COMPONENT_TYPES.register(name, () -> operator.apply(DataComponentType.builder()).build());
    }

    public static void register(IEventBus modEventBus) {
        MOD_EFFECT_COMPONENT_TYPES.register(modEventBus);
    }
}
