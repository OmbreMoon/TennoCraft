package com.ombremoon.tennocraft.common.world;

import com.ombremoon.tennocraft.common.api.mod.ConditionalModEffect;
import com.ombremoon.tennocraft.common.init.TCModEffectComponents;
import com.ombremoon.tennocraft.common.api.mod.effects.ModValueEffect;
import com.ombremoon.tennocraft.common.init.TCDamageTypes;
import com.ombremoon.tennocraft.common.init.TCStatusEffects;
import com.ombremoon.tennocraft.common.init.TCTags;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;

import java.util.List;
import java.util.function.Supplier;

public enum WorldStatus {
    IMPACT(0, TCDamageTypes.IMPACT, TCModEffectComponents.IMPACT, TCStatusEffects.KNOCKBACK, TCTags.EntityTypes.WEAK_TO_IMPACT, TCTags.EntityTypes.RESISTANT_TO_IMPACT, TCTags.EntityTypes.IMMUNE_TO_KNOCKBACK),
    PUNCTURE(0, TCDamageTypes.PUNCTURE, TCModEffectComponents.PUNCTURE, TCStatusEffects.WEAKENED, TCTags.EntityTypes.WEAK_TO_PUNCTURE, TCTags.EntityTypes.RESISTANT_TO_PUNCTURE, TCTags.EntityTypes.IMMUNE_TO_KNOCKBACK),
    SLASH(0, TCDamageTypes.SLASH, TCModEffectComponents.SLASH, TCStatusEffects.BLEED, TCTags.EntityTypes.WEAK_TO_SLASH, TCTags.EntityTypes.RESISTANT_TO_SLASH, TCTags.EntityTypes.IMMUNE_TO_BLEED),
    HEAT(1, TCDamageTypes.HEAT, TCModEffectComponents.HEAT, TCStatusEffects.IGNITE, TCTags.EntityTypes.WEAK_TO_HEAT, TCTags.EntityTypes.RESISTANT_TO_HEAT, TCTags.EntityTypes.IMMUNE_TO_IGNITE),
    COLD(2, TCDamageTypes.COLD, TCModEffectComponents.COLD, TCStatusEffects.FREEZE, TCTags.EntityTypes.WEAK_TO_COLD, TCTags.EntityTypes.RESISTANT_TO_COLD, TCTags.EntityTypes.IMMUNE_TO_FREEZE),
    ELECTRICITY(4, TCDamageTypes.ELECTRICITY, TCModEffectComponents.ELECTRICITY, TCStatusEffects.TESLA_CHAIN, TCTags.EntityTypes.WEAK_TO_ELECTRICITY, TCTags.EntityTypes.RESISTANT_TO_ELECTRICITY, TCTags.EntityTypes.IMMUNE_TO_TESLA_CHAIN),
    TOXIC(8, TCDamageTypes.TOXIC, TCModEffectComponents.TOXIC, TCStatusEffects.POISON, TCTags.EntityTypes.WEAK_TO_TOXIC, TCTags.EntityTypes.RESISTANT_TO_TOXIC, TCTags.EntityTypes.IMMUNE_TO_POISON),
    BLAST(3, TCDamageTypes.BLAST, TCModEffectComponents.BLAST, TCStatusEffects.DETONATE, TCTags.EntityTypes.WEAK_TO_BLAST, TCTags.EntityTypes.RESISTANT_TO_BLAST, TCTags.EntityTypes.IMMUNE_TO_DETONATE),
    CORROSIVE(12, TCDamageTypes.CORROSIVE, TCModEffectComponents.CORROSIVE, TCStatusEffects.CORROSION, TCTags.EntityTypes.WEAK_TO_CORROSIVE, TCTags.EntityTypes.RESISTANT_TO_CORROSIVE, TCTags.EntityTypes.IMMUNE_TO_CORROSION),
    MAGNETIC(6, TCDamageTypes.MAGNETIC, TCModEffectComponents.MAGNETIC, TCStatusEffects.DISRUPT, TCTags.EntityTypes.WEAK_TO_MAGNETIC, TCTags.EntityTypes.RESISTANT_TO_MAGNETIC, TCTags.EntityTypes.IMMUNE_TO_DISRUPT),
    GAS(9, TCDamageTypes.GAS, TCModEffectComponents.GAS, TCStatusEffects.GAS_CLOUD, TCTags.EntityTypes.WEAK_TO_GAS, TCTags.EntityTypes.RESISTANT_TO_GAS, TCTags.EntityTypes.IMMUNE_TO_GAS_CLOUD),
    RADIATION(5, TCDamageTypes.RADIATION, TCModEffectComponents.RADIATION, TCStatusEffects.CONFUSION, TCTags.EntityTypes.WEAK_TO_RADIATION, TCTags.EntityTypes.RESISTANT_TO_RADIATION, TCTags.EntityTypes.IMMUNE_TO_CONFUSION),
    VIRAL(10, TCDamageTypes.VIRAL, TCModEffectComponents.VIRAL, TCStatusEffects.VIRUS, TCTags.EntityTypes.WEAK_TO_VIRAL, TCTags.EntityTypes.RESISTANT_TO_VIRAL, TCTags.EntityTypes.IMMUNE_TO_VIRUS);

    public static final Int2ObjectArrayMap<WorldStatus> BY_FLAG = new Int2ObjectArrayMap<>();
    private final int flag;
    private final ResourceKey<DamageType> damageType;
    private final Supplier<DataComponentType<List<ConditionalModEffect<ModValueEffect>>>> dataComponentType;
    private final Holder<MobEffect> statusEffect;
    private final TagKey<EntityType<?>> weakToTag;
    private final TagKey<EntityType<?>> resistantToTag;
    private final TagKey<EntityType<?>> immuneToTag;

    WorldStatus(
            int flag,
            ResourceKey<DamageType> damageType,
            Supplier<DataComponentType<List<ConditionalModEffect<ModValueEffect>>>> dataComponentType,
            Holder<MobEffect> statusEffect,
            TagKey<EntityType<?>> weakToTag,
            TagKey<EntityType<?>> resistantToTag,
            TagKey<EntityType<?>> immuneToTag) {
        this.flag = flag;
        this.damageType = damageType;
        this.dataComponentType = dataComponentType;
        this.statusEffect = statusEffect;
        this.weakToTag = weakToTag;
        this.resistantToTag = resistantToTag;
        this.immuneToTag = immuneToTag;
    }

    public int flag() {
        return this.flag;
    }

    public ResourceKey<DamageType> getDamageType() {
        return this.damageType;
    }

    public Supplier<DataComponentType<List<ConditionalModEffect<ModValueEffect>>>> getDataComponentType() {
        return this.dataComponentType;
    }

    public Holder<MobEffect> statusEffect() {
        return this.statusEffect;
    }

    public TagKey<EntityType<?>> weakToTag() {
        return this.weakToTag;
    }

    public TagKey<EntityType<?>> resistantToTag() {
        return this.resistantToTag;
    }

    public TagKey<EntityType<?>> immuneToTag() {
        return this.immuneToTag;
    }

    public static WorldStatus byFlag(int flag) {
        return BY_FLAG.get(flag);
    }

    static {
        for (WorldStatus status : WorldStatus.values()) {
            BY_FLAG.put(status.flag, status);
        }
    }
}
