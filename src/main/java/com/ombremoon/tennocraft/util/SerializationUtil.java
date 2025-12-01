package com.ombremoon.tennocraft.util;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public class SerializationUtil {
    public static final StreamCodec<RegistryFriendlyByteBuf, ResourceLocation> REGISTRY_RESOURCE_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, ResourceLocation::getNamespace,
            ByteBufCodecs.STRING_UTF8, ResourceLocation::getPath,
            ResourceLocation::fromNamespaceAndPath
    );
}
