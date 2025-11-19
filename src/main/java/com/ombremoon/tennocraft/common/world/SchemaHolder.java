package com.ombremoon.tennocraft.common.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.weapon.schema.Schema;
import com.ombremoon.tennocraft.main.Keys;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public record SchemaHolder<T>(ResourceKey<Schema> schemaKey, T schema, String type) {
    public static final Codec<SchemaHolder<?>> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    ResourceKey.codec(Keys.SCHEMA).fieldOf("key").forGetter(SchemaHolder::schemaKey),
                    Schema.DIRECT_CODEC.fieldOf("schema").forGetter(schemaHolder -> (Schema) schemaHolder.schema),
                    Codec.STRING.fieldOf("type").forGetter(SchemaHolder::type)
            ).apply(instance, SchemaHolder::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, SchemaHolder<?>> STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(Keys.SCHEMA), SchemaHolder::schemaKey,
            Schema.STREAM_CODEC, schemaHolder -> (Schema) schemaHolder.schema,
            ByteBufCodecs.STRING_UTF8, SchemaHolder::type,
            SchemaHolder::new
    );

    public ResourceLocation location() {
        return this.schemaKey.location();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else {
            return other instanceof SchemaHolder<?> holder && this.schemaKey.equals(holder.schemaKey);
        }
    }

    @Override
    public int hashCode() {
        return this.schemaKey.hashCode();
    }

    @Override
    public String toString() {
        return this.schemaKey.toString();
    }
}
