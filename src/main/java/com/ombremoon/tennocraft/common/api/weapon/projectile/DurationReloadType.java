package com.ombremoon.tennocraft.common.api.weapon.projectile;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.init.TCReloadSerializers;
import com.ombremoon.tennocraft.common.world.Resource;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

import java.util.Optional;

public record DurationReloadType(int initialDelay, int ammoPerSecond, Optional<ConsumeEvent> consumeWhen) implements ReloadType<DurationReloadType> {

    @Override
    public ReloadSerializer<DurationReloadType> getSerializer() {
        return TCReloadSerializers.DURATION.get();
    }

    public static class Serializer implements ReloadSerializer<DurationReloadType> {
        public static final MapCodec<DurationReloadType> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        Codec.INT.fieldOf("initial_delay").forGetter(DurationReloadType::initialDelay),
                        Codec.INT.fieldOf("ammo_per_second").forGetter(DurationReloadType::ammoPerSecond),
                        ConsumeEvent.CODEC.optionalFieldOf("consume_when").forGetter(DurationReloadType::consumeWhen)
                ).apply(instance, DurationReloadType::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, DurationReloadType> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, DurationReloadType::initialDelay,
                ByteBufCodecs.VAR_INT, DurationReloadType::ammoPerSecond,
                ByteBufCodecs.optional(NeoForgeStreamCodecs.enumCodec(ConsumeEvent.class)), DurationReloadType::consumeWhen,
                DurationReloadType::new
        );

        @Override
        public MapCodec<DurationReloadType> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, DurationReloadType> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
