package com.ombremoon.tennocraft.common.init;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.ombremoon.tennocraft.common.api.weapon.ranged.reload.*;
import com.ombremoon.tennocraft.main.CommonClass;
import com.ombremoon.tennocraft.util.SerializationUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class TCReloadSerializers {
    private static final Map<ResourceLocation, ReloadSerializer<?>> REGISTRY = new HashMap<>();
    public static final Codec<ReloadSerializer<?>> CODEC = ResourceLocation.CODEC
            .comapFlatMap(
                    location -> {
                        ReloadSerializer<?> serializer = REGISTRY.get(location);
                        return serializer != null
                                ? DataResult.success(serializer)
                                : DataResult.error(() -> "No ReloadSerializer with key: " + location);
                    },
                    ReloadSerializer::id
            );
    public static final StreamCodec<RegistryFriendlyByteBuf, ReloadSerializer<?>> STREAM_CODEC = SerializationUtil.REGISTRY_RESOURCE_STREAM_CODEC
            .map(TCReloadSerializers::getTypeFromLocation, ReloadSerializer::id);

    public static ReloadSerializer<?> getTypeFromLocation(ResourceLocation resourceLocation) {
        return REGISTRY.getOrDefault(resourceLocation, null);
    }

    public static final ReloadSerializer<AmmoReloadType> AMMO = register("ammo", new AmmoReloadType.Serializer());
    public static final ReloadSerializer<DurationReloadType> DURATION = register("duration", new DurationReloadType.Serializer());
    public static final ReloadSerializer<ResourceReloadType> RESOURCE = register("resource", new ResourceReloadType.Serializer());
    public static final ReloadSerializer<NoReloadType> NO_RELOAD = register("no_reload", new NoReloadType.Serializer());

    private static <T extends ReloadType<T>> ReloadSerializer<T> register(String name, ReloadSerializer<T> serializer) {
        REGISTRY.put(CommonClass.customLocation(name), serializer);
        return serializer;
    }
}
