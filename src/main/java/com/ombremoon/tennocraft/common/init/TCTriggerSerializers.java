package com.ombremoon.tennocraft.common.init;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.ombremoon.tennocraft.common.api.weapon.ranged.trigger.*;
import com.ombremoon.tennocraft.util.SerializationUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class TCTriggerSerializers {
    private static final Map<ResourceLocation, TriggerSerializer<?>> REGISTRY = new HashMap<>();
    public static final Codec<TriggerSerializer<?>> CODEC = ResourceLocation.CODEC
            .comapFlatMap(
                    location -> {
                        TriggerSerializer<?> serializer = REGISTRY.get(location);
                        return serializer != null
                                ? DataResult.success(serializer)
                                : DataResult.error(() -> "No TriggerSerializer with key: " + location);
                    },
                    TriggerSerializer::id
            );
    public static final StreamCodec<RegistryFriendlyByteBuf, TriggerSerializer<?>> STREAM_CODEC = SerializationUtil.REGISTRY_RESOURCE_STREAM_CODEC
            .map(TCTriggerSerializers::getProjectileFromLocation, TriggerSerializer::id);

    public static TriggerSerializer<?> getProjectileFromLocation(ResourceLocation resourceLocation) {
        return REGISTRY.getOrDefault(resourceLocation, null);
    }

    public static final TriggerSerializer<AutoTrigger> AUTO = register(new AutoTrigger.Serializer());
    public static final TriggerSerializer<AutoSpoolTrigger> AUTO_SPOOL = register(new AutoSpoolTrigger.Serializer());
    public static final TriggerSerializer<SemiTrigger> SEMI = register(new SemiTrigger.Serializer());
    public static final TriggerSerializer<BurstTrigger> BURST = register(new BurstTrigger.Serializer());
    public static final TriggerSerializer<DuplexTrigger> DUPLEX = register(new DuplexTrigger.Serializer());
    public static final TriggerSerializer<ActiveTrigger> ACTIVE = register(new ActiveTrigger.Serializer());
    public static final TriggerSerializer<HeldTrigger> HELD = register(new HeldTrigger.Serializer());
    public static final TriggerSerializer<ChargeTrigger> CHARGE = register(new ChargeTrigger.Serializer());
    public static final TriggerSerializer<FallbackTrigger> FALLBACK = register(new FallbackTrigger.Serializer());

    private static <T extends TriggerType<T>> TriggerSerializer<T> register(TriggerSerializer<T> serializer) {
        REGISTRY.put(serializer.id(), serializer);
        return serializer;
    }
}
