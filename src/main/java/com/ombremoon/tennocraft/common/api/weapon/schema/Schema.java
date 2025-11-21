package com.ombremoon.tennocraft.common.api.weapon.schema;

import com.mojang.serialization.Codec;
import com.ombremoon.tennocraft.common.api.mod.Modification;
import com.ombremoon.tennocraft.common.init.TCSchemas;
import com.ombremoon.tennocraft.common.world.SlotGroup;
import com.ombremoon.tennocraft.main.Keys;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFileCodec;

public interface Schema {
    Codec<Schema> DIRECT_CODEC = TCSchemas.REGISTRY.byNameCodec().dispatch(Schema::getSerializer, SchemaSerializer::codec);
    Codec<Holder<Schema>> CODEC = RegistryFileCodec.create(Keys.SCHEMA, DIRECT_CODEC);
    StreamCodec<RegistryFriendlyByteBuf, Schema> STREAM_CODEC = ByteBufCodecs.registry(TCSchemas.RESOURCE_KEY).dispatch(Schema::getSerializer, SchemaSerializer::streamCodec);

    SchemaSerializer<?> getSerializer();

    SlotGroup getType();
}
