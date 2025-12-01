package com.ombremoon.tennocraft.common.init;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.ombremoon.tennocraft.common.api.weapon.ranged.projectile.*;
import com.ombremoon.tennocraft.main.CommonClass;
import com.ombremoon.tennocraft.util.SerializationUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class TCProjectileSerializers {
    private static final Map<ResourceLocation, ProjectileSerializer<?>> REGISTRY = new HashMap<>();
    public static final Codec<ProjectileSerializer<?>> CODEC = ResourceLocation.CODEC
            .comapFlatMap(
                    location -> {
                        ProjectileSerializer<?> serializer = REGISTRY.get(location);
                        return serializer != null
                                ? DataResult.success(serializer)
                                : DataResult.error(() -> "No ProjectileSerializer with key: " + location);
                    },
                    ProjectileSerializer::id
            );
    public static final StreamCodec<RegistryFriendlyByteBuf, ProjectileSerializer<?>> STREAM_CODEC = SerializationUtil.REGISTRY_RESOURCE_STREAM_CODEC
            .map(TCProjectileSerializers::getProjectileFromLocation, ProjectileSerializer::id);

    public static ProjectileSerializer<?> getProjectileFromLocation(ResourceLocation resourceLocation) {
        return REGISTRY.getOrDefault(resourceLocation, null);
    }

    public static final ProjectileSerializer<Hitscan> HITSCAN = register("hitscan", new Hitscan.Serializer());
    public static final ProjectileSerializer<SolidProjectile> PROJECTILE = register("projectile", new SolidProjectile.Serializer());
    public static final ProjectileSerializer<AreaOfEffect> AOE = register("aoe", new AreaOfEffect.Serializer());

    private static <T extends ProjectileType<T>> ProjectileSerializer<T> register(String name, ProjectileSerializer<T> serializer) {
        REGISTRY.put(CommonClass.customLocation(name), serializer);
        return serializer;
    }
}
