package com.ombremoon.tennocraft.common.init;

import com.mojang.serialization.Codec;
import com.ombremoon.tennocraft.common.api.mod.ConditionalModEffect;
import com.ombremoon.tennocraft.common.api.mod.effects.ModAttributeEffect;
import com.ombremoon.tennocraft.common.api.mod.effects.ModValueEffect;
import com.ombremoon.tennocraft.common.api.mod.effects.ModifyDamageEffect;
import com.ombremoon.tennocraft.common.api.mod.effects.ModifyItemEffect;
import com.ombremoon.tennocraft.common.world.level.loot.ModContextParams;
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
    public static final Supplier<DataComponentType<List<ModifyItemEffect>>> MODIFY_ITEM = register(
            "modify_item",
            builder -> builder.persistent(ModifyItemEffect.CODEC.listOf())
    );
    public static final Supplier<DataComponentType<List<ModifyDamageEffect>>> MODIFY_ITEM_DAMAGE = register(
            "modify_item_damage",
            builder -> builder.persistent(ModifyDamageEffect.CODEC.listOf())
    );
    public static final Supplier<DataComponentType<List<ConditionalModEffect<ModValueEffect>>>> DAMAGE = register(
            "damage",
            builder -> builder.persistent(ConditionalModEffect.codec(ModValueEffect.CODEC, ModContextParams.MODDED_ATTACK).listOf())
    );
    public static final Supplier<DataComponentType<List<ConditionalModEffect<ModValueEffect>>>> IMPACT = register(
            "impact_damage",
            builder -> builder.persistent(ConditionalModEffect.codec(ModValueEffect.CODEC, ModContextParams.MODDED_ATTACK).listOf())
    );
    public static final Supplier<DataComponentType<List<ConditionalModEffect<ModValueEffect>>>> PUNCTURE = register(
            "puncture_damage",
            builder -> builder.persistent(ConditionalModEffect.codec(ModValueEffect.CODEC, ModContextParams.MODDED_ATTACK).listOf())
    );
    public static final Supplier<DataComponentType<List<ConditionalModEffect<ModValueEffect>>>> SLASH = register(
            "slash_damage",
            builder -> builder.persistent(ConditionalModEffect.codec(ModValueEffect.CODEC, ModContextParams.MODDED_ATTACK).listOf())
    );
    public static final Supplier<DataComponentType<List<ConditionalModEffect<ModValueEffect>>>> HEAT = register(
            "heat_damage",
            builder -> builder.persistent(ConditionalModEffect.codec(ModValueEffect.CODEC, ModContextParams.MODDED_ATTACK).listOf())
    );
    public static final Supplier<DataComponentType<List<ConditionalModEffect<ModValueEffect>>>> COLD = register(
            "cold_damage",
            builder -> builder.persistent(ConditionalModEffect.codec(ModValueEffect.CODEC, ModContextParams.MODDED_ATTACK).listOf())
    );
    public static final Supplier<DataComponentType<List<ConditionalModEffect<ModValueEffect>>>> ELECTRICITY = register(
            "electricity_damage",
            builder -> builder.persistent(ConditionalModEffect.codec(ModValueEffect.CODEC, ModContextParams.MODDED_ATTACK).listOf())
    );
    public static final Supplier<DataComponentType<List<ConditionalModEffect<ModValueEffect>>>> TOXIC = register(
            "toxic_damage",
            builder -> builder.persistent(ConditionalModEffect.codec(ModValueEffect.CODEC, ModContextParams.MODDED_ATTACK).listOf())
    );
    public static final Supplier<DataComponentType<List<ConditionalModEffect<ModValueEffect>>>> BLAST = register(
            "blast_damage",
            builder -> builder.persistent(ConditionalModEffect.codec(ModValueEffect.CODEC, ModContextParams.MODDED_ATTACK).listOf())
    );
    public static final Supplier<DataComponentType<List<ConditionalModEffect<ModValueEffect>>>> CORROSIVE = register(
            "corrosion_damage",
            builder -> builder.persistent(ConditionalModEffect.codec(ModValueEffect.CODEC, ModContextParams.MODDED_ATTACK).listOf())
    );
    public static final Supplier<DataComponentType<List<ConditionalModEffect<ModValueEffect>>>> MAGNETIC = register(
            "magnetic_damage",
            builder -> builder.persistent(ConditionalModEffect.codec(ModValueEffect.CODEC, ModContextParams.MODDED_ATTACK).listOf())
    );
    public static final Supplier<DataComponentType<List<ConditionalModEffect<ModValueEffect>>>> GAS = register(
            "gas_damage",
            builder -> builder.persistent(ConditionalModEffect.codec(ModValueEffect.CODEC, ModContextParams.MODDED_ATTACK).listOf())
    );
    public static final Supplier<DataComponentType<List<ConditionalModEffect<ModValueEffect>>>> RADIATION = register(
            "radiation_damage",
            builder -> builder.persistent(ConditionalModEffect.codec(ModValueEffect.CODEC, ModContextParams.MODDED_ATTACK).listOf())
    );
    public static final Supplier<DataComponentType<List<ConditionalModEffect<ModValueEffect>>>> VIRAL = register(
            "viral_damage",
            builder -> builder.persistent(ConditionalModEffect.codec(ModValueEffect.CODEC, ModContextParams.MODDED_ATTACK).listOf())
    );
    public static final Supplier<DataComponentType<List<ConditionalModEffect<ModValueEffect>>>> CRIT_CHANCE = register(
            "crit_chance",
            builder -> builder.persistent(ConditionalModEffect.codec(ModValueEffect.CODEC, ModContextParams.MODDED_ATTACK).listOf())
    );
    public static final Supplier<DataComponentType<List<ConditionalModEffect<ModValueEffect>>>> CRIT_MULTIPLIER = register(
            "crit_multiplier",
            builder -> builder.persistent(ConditionalModEffect.codec(ModValueEffect.CODEC, ModContextParams.MODDED_ATTACK).listOf())
    );
    public static final Supplier<DataComponentType<List<ConditionalModEffect<ModValueEffect>>>> STATUS_CHANCE = register(
            "status_chance",
            builder -> builder.persistent(ConditionalModEffect.codec(ModValueEffect.CODEC, ModContextParams.MODDED_ATTACK).listOf())
    );

    private static <T> Supplier<DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> operator) {
        return MOD_EFFECT_COMPONENT_TYPES.register(name, () -> operator.apply(DataComponentType.builder()).build());
    }

    public static void register(IEventBus modEventBus) {
        MOD_EFFECT_COMPONENT_TYPES.register(modEventBus);
    }
}
