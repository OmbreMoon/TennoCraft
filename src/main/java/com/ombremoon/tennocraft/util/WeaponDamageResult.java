package com.ombremoon.tennocraft.util;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.ombremoon.tennocraft.common.api.IWeaponModHolder;
import com.ombremoon.tennocraft.common.api.handler.MeleeWeaponHandler;
import com.ombremoon.tennocraft.common.api.handler.RangedWeaponHandler;
import com.ombremoon.tennocraft.common.api.mod.ModContainer;
import com.ombremoon.tennocraft.common.api.weapon.DamageValue;
import com.ombremoon.tennocraft.common.api.weapon.TriggerType;
import com.ombremoon.tennocraft.common.api.weapon.schema.MeleeWeaponSchema;
import com.ombremoon.tennocraft.common.api.weapon.schema.RangedWeaponSchema;
import com.ombremoon.tennocraft.common.api.weapon.schema.WeaponSchema;
import com.ombremoon.tennocraft.common.init.TCData;
import com.ombremoon.tennocraft.common.init.TCTags;
import com.ombremoon.tennocraft.common.world.WorldStatus;
import com.ombremoon.tennocraft.common.world.effect.StatusEffect;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.tslat.smartbrainlib.util.RandomUtil;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public record WeaponDamageResult(DamageValue value, List<Holder<DamageType>> procs) {

    public boolean proccedEffect() {
        return !this.procs.isEmpty();
    }

    //Implement Status Effects
    //Implement Soft Combo System
    //Handle Mutlishot Damage
    //Implement Damage Falloff

    public static WeaponDamageResult calculateRanged(IWeaponModHolder<?> modHolder, ItemStack stack, LivingEntity target) {
        RangedWeaponHandler handler = stack.get(TCData.RANGED_WEAPON_HANDLER);
        if (handler != null) {
            RangedWeaponSchema schema = handler.getSchema();
            ModContainer mods = handler.getMods();
            Partial partial = quantizeAndNormalize(target, schema, handler.getTriggerType(), mods);
            return resultFromPartial(stack, target, partial, schema, handler.getTriggerType(), handler::handleBulletDamage);
        }

        return null;
    }

    public static WeaponDamageResult calculateMelee(ItemStack stack, LivingEntity target) {
        MeleeWeaponHandler handler = stack.get(TCData.MELEE_WEAPON_HANDLER);
        if (handler != null) {
            MeleeWeaponSchema schema = handler.getSchema();
            ModContainer mods = handler.getMods();
            Partial partial = quantizeAndNormalize(target, schema, null, mods);
            return resultFromPartial(stack, target, partial, schema, null, handler::handleComboModifiers);
        }

        return null;
    }

    private static WeaponDamageResult resultFromPartial(ItemStack stack, LivingEntity target, Partial partial, WeaponSchema schema, @Nullable TriggerType type, PartialOutput output) {
        output.accept(stack, target, partial);
        float f = partial.damage;

        ServerLevel level = (ServerLevel) target.level();
        float modifiedDamage = 1.0F + Math.max(0.0F, ModHelper.modifyDamage(level, stack, target, 0.0F));
        f *= modifiedDamage;

        float modifiedCritChance = schema.getModdedCritChance(level, stack, target, type);
        int tier = Mth.floor(modifiedCritChance);
        float potentialCrit = modifiedCritChance % 1.0F;
        if (potentialCrit != 0 && RandomUtil.percentChance(potentialCrit)) {
            tier++;
        }

        float modifiedCritMult = schema.getModdedCritDamage(level, stack, target, type);;
        f *= 1 + tier * (modifiedCritMult - 1);

        List<Holder<DamageType>> procs = new ArrayList<>();
        float modifiedStatus = schema.getModdedStatusChance(level, stack, target, type);
        int procCount = Mth.floor(modifiedStatus);
        float potentialProc = modifiedStatus % 1.0F;
        if (potentialProc != 0 && RandomUtil.percentChance(potentialProc)) {
            procCount++;
        }

        List<DamageValue> weightedValues = partial.distribution.toList();
        DamageValue hitValue = getWeightedDamage(weightedValues, RandomUtil.randomValueUpTo(1.0F));
        for (int i = 0; i < procCount; i++) {
            DamageValue proc = getWeightedDamage(weightedValues, RandomUtil.randomValueUpTo(1.0F));
//            Holder<DamageType> holder = level.holderOrThrow(proc.damageType());
            Holder<DamageType> holder = proc.damageType();
            procs.add(holder);
        }

        DamageValue damageValue = new DamageValue(hitValue.damageType(), f);
        return new WeaponDamageResult(damageValue, procs);
    }

    private static Partial quantizeAndNormalize(LivingEntity target, WeaponSchema schema, @Nullable TriggerType type, ModContainer mods) {
        Distribution distribution = schema.getBaseDamageDistribution(type);
        int baseDamage = schema.getBaseDamage(type);
        float scale = (float) baseDamage / 16;
        var baseValue = quantizeBaseValue(target, distribution.toList(), scale);

        List<DamageValue> weightedDamage = new ArrayList<>();
        List<DamageValue> quantizedValues = new ArrayList<>(baseValue.getFirst());
        float f = baseValue.getSecond();
        for (DamageValue modifier : mods.damageModifiers) {
            Holder<DamageType> damageType = modifier.damageType();
            ResourceKey<DamageType> damageKey = damageType.getKey();
//            ResourceKey<DamageType> damageKey = modifier.damageType();
//            Holder<DamageType> damageType = target.registryAccess().holderOrThrow(damageKey);
            float quantizedValue;
            if (distribution.contains(damageType)) {
                float baseType = distribution.getAmount(damageType);
                quantizedValue = Math.round((baseType * modifier.amount()) / scale) * scale;
            } else if (damageType.is(TCTags.DamageTypes.PHYSICAL)) {
                continue;
            } else {
                quantizedValue = Math.round((baseDamage * modifier.amount()) / scale) * scale;
            }

            float healthModifier = getHealthModifier(target, damageType);
            f += quantizedValue * healthModifier;
            quantizedValues.add(new DamageValue(modifier.damageType(), quantizedValue));
        }

        for (DamageValue value : quantizedValues) {
            float weightedValue = value.amount() / f;
            weightedDamage.add(new DamageValue(value.damageType(), weightedValue));
        }

        return new Partial(createDistribution(weightedDamage), f);
    }

    public static Pair<List<DamageValue>, Float> quantizeBaseValue(LivingEntity target, List<DamageValue> values, float scale) {
        List<DamageValue> quantizedBases = new ArrayList<>();
        Distribution distribution = createDistribution(values);
        MutableFloat mutableFloat = new MutableFloat();
        distribution.forEach((key, amount) -> {
            float healthModifier = getHealthModifier(target, key);
            float quantizedBase = Math.round(amount / scale) * scale;
            mutableFloat.add(quantizedBase * healthModifier);
            quantizedBases.add(new DamageValue(key, quantizedBase));
        });

        return Pair.of(quantizedBases, mutableFloat.floatValue());
    }

    private static float getHealthModifier(LivingEntity target, Holder<DamageType> damageType) {
        WorldStatus status = StatusEffect.getStatusFromType(damageType.getKey());
        if (target.getType().is(status.resistantToTag())) {
            return 1.5F;
        } else if (target.getType().is(status.weakToTag())) {
            return 0.5F;
        } else {
            return 1.0F;
        }
    }

    private static DamageValue getWeightedDamage(List<DamageValue> entries, float weightedIndex) {
        float weight = 0.0F;
        for (DamageValue damage : entries) {
            weight += damage.amount();
            if (weight > weightedIndex) {
                return damage;
            }
        }

        return RandomUtil.getRandomSelection(entries);
    }

    public static Distribution createDistribution(List<DamageValue> values) {
        ImmutableMap.Builder<Holder<DamageType>, Float> builder = ImmutableMap.builder();
        for (DamageValue value : values) {
            builder.put(value.damageType(), value.amount());
        }

        return new Distribution(builder.build());
    }

    public record Distribution(Map<Holder<DamageType>, Float> distribution) {

        public boolean contains(Holder<DamageType> key) {
            return this.distribution.containsKey(key);
        }

        public float getAmount(Holder<DamageType> key) {
            return this.distribution.get(key);
        }

        public void forEach(BiConsumer<Holder<DamageType>, Float> consumer) {
            this.distribution.forEach(consumer);
        }

        public List<DamageValue> toList() {
            List<DamageValue> values = new ArrayList<>();
            for (var entry : distribution.entrySet()) {
                values.add(new DamageValue(entry.getKey(), entry.getValue()));
            }

            return values;
        }
    }

    public static class Partial {
        private float damage;
        private final Distribution distribution;

        Partial(Distribution distribution, float damage) {
            this.distribution = distribution;
            this.damage = damage;
        }

        Partial(Distribution distribution) {
            this(distribution, 0.0F);
        }

        public void setDamage(float damage) {
            this.damage = damage;
        }

        public float getDamage() {
            return this.damage;
        }

        public Distribution getDistribution() {
            return this.distribution;
        }
    }

    @FunctionalInterface
    public interface PartialOutput {

        void accept(ItemStack stack, LivingEntity target, Partial partial);
    }
}
