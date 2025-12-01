package com.ombremoon.tennocraft.common.api.weapon.ranged.trigger;

import com.mojang.serialization.MapCodec;
import com.ombremoon.tennocraft.common.init.TCTriggerSerializers;
import com.ombremoon.tennocraft.main.CommonClass;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Optional;

public record FallbackTrigger() implements TriggerType<FallbackTrigger> {
    public static final ResourceLocation TYPE = CommonClass.customLocation("fallback");

    @Override
    public TriggerSerializer<FallbackTrigger> getSerializer() {
        return TCTriggerSerializers.FALLBACK;
    }

    @Override
    public Optional<LootItemCondition> canFire() {
        return Optional.empty();
    }

    public static class Serializer implements TriggerSerializer<FallbackTrigger> {
        public static final MapCodec<FallbackTrigger> CODEC = MapCodec.unit(FallbackTrigger::new);
        public static final StreamCodec<RegistryFriendlyByteBuf, FallbackTrigger> STREAM_CODEC = StreamCodec.unit(new FallbackTrigger());

        @Override
        public ResourceLocation id() {
            return TYPE;
        }

        @Override
        public MapCodec<FallbackTrigger> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, FallbackTrigger> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
