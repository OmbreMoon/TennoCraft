package com.ombremoon.tennocraft.common.api.weapon.schema.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.init.TCTags;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffect;

import java.util.List;
import java.util.Optional;

public record AttackMultiplier(float multiplier, Optional<List<Holder<MobEffect>>> forcedProcs) {
    public static final Codec<AttackMultiplier> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.FLOAT.fieldOf("multiplier").forGetter(AttackMultiplier::multiplier),
                    BuiltInRegistries.MOB_EFFECT.holderByNameCodec().listOf().optionalFieldOf("forcedProcs").forGetter(AttackMultiplier::forcedProcs)
            ).apply(instance, AttackMultiplier::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, AttackMultiplier> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, AttackMultiplier::multiplier,
            ByteBufCodecs.optional(ByteBufCodecs.holderRegistry(Registries.MOB_EFFECT).apply(ByteBufCodecs.list())), AttackMultiplier::forcedProcs,
            AttackMultiplier::new
    );

    public AttackMultiplier(float multiplier, Optional<List<Holder<MobEffect>>> forcedProcs) {
        this.multiplier = multiplier;
        this.forcedProcs = forcedProcs;

        if (forcedProcs.isPresent()) {
            for (var effect : forcedProcs.get()) {
                if (!effect.is(TCTags.MobEffects.APPLICABLE_FORCED_PROC)) {
                    throw new IllegalArgumentException("Defined attack multiplier with inapplicable forced proc: " + effect);
                }
            }
        }
    }
}
