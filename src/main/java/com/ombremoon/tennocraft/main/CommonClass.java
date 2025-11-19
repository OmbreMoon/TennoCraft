package com.ombremoon.tennocraft.main;

import com.ombremoon.tennocraft.common.init.*;
import com.ombremoon.tennocraft.common.api.mod.RankBasedValue;
import com.ombremoon.tennocraft.common.api.mod.TCModEffectComponents;
import com.ombremoon.tennocraft.common.api.mod.effects.ModEntityEffect;
import com.ombremoon.tennocraft.common.api.mod.effects.ModLocationEffect;
import com.ombremoon.tennocraft.common.api.mod.effects.ModValueEffect;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CommonClass {

    public static void init(IEventBus modEventBus) {
        TCAbilities.register(modEventBus);
        TCAttributes.register(modEventBus);
        TCData.register(modEventBus);
        TCItems.register(modEventBus);
        TCModEffectComponents.register(modEventBus);
        TCProjectileSerializers.register(modEventBus);
        TCSchemas.register(modEventBus);
        TCStatusEffects.register(modEventBus);

        registerSimple(RankBasedValue.RANK_BASED_VALUE_TYPES, modEventBus, RankBasedValue::bootstrap);
        registerSimple(ModLocationEffect.MOD_LOCATION_EFFECT_TYPES, modEventBus, ModLocationEffect::bootstrap);
        registerSimple(ModValueEffect.MOD_VALUE_EFFECT_TYPES, modEventBus, ModValueEffect::bootstrap);
        registerSimple(ModEntityEffect.MOD_ENTITY_EFFECT_TYPES, modEventBus, ModEntityEffect::bootstrap);
    }

    private static <T> void registerSimple(DeferredRegister<T> registry, IEventBus modEventBus, DeferredBootstrap<T> bootstrap) {
        registry.register(modEventBus);
        bootstrap.run(registry);
    }

    public static boolean isDevEnv() {
        return !FMLLoader.isProduction();
    }

    public static ResourceLocation customLocation(String name) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name);
    }

    @FunctionalInterface
    interface DeferredBootstrap<T> {
        Object run(DeferredRegister<T> registry);
    }
}
