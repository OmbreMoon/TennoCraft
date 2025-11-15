package com.ombremoon.tennocraft.common.modholder.api.weapon.schema;

import com.mojang.serialization.Codec;
import com.ombremoon.tennocraft.common.init.TCSchemas;
import com.ombremoon.tennocraft.main.Keys;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.neoforged.neoforge.common.CommonHooks;

public interface Schema {
    Codec<Schema> DIRECT_CODEC = TCSchemas.REGISTRY.byNameCodec().dispatch(Schema::getSerializer, SchemaSerializer::codec);
    Codec<Holder<Schema>> CODEC = RegistryFileCodec.create(Keys.SCHEMA, DIRECT_CODEC);
    StreamCodec<RegistryFriendlyByteBuf, Schema> STREAM_CODEC = ByteBufCodecs.registry(TCSchemas.RESOURCE_KEY).dispatch(Schema::getSerializer, SchemaSerializer::streamCodec);

    SchemaSerializer<?> getSerializer();
}
