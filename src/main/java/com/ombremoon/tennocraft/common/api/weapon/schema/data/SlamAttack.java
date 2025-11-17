package com.ombremoon.tennocraft.common.api.weapon.schema.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;

public record SlamAttack(ResourceKey<DamageType> damage, Holder<MobEffect> forcedProc, float radius) {
    public static final Codec<SlamAttack> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    ResourceKey.codec(Registries.DAMAGE_TYPE).fieldOf("damage").forGetter(SlamAttack::damage),
                    MobEffect.CODEC.fieldOf("forcedProc").forGetter(SlamAttack::forcedProc),
                    Codec.FLOAT.fieldOf("radius").forGetter(SlamAttack::radius)
            ).apply(instance, SlamAttack::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, SlamAttack> STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(Registries.DAMAGE_TYPE), SlamAttack::damage,
            ByteBufCodecs.holderRegistry(Registries.MOB_EFFECT), SlamAttack::forcedProc,
            ByteBufCodecs.FLOAT, SlamAttack::radius,
            SlamAttack::new
    );
}
