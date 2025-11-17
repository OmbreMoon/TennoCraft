package com.ombremoon.tennocraft.util;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.ombremoon.tennocraft.common.api.handler.MeleeWeaponHandler;
import com.ombremoon.tennocraft.common.api.mod.ModContainer;
import com.ombremoon.tennocraft.common.api.weapon.DamageValue;
import com.ombremoon.tennocraft.common.api.weapon.TriggerType;
import com.ombremoon.tennocraft.common.api.weapon.schema.AttackSchema;
import com.ombremoon.tennocraft.common.api.weapon.schema.MeleeWeaponSchema;
import com.ombremoon.tennocraft.common.init.TCData;
import com.ombremoon.tennocraft.common.world.item.IModHolder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.ItemStack;
import net.tslat.smartbrainlib.util.RandomUtil;
import org.apache.commons.lang3.mutable.MutableFloat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public record DamageResult(DamageValue value, List<ResourceKey<DamageType>> procs) {

    public boolean proccedEffect() {
        return !this.procs.isEmpty();
    }

    public static DamageResult calculateRangedResult(IModHolder<?> modHolder, ItemStack stack, TriggerType triggerType) {

    }

    public static DamageResult calculateMeleeResult(IModHolder<?> modHolder, ItemStack stack) {
        MeleeWeaponHandler handler = stack.get(TCData.MELEE_WEAPON_HANDLER);
        if (handler != null) {
            MeleeWeaponSchema schema = handler.getSchema();
            AttackSchema attacks = schema.getAttacks().attack();
            int baseDamage = schema.getBaseDamage();
            float scale = baseDamage / 16.0F;
            float critChance = attacks.getCritChance();
            float critMult = attacks.getCritMultiplier();
            float status = attacks.getStatus();
            Distribution distribution = Distribution.createDistribution(attacks.getDamage());
            Partial partial = quantizeAndNormalize(distribution, baseDamage, scale, modHolder.getMods(stack));
            return resultFromPartial(stack, partial, critChance, critMult, status, handler::handleComboModifiers);
        }

        return null;
    }

    public static DamageResult calculateResult(IModHolder<?> modHolder) {

    }

    private static DamageResult resultFromPartial(ItemStack stack, Partial partial, float critChance, float critMult, float status, PartialOutput output) {
        output.accept(stack, partial);
        float f = partial.damage;

        float modifiedDamage = 1.0F; // Modded Bonus/Faction Damage
        f *= modifiedDamage;

        float modifiedCritChance = Math.max(0.0F, critChance); // Modded Crit Chance
        int tier = Mth.floor(modifiedCritChance);
        float potentialCrit = modifiedCritChance % 1.0F;
        if ((potentialCrit == 0 && modifiedCritChance != 0) || RandomUtil.percentChance(critChance)) {
            tier++;
        }

        float modifiedCritMult = Math.max(0.0F, critMult); // Modded Crit Mult
        f *= 1 + tier * (modifiedCritMult - 1);

        List<ResourceKey<DamageType>> procs = new ArrayList<>();
        float modifiedStatus = Math.max(0.0F, status); //Modded Status Chance;
        int procCount = Mth.floor(modifiedStatus);
        float potentialProc = modifiedStatus % 1.0F;
        if ((potentialProc == 0 && modifiedStatus != 0) || RandomUtil.percentChance(modifiedStatus)) {
            procCount++;
        }

        List<DamageValue> weightedValues = partial.distribution.toList();
        DamageValue hitValue = getWeightedDamage(weightedValues, RandomUtil.randomValueUpTo(1.0F));
        for (int i = 0; i < procCount; i++) {
            DamageValue proc = getWeightedDamage(weightedValues, RandomUtil.randomValueUpTo(1.0F));
            procs.add(proc.damageType());
        }

        DamageValue damageValue = new DamageValue(hitValue.damageType(), f);
        return new DamageResult(damageValue, procs);
    }

    private static Partial quantizeAndNormalize(Distribution distribution, int baseDamage, float scale, ModContainer mods) {
        var baseValue = calculateQuantizedBaseValue(distribution, scale);
        List<DamageValue> weightedDamage = new ArrayList<>();
        List<DamageValue> quantizedValues = new ArrayList<>(baseValue.getFirst());
        float f = baseValue.getSecond();
        for (DamageValue modifier : mods.damageModifiers) {
            ResourceKey<DamageType> damageType = modifier.damageType();
            float quantizedValue;
            if (distribution.contains(damageType)) {
                float baseType = distribution.getAmount(damageType);
                quantizedValue = Math.round((baseType * modifier.amount()) / scale) * scale;
            } else {
                quantizedValue = Math.round((baseDamage * modifier.amount()) / scale) * scale;
            }

            f += quantizedValue;
            quantizedValues.add(new DamageValue(modifier.damageType(), quantizedValue));
        }

        for (DamageValue value : quantizedValues) {
            float weightedValue = value.amount() / f;
            weightedDamage.add(new DamageValue(value.damageType(), weightedValue));
        }

        return new Partial(f, Distribution.createDistribution(weightedDamage));
    }

    private static Pair<List<DamageValue>, Float> calculateQuantizedBaseValue(Distribution distribution, float scale) {
        List<DamageValue> quantizedBases = new ArrayList<>();
        MutableFloat mutableFloat = new MutableFloat();
        distribution.forEach((key, amount) -> {
            float quantizedBase = Math.round(amount / scale) * scale;
            mutableFloat.add(quantizedBase);
            quantizedBases.add(new DamageValue(key, quantizedBase));
        });

        return Pair.of(quantizedBases, mutableFloat.floatValue());
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

    record Distribution(Map<ResourceKey<DamageType>, Float> distribution) {

        public boolean contains(ResourceKey<DamageType> key) {
            return this.distribution.containsKey(key);
        }

        public float getAmount(ResourceKey<DamageType> key) {
            return this.distribution.get(key);
        }

        public void forEach(BiConsumer<ResourceKey<DamageType>, Float> consumer) {
            this.distribution.forEach(consumer);
        }

        public List<DamageValue> toList() {
            List<DamageValue> values = new ArrayList<>();
            for (var entry : distribution.entrySet()) {
                values.add(new DamageValue(entry.getKey(), entry.getValue()));
            }

            return values;
        }

        public static Distribution createDistribution(List<DamageValue> values) {
            ImmutableMap.Builder<ResourceKey<DamageType>, Float> builder = ImmutableMap.builder();
            for (DamageValue value : values) {
                builder.put(value.damageType(), value.amount());
            }

            return new Distribution(builder.build());
        }
    }

    public static class Partial {
        private float damage;
        private final Distribution distribution;

        Partial(float damage, Distribution distribution) {
            this.damage = damage;
            this.distribution = distribution;
        }

        public void setDamage(float damage) {
            this.damage = damage;
        }
    }

    @FunctionalInterface
    public interface PartialOutput {

        void accept(ItemStack stack, Partial partial);
    }
}
