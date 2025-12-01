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
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;

import java.util.Optional;

public record AreaOfEffect(float range, DamageFalloff falloff, Holder<Bullet> bullet) implements ProjectileType<AreaOfEffect> {

    @Override
    public ProjectileSerializer<AreaOfEffect> getSerializer() {
        return TCProjectileSerializers.AOE;
    }

    @Override
    public Optional<DamageFalloff> getFalloff() {
        return Optional.of(this.falloff);
    }

    public static class Serializer implements ProjectileSerializer<AreaOfEffect> {
        private static final ResourceLocation LOCATION = CommonClass.customLocation("aoe");
        public static final MapCodec<AreaOfEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        ExtraCodecs.POSITIVE_FLOAT.fieldOf("range").forGetter(AreaOfEffect::range),
                        DamageFalloff.CODEC.fieldOf("falloff").forGetter(AreaOfEffect::falloff),
                        Bullet.CODEC.fieldOf("bullet").forGetter(AreaOfEffect::bullet)
                ).apply(instance, AreaOfEffect::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, AreaOfEffect> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public AreaOfEffect decode(RegistryFriendlyByteBuf buffer) {
                float range = buffer.readFloat();
                DamageFalloff falloff = DamageFalloff.STREAM_CODEC.decode(buffer);
                ResourceKey<Bullet> key = buffer.readResourceKey(Keys.BULLET);
                Holder<Bullet> bullet = buffer.registryAccess().holderOrThrow(key);
                return new AreaOfEffect(range, falloff, bullet);
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buffer, AreaOfEffect value) {
                buffer.writeFloat(value.range);
                DamageFalloff.STREAM_CODEC.encode(buffer, value.falloff);
                ResourceKey<Bullet> key = value.bullet.unwrapKey().orElseThrow();
                buffer.writeResourceKey(key);
            }
        };

        @Override
        public ResourceLocation id() {
            return LOCATION;
        }

        @Override
        public MapCodec<AreaOfEffect> codec() {
            return MAP_CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AreaOfEffect> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
