package com.ombremoon.tennocraft.common.api.weapon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;

public record DamageValue(ResourceKey<DamageType> damageType, float amount) {
    public static final Codec<DamageValue> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    ResourceKey.codec(Registries.DAMAGE_TYPE).fieldOf("damageType").forGetter(DamageValue::damageType),
                    Codec.FLOAT.fieldOf("amount").forGetter(DamageValue::amount)
            ).apply(instance, DamageValue::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, DamageValue> STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(Registries.DAMAGE_TYPE), DamageValue::damageType,
            ByteBufCodecs.FLOAT, DamageValue::amount,
            DamageValue::new
    );

    public Holder<DamageType> toDamageType(Level level) {
        return level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(damageType);
    }
}
