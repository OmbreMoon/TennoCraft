package com.ombremoon.tennocraft.common.modholder.api.mod;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Optional;

public record ConditionalModEffect<T>(T effect, Optional<LootItemCondition> requirements, Optional<AuraEffect> auraEffect, Optional<Integer> effectDuration) {

    public static Codec<LootItemCondition> conditionCodec(LootContextParamSet params) {
        return LootItemCondition.DIRECT_CODEC
                .validate(
                        lootItemCondition -> {
                            ProblemReporter.Collector problemreporter$collector = new ProblemReporter.Collector();
                            ValidationContext validationcontext = new ValidationContext(problemreporter$collector, params);
                            lootItemCondition.validate(validationcontext);
                            return problemreporter$collector.getReport()
                                    .map(p_344978_ -> DataResult.<LootItemCondition>error(() -> "Validation error in mod effect condition: " + p_344978_))
                                    .orElseGet(() -> DataResult.success(lootItemCondition));
                        }
                );
    }

    public static <T>Codec<ConditionalModEffect<T>> codec(Codec<T> codec, LootContextParamSet params) {
        return RecordCodecBuilder.create(
                instance -> instance.group(
                        codec.fieldOf("effect").forGetter(ConditionalModEffect::effect),
                        conditionCodec(params).optionalFieldOf("requirements").forGetter(ConditionalModEffect::requirements),
                        AuraEffect.CODEC.optionalFieldOf("aura").forGetter(ConditionalModEffect::auraEffect),
                        Codec.INT.optionalFieldOf("duration").forGetter(ConditionalModEffect::effectDuration)
                ).apply(instance, ConditionalModEffect::new)
        );
    }

    public boolean matches(LootContext context) {
        return this.requirements.map(lootItemCondition -> lootItemCondition.test(context)).orElse(true);
    }
}
