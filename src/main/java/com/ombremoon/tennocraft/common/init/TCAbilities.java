package com.ombremoon.tennocraft.common.init;

import com.ombremoon.tennocraft.common.modholder.api.AbilityType;
import com.ombremoon.tennocraft.common.modholder.api.PassiveAbilityType;
import com.ombremoon.tennocraft.main.CommonClass;
import com.ombremoon.tennocraft.main.Constants;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class TCAbilities {
    public static final ResourceKey<Registry<AbilityType<?>>> RESOURCE_KEY = ResourceKey.createRegistryKey(CommonClass.customLocation("ability_type"));
    public static final Registry<AbilityType<?>> REGISTRY = new RegistryBuilder<>(RESOURCE_KEY).sync(true).create();
    public static final DeferredRegister<AbilityType<?>> ABILITY_TYPES = DeferredRegister.create(REGISTRY, Constants.MOD_ID);

    public static final Holder<AbilityType<?>> TEST = ABILITY_TYPES.register("test", () -> new AbilityType<>());
    public static final Holder<AbilityType<?>> TEST1 = ABILITY_TYPES.register("test1", () -> new PassiveAbilityType<>());

    public static void register(IEventBus modEventBus) {
        ABILITY_TYPES.register(modEventBus);
    }
}
