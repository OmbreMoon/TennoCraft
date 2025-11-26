package com.ombremoon.tennocraft.common.init;

import com.ombremoon.tennocraft.common.api.weapon.projectile.AreaOfEffect;
import com.ombremoon.tennocraft.common.api.weapon.projectile.Hitscan;
import com.ombremoon.tennocraft.common.api.weapon.projectile.SolidProjectile;
import com.ombremoon.tennocraft.common.api.weapon.projectile.ProjectileSerializer;
import com.ombremoon.tennocraft.main.CommonClass;
import com.ombremoon.tennocraft.main.Constants;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public class TCProjectileSerializers {
    public static final ResourceKey<Registry<ProjectileSerializer<?>>> RESOURCE_KEY = ResourceKey.createRegistryKey(CommonClass.customLocation("projectile_type"));
    public static final Registry<ProjectileSerializer<?>> REGISTRY = new RegistryBuilder<>(RESOURCE_KEY).sync(true).create();
    public static final DeferredRegister<ProjectileSerializer<?>> PROJECTILE_SERIALIZERS = DeferredRegister.create(REGISTRY, Constants.MOD_ID);

    public static final Supplier<ProjectileSerializer<Hitscan>> HITSCAN = PROJECTILE_SERIALIZERS.register("hitscan", Hitscan.Serializer::new);
    public static final Supplier<ProjectileSerializer<SolidProjectile>> PROJECTILE = PROJECTILE_SERIALIZERS.register("projectile", SolidProjectile.Serializer::new);
    public static final Supplier<ProjectileSerializer<AreaOfEffect>> AOE = PROJECTILE_SERIALIZERS.register("aoe", AreaOfEffect.Serializer::new);

    public static void register(IEventBus modEventBus) {
        PROJECTILE_SERIALIZERS.register(modEventBus);
    }
}
