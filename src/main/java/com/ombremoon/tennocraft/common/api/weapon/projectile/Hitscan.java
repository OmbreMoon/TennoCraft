package com.ombremoon.tennocraft.common.api.weapon.projectile;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.weapon.schema.data.DamageFalloff;
import com.ombremoon.tennocraft.common.init.TCProjectileSerializers;
import com.ombremoon.tennocraft.main.Keys;
import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;

import java.util.Optional;

public record Hitscan(int maxDistance, Optional<DamageFalloff> falloff, Holder<Bullet> bullet) implements ProjectileType<Hitscan> {

    public Hitscan(int maxDistance, Holder<Bullet> bullet) {
        this(maxDistance, Optional.empty(), bullet);
    }
    public Hitscan(Holder<Bullet> bullet) {
        this(300, Optional.empty(), bullet);
    }

    @Override
    public ProjectileSerializer<Hitscan> getSerializer() {
        return TCProjectileSerializers.HITSCAN.get();
    }

    @Override
    public Optional<DamageFalloff> getFalloff() {
        return this.falloff;
    }

    public static class Serializer implements ProjectileSerializer<Hitscan> {
        public static final MapCodec<Hitscan> MAP_CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        ExtraCodecs.POSITIVE_INT.fieldOf("max_distance").forGetter(Hitscan::maxDistance),
                        DamageFalloff.CODEC.optionalFieldOf("falloff").forGetter(Hitscan::falloff),
                        Bullet.CODEC.fieldOf("bullet").forGetter(Hitscan::bullet)
                ).apply(instance, Hitscan::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, Hitscan> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public Hitscan decode(RegistryFriendlyByteBuf buffer) {
                int maxDistance = buffer.readVarInt();
                Optional<DamageFalloff> falloff = ByteBufCodecs.optional(DamageFalloff.STREAM_CODEC).decode(buffer);
                ResourceKey<Bullet> key = ResourceKey.streamCodec(Keys.BULLET).decode(buffer);
                Holder<Bullet> bullet = buffer.registryAccess().holderOrThrow(key);
                return new Hitscan(maxDistance, falloff, bullet);
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buffer, Hitscan value) {
                buffer.writeVarInt(value.maxDistance);
                ByteBufCodecs.optional(DamageFalloff.STREAM_CODEC).encode(buffer, value.falloff);
                ResourceKey<Bullet> key = value.bullet.unwrapKey().orElseThrow();
                ResourceKey.streamCodec(Keys.BULLET).encode(buffer, key);
            }
        };

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
