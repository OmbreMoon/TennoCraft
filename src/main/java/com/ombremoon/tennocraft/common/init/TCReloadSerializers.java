package com.ombremoon.tennocraft.common.init;

import com.ombremoon.tennocraft.common.api.weapon.projectile.*;
import com.ombremoon.tennocraft.main.CommonClass;
import com.ombremoon.tennocraft.main.Constants;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public class TCReloadSerializers {
    public static final ResourceKey<Registry<ReloadSerializer<?>>> RESOURCE_KEY = ResourceKey.createRegistryKey(CommonClass.customLocation("reload_type"));
    public static final Registry<ReloadSerializer<?>> REGISTRY = new RegistryBuilder<>(RESOURCE_KEY).sync(true).create();
    public static final DeferredRegister<ReloadSerializer<?>> RELOAD_SERIALIZERS = DeferredRegister.create(REGISTRY, Constants.MOD_ID);

    public static final Supplier<ReloadSerializer<AmmoReloadType>> AMMO = RELOAD_SERIALIZERS.register("ammo", AmmoReloadType.Serializer::new);
    public static final Supplier<ReloadSerializer<ResourceReloadType>> RESOURCE = RELOAD_SERIALIZERS.register("resource", ResourceReloadType.Serializer::new);
    public static final Supplier<ReloadSerializer<DurationReloadType>> DURATION = RELOAD_SERIALIZERS.register("duration", DurationReloadType.Serializer::new);

    public static void register(IEventBus modEventBus) {
        RELOAD_SERIALIZERS.register(modEventBus);
    }
}
