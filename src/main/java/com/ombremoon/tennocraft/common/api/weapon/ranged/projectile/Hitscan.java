package com.ombremoon.tennocraft.common.api.weapon.ranged.projectile;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.weapon.ranged.Bullet;
import com.ombremoon.tennocraft.common.api.weapon.schema.data.DamageFalloff;
import com.ombremoon.tennocraft.common.init.TCProjectileSerializers;
import com.ombremoon.tennocraft.main.CommonClass;
import com.ombremoon.tennocraft.main.Keys;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;

import java.util.Optional;

public record Hitscan(Optional<Float> maxDistance, Optional<DamageFalloff> falloff, Holder<Bullet> bullet) implements ProjectileType<Hitscan> {

    public Hitscan(float maxDistance, DamageFalloff falloff, Holder<Bullet> bullet) {
        this(Optional.of(maxDistance), Optional.of(falloff), bullet);
    }

    public Hitscan(float maxDistance, Holder<Bullet> bullet) {
        this(Optional.of(maxDistance), Optional.empty(), bullet);
    }

    public Hitscan(Holder<Bullet> bullet) {
        this(Optional.empty(), Optional.empty(), bullet);
    }

    public float getRange() {
        return this.maxDistance.orElse(300.0F);
    }

    @Override
    public ProjectileSerializer<Hitscan> getSerializer() {
        return TCProjectileSerializers.HITSCAN;
    }

    @Override
    public Optional<DamageFalloff> getFalloff() {
        return this.falloff;
    }

    public static class Serializer implements ProjectileSerializer<Hitscan> {
        private static final ResourceLocation LOCATION = CommonClass.customLocation("hitscan");
        public static final MapCodec<Hitscan> MAP_CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("max_distance").forGetter(Hitscan::maxDistance),
                        DamageFalloff.CODEC.optionalFieldOf("falloff").forGetter(Hitscan::falloff),
                        Bullet.CODEC.fieldOf("bullet").forGetter(Hitscan::bullet)
                ).apply(instance, Hitscan::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, Hitscan> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public Hitscan decode(RegistryFriendlyByteBuf buffer) {
                Optional<Float> maxDistance = ByteBufCodecs.optional(ByteBufCodecs.FLOAT).decode(buffer);
                Optional<DamageFalloff> falloff = ByteBufCodecs.optional(DamageFalloff.STREAM_CODEC).decode(buffer);
                ResourceKey<Bullet> key = ResourceKey.streamCodec(Keys.BULLET).decode(buffer);
                Holder<Bullet> bullet = buffer.registryAccess().holderOrThrow(key);
                return new Hitscan(maxDistance, falloff, bullet);
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buffer, Hitscan value) {
                ByteBufCodecs.optional(ByteBufCodecs.FLOAT).encode(buffer, value.maxDistance);
                ByteBufCodecs.optional(DamageFalloff.STREAM_CODEC).encode(buffer, value.falloff);
                ResourceKey<Bullet> key = value.bullet.unwrapKey().orElseThrow();
                ResourceKey.streamCodec(Keys.BULLET).encode(buffer, key);
            }
        };

        @Override
        public ResourceLocation id() {
            return LOCATION;
        }

        @Override
        public MapCodec<Hitscan> codec() {
            return MAP_CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, Hitscan> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
