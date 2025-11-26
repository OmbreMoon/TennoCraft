package com.ombremoon.tennocraft.common.api.weapon.projectile;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.init.TCReloadSerializers;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

import java.util.Optional;

public record AmmoReloadType(Optional<ConsumeEvent> consumeWhen) implements ReloadType<AmmoReloadType> {

    @Override
    public ReloadSerializer<AmmoReloadType> getSerializer() {
        return TCReloadSerializers.AMMO.get();
    }

    public static class Serializer implements ReloadSerializer<AmmoReloadType> {
        public static final MapCodec<AmmoReloadType> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        ConsumeEvent.CODEC.optionalFieldOf("consume_when").forGetter(AmmoReloadType::consumeWhen)
                ).apply(instance, AmmoReloadType::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, AmmoReloadType> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.optional(NeoForgeStreamCodecs.enumCodec(ConsumeEvent.class)), AmmoReloadType::consumeWhen,
                AmmoReloadType::new
        );

        @Override
        public MapCodec<AmmoReloadType> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AmmoReloadType> streamCodec() {
            return STREAM_CODEC;
        }
    }

    public AmmoReloadType() {
        this(Optional.of(ConsumeEvent.ON_FIRE));
    }
}
