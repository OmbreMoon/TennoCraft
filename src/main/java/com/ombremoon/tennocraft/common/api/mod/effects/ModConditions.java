package com.ombremoon.tennocraft.common.api.mod.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.mod.AuraEffect;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Optional;

public record ModConditions(Optional<LootItemCondition> requirements, Optional<AuraEffect> auraEffect, Optional<Integer> effectDuration) {

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

    public static Codec<ModConditions> codec(LootContextParamSet params) {
        return RecordCodecBuilder.create(
                instance -> instance.group(
                        conditionCodec(params).optionalFieldOf("requirements").forGetter(ModConditions::requirements),
                        AuraEffect.CODEC.optionalFieldOf("aura").forGetter(ModConditions::auraEffect),
                        Codec.INT.optionalFieldOf("duration").forGetter(ModConditions::effectDuration)
                ).apply(instance, ModConditions::new)
        );
    }

    public boolean matches(LootContext context) {
        return this.requirements.map(lootItemCondition -> lootItemCondition.test(context)).orElse(true);
    }

    public int getDuration() {
        return this.effectDuration.orElse(0);
    }
}
