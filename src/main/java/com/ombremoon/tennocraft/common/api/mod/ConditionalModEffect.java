package com.ombremoon.tennocraft.common.api.mod;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.mod.effects.ModConditions;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Optional;

public record ConditionalModEffect<T>(T effect, Optional<ModConditions> conditions) {

    public static <T>Codec<ConditionalModEffect<T>> codec(Codec<T> codec, LootContextParamSet params) {
        return RecordCodecBuilder.create(
                instance -> instance.group(
                        codec.fieldOf("effect").forGetter(ConditionalModEffect::effect),
                        ModConditions.codec(params).optionalFieldOf("requirements").forGetter(ConditionalModEffect::conditions)
                ).apply(instance, ConditionalModEffect::new)
        );
    }

    public boolean matches(LootContext context) {
        return this.conditions.map(modConditions -> modConditions.matches(context)).orElse(true);
    }
}
