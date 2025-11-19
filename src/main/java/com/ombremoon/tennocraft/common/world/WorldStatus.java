package com.ombremoon.tennocraft.common.world;

import com.ombremoon.tennocraft.common.init.TCStatusEffects;
import com.ombremoon.tennocraft.common.init.TCTags;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;

public enum WorldStatus {
    IMPACT(0, TCStatusEffects.KNOCKBACK, TCTags.EntityTypes.WEAK_TO_IMPACT, TCTags.EntityTypes.RESISTANT_TO_IMPACT, TCTags.EntityTypes.IMMUNE_TO_KNOCKBACK),
    PUNCTURE(0, TCStatusEffects.WEAKENED, TCTags.EntityTypes.WEAK_TO_PUNCTURE, TCTags.EntityTypes.RESISTANT_TO_PUNCTURE, TCTags.EntityTypes.IMMUNE_TO_KNOCKBACK),
    SLASH(0, TCStatusEffects.BLEED, TCTags.EntityTypes.WEAK_TO_SLASH, TCTags.EntityTypes.RESISTANT_TO_SLASH, TCTags.EntityTypes.IMMUNE_TO_BLEED),
    HEAT(1, TCStatusEffects.IGNITE, TCTags.EntityTypes.WEAK_TO_HEAT, TCTags.EntityTypes.RESISTANT_TO_HEAT, TCTags.EntityTypes.IMMUNE_TO_IGNITE),
    COLD(2, TCStatusEffects.FREEZE, TCTags.EntityTypes.WEAK_TO_COLD, TCTags.EntityTypes.RESISTANT_TO_COLD, TCTags.EntityTypes.IMMUNE_TO_FREEZE),
    ELECTRICITY(4, TCStatusEffects.TESLA_CHAIN, TCTags.EntityTypes.WEAK_TO_ELECTRICITY, TCTags.EntityTypes.RESISTANT_TO_ELECTRICITY, TCTags.EntityTypes.IMMUNE_TO_TESLA_CHAIN),
    TOXIC(8, TCStatusEffects.POISON, TCTags.EntityTypes.WEAK_TO_TOXIC, TCTags.EntityTypes.RESISTANT_TO_TOXIC, TCTags.EntityTypes.IMMUNE_TO_POISON),
    BLAST(3, TCStatusEffects.DETONATE, TCTags.EntityTypes.WEAK_TO_BLAST, TCTags.EntityTypes.RESISTANT_TO_BLAST, TCTags.EntityTypes.IMMUNE_TO_DETONATE),
    CORROSIVE(12, TCStatusEffects.CORROSION, TCTags.EntityTypes.WEAK_TO_CORROSIVE, TCTags.EntityTypes.RESISTANT_TO_CORROSIVE, TCTags.EntityTypes.IMMUNE_TO_CORROSION),
    GAS(9, TCStatusEffects.DISRUPT, TCTags.EntityTypes.WEAK_TO_MAGNETIC, TCTags.EntityTypes.RESISTANT_TO_MAGNETIC, TCTags.EntityTypes.IMMUNE_TO_DISRUPT),
    MAGNETIC(6, TCStatusEffects.GAS_CLOUD, TCTags.EntityTypes.WEAK_TO_GAS, TCTags.EntityTypes.RESISTANT_TO_GAS, TCTags.EntityTypes.IMMUNE_TO_GAS_CLOUD),
    RADIATION(5, TCStatusEffects.CONFUSION, TCTags.EntityTypes.WEAK_TO_RADIATION, TCTags.EntityTypes.RESISTANT_TO_RADIATION, TCTags.EntityTypes.IMMUNE_TO_CONFUSION),
    VIRAL(10, TCStatusEffects.VIRUS, TCTags.EntityTypes.WEAK_TO_VIRAL, TCTags.EntityTypes.RESISTANT_TO_VIRAL, TCTags.EntityTypes.IMMUNE_TO_VIRUS);

    private final int flag;
    private final Holder<MobEffect> statusEffect;
    private final TagKey<EntityType<?>> weakToTag;
    private final TagKey<EntityType<?>> resistantToTag;
    private final TagKey<EntityType<?>> immuneToTag;

    WorldStatus(int flag, Holder<MobEffect> statusEffect, TagKey<EntityType<?>> weakToTag, TagKey<EntityType<?>> resistantToTag, TagKey<EntityType<?>> immuneToTag) {
        this.flag = flag;
        this.statusEffect = statusEffect;
        this.weakToTag = weakToTag;
        this.resistantToTag = resistantToTag;
        this.immuneToTag = immuneToTag;
    }

    public int flag() {
        return this.flag;
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
}
