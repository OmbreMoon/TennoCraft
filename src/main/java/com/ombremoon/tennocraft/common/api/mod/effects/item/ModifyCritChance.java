package com.ombremoon.tennocraft.common.api.mod.effects.item;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.mod.AuraEffect;
import com.ombremoon.tennocraft.common.api.mod.ConditionalModEffect;
import com.ombremoon.tennocraft.common.api.mod.effects.ModConditions;
import com.ombremoon.tennocraft.common.api.mod.effects.ModValueEffect;
import com.ombremoon.tennocraft.common.api.mod.effects.ModifyItemEffect;
import com.ombremoon.tennocraft.common.api.mod.effects.value.AddValue;
import com.ombremoon.tennocraft.common.init.TCModEffectComponents;
import com.ombremoon.tennocraft.common.world.level.loot.ModContextParams;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;
import java.util.Optional;

public record ModifyCritChance(ModValueEffect value, Optional<ModConditions> conditions, ResourceLocation id) implements ModifyItemEffect {
    public static final MapCodec<ModifyCritChance> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    ModValueEffect.CODEC.fieldOf("value").forGetter(ModifyCritChance::value),
                    ModConditions.codec(ModContextParams.MODDED_ATTACK).optionalFieldOf("conditions").forGetter(ModifyCritChance::conditions),
                    ResourceLocation.CODEC.fieldOf("id").forGetter(ModifyCritChance::id)
            ).apply(instance, ModifyCritChance::new));

    static ModifyCritChance modifyCrit(ResourceLocation id, ModValueEffect effect) {
        return new ModifyCritChance(effect, Optional.empty(), id);
    }

    static ModifyCritChance modifyCrit(ResourceLocation id, ModValueEffect effect, LootItemCondition.Builder requirements) {
        ModConditions conditions = new ModConditions(Optional.of(requirements.build()), Optional.empty(), Optional.empty());
        return new ModifyCritChance(effect, Optional.of(conditions), id);
    }

    static ModifyCritChance modifyCrit(ResourceLocation id, ModValueEffect effect, LootItemCondition.Builder requirements, AuraEffect auraEffect) {
        ModConditions conditions = new ModConditions(Optional.of(requirements.build()), Optional.of(auraEffect), Optional.empty());
        return new ModifyCritChance(effect, Optional.of(conditions), id);
    }

    static ModifyCritChance modifyCrit(ResourceLocation id, ModValueEffect effect, LootItemCondition.Builder requirements, int duration) {
        ModConditions conditions = new ModConditions(Optional.of(requirements.build()), Optional.empty(), Optional.of(duration));
        return new ModifyCritChance(effect, Optional.of(conditions), id);
    }

    static ModifyCritChance modifyCrit(ResourceLocation id, ModValueEffect effect, AuraEffect auraEffect, int duration) {
        ModConditions conditions = new ModConditions(Optional.empty(), Optional.of(auraEffect), Optional.of(duration));
        return new ModifyCritChance(effect, Optional.of(conditions), id);
    }

    static ModifyCritChance modifyCrit(ResourceLocation id, ModValueEffect effect, LootItemCondition.Builder requirements, AuraEffect auraEffect, int duration) {
        ModConditions conditions = new ModConditions(Optional.of(requirements.build()), Optional.of(auraEffect), Optional.of(duration));
        return new ModifyCritChance(effect, Optional.of(conditions), id);
    }

    @Override
    public DataComponentType<List<ConditionalModEffect<ModValueEffect>>> withComponent() {
        return TCModEffectComponents.CRIT_CHANCE.get();
    }

    @Override
    public MapCodec<? extends ModifyItemEffect> codec() {
        return CODEC;
    }
}
