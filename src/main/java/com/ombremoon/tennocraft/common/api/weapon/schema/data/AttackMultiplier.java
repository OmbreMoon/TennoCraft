package com.ombremoon.tennocraft.common.api.weapon.schema.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.weapon.DamageValue;
import com.ombremoon.tennocraft.common.init.TCTags;
import com.ombremoon.tennocraft.util.WeaponDamageResult;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffect;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AttackMultiplier {
    public static final Codec<AttackMultiplier> CODEC = RecordCodecBuilder.<AttackMultiplier>create(
            instance -> instance.group(
                    Codec.FLOAT.fieldOf("multiplier").forGetter(AttackMultiplier::getMultiplier),
                    Codec.FLOAT.optionalFieldOf("status").forGetter(AttackMultiplier::getBonusStatus),
                    DamageValue.CODEC.codec().listOf().optionalFieldOf("modifiers").forGetter(AttackMultiplier::getPhysicalModifiers),
                    BuiltInRegistries.MOB_EFFECT.holderByNameCodec().listOf().optionalFieldOf("forcedProcs").forGetter(AttackMultiplier::getForcedProcs)
            ).apply(instance, AttackMultiplier::new)
    ).validate(AttackMultiplier::validate);
    public static final StreamCodec<RegistryFriendlyByteBuf, AttackMultiplier> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, AttackMultiplier::getMultiplier,
            ByteBufCodecs.optional(ByteBufCodecs.FLOAT), AttackMultiplier::getBonusStatus,
            ByteBufCodecs.optional(DamageValue.STREAM_CODEC.apply(ByteBufCodecs.list())), AttackMultiplier::getPhysicalModifiers,
            ByteBufCodecs.optional(ByteBufCodecs.holderRegistry(Registries.MOB_EFFECT).apply(ByteBufCodecs.list())), AttackMultiplier::getForcedProcs,
            AttackMultiplier::new
    );

    private static DataResult<AttackMultiplier> validate(AttackMultiplier multiplier) {
        if (multiplier.physicalModifiers.isPresent()) {
            List<DamageValue> list = multiplier.physicalModifiers.get().stream().filter(value -> !value.damageType().is(TCTags.DamageTypes.PHYSICAL)).toList();
            if (!list.isEmpty()) {
                return DataResult.error(() -> "Encountered modifier with non-physical damage projectileType: " + list);
            }
        }

        if (multiplier.forcedProcs.isPresent()) {
            List<Holder<MobEffect>> list = multiplier.forcedProcs.get().stream().filter(effect -> !effect.is(TCTags.MobEffects.APPLICABLE_FORCED_PROC)).toList();
            if (!list.isEmpty()) {
                return DataResult.error(() -> "Defined attack multiplier with inapplicable forced proc: " + list);
            }
        }
        return DataResult.success(multiplier);
    }

    private final float multiplier;
    private final Optional<List<DamageValue>> physicalModifiers;
    private final Optional<Float> bonusStatus;
    private final Optional<List<Holder<MobEffect>>> forcedProcs;
    private WeaponDamageResult.Distribution distribution;

    public AttackMultiplier(float multiplier, Optional<Float> bonusStatus, Optional<List<DamageValue>> physicalModifiers, Optional<List<Holder<MobEffect>>> forcedProcs) {
        this.multiplier = multiplier;
        this.bonusStatus = bonusStatus;
        this.physicalModifiers = physicalModifiers;
        this.forcedProcs = forcedProcs;

        physicalModifiers.ifPresent(values -> this.distribution = WeaponDamageResult.createDistribution(values));
    }

    public AttackMultiplier withModifier(DamageValue... values) {
        return new AttackMultiplier(this.multiplier, this.bonusStatus, Optional.of(Arrays.asList(values)), this.forcedProcs);
    }

    @SuppressWarnings("unchecked")
    public AttackMultiplier withProc(Holder<MobEffect>... effects) {
        return new AttackMultiplier(this.multiplier, this.bonusStatus, this.physicalModifiers, Optional.of(Arrays.asList(effects)));
    }

    public WeaponDamageResult.Distribution getDistribution() {
        return this.distribution;
    }

    public float getMultiplier() {
        return this.multiplier;
    }

    public Optional<Float> getBonusStatus() {
        return this.bonusStatus;
    }

    public Optional<List<DamageValue>> getPhysicalModifiers() {
        return this.physicalModifiers;
    }

    public Optional<List<Holder<MobEffect>>> getForcedProcs() {
        return this.forcedProcs;
    }
}
