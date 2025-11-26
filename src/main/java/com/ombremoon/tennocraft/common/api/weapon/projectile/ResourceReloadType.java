package com.ombremoon.tennocraft.common.api.weapon.projectile;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.mod.effects.ModValueEffect;
import com.ombremoon.tennocraft.common.init.TCReloadSerializers;
import com.ombremoon.tennocraft.common.world.Resource;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

import java.util.Optional;

public record ResourceReloadType(Resource resource, float percentResource, Optional<ConsumeEvent> consumeWhen) implements ReloadType<ResourceReloadType> {

    @Override
    public ReloadSerializer<ResourceReloadType> getSerializer() {
        return TCReloadSerializers.RESOURCE.get();
    }

    public static class Serializer implements ReloadSerializer<ResourceReloadType> {
        public static final MapCodec<ResourceReloadType> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        Resource.CODEC.fieldOf("resource").forGetter(ResourceReloadType::resource),
                        Codec.FLOAT.fieldOf("percent_resource").forGetter(ResourceReloadType::percentResource),
                        ConsumeEvent.CODEC.optionalFieldOf("consume_when").forGetter(ResourceReloadType::consumeWhen)
                ).apply(instance, ResourceReloadType::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, ResourceReloadType> STREAM_CODEC = StreamCodec.composite(
                NeoForgeStreamCodecs.enumCodec(Resource.class), ResourceReloadType::resource,
                ByteBufCodecs.FLOAT, ResourceReloadType::percentResource,
                ByteBufCodecs.optional(NeoForgeStreamCodecs.enumCodec(ConsumeEvent.class)), ResourceReloadType::consumeWhen,
                ResourceReloadType::new
        );

        @Override
        public MapCodec<ResourceReloadType> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ResourceReloadType> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
