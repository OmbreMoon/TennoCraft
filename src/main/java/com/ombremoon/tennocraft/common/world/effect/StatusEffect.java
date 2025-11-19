package com.ombremoon.tennocraft.common.world.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.init.TCDamageTypes;
import com.ombremoon.tennocraft.common.init.TCStatusEffects;
import com.ombremoon.tennocraft.common.init.TCTags;
import com.ombremoon.tennocraft.common.world.WorldStatus;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.util.FastColor;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class StatusEffect extends MobEffect {
    private static final Map<ResourceKey<DamageType>, WorldStatus> STATUS_BY_DAMAGE_TYPE;
    private final int maxStacks;
    private final int entityDuration;
    private final int playerDuration;

    public StatusEffect(MobEffectCategory category, int maxStacks, int entityDuration, int color) {
        this(category, maxStacks, entityDuration, entityDuration, color, ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, FastColor.ARGB32.color(255, color)));
    }

    public StatusEffect(MobEffectCategory category, int maxStacks, int entityDuration, int playerDuration, int color) {
        this(category, maxStacks, entityDuration, playerDuration, color, ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, FastColor.ARGB32.color(255, color)));
    }

    public StatusEffect(MobEffectCategory category, int maxStacks, int entityDuration, int playerDuration, int color, ParticleOptions particle) {
        super(category, color, particle);
        this.maxStacks = maxStacks;
        this.entityDuration = entityDuration;
        this.playerDuration = playerDuration;
    }

    public abstract ResourceKey<DamageType> damageProc();

    public boolean canStackInfinitely() {
        return this.maxStacks <= 0;
    }

    public int getEntityDuration() {
        return this.entityDuration;
    }

    public int getPlayerDuration() {
        return this.playerDuration;
    }

    public static WorldStatus getStatusFromType(ResourceKey<DamageType> damageType) {
        return STATUS_BY_DAMAGE_TYPE.getOrDefault(damageType, WorldStatus.IMPACT);
    }

    public record ProcEntries(Map<ResourceKey<DamageType>, List<Proc>> procs) {
        public static final Codec<ProcEntries> CODEC = Codec.unboundedMap(
                ResourceKey.codec(Registries.DAMAGE_TYPE), Proc.CODEC.listOf()
        ).xmap(ProcEntries::new, ProcEntries::procs);


        public void addEntry(StatusEffect effect, Proc proc) {
            var list = this.procs.computeIfAbsent(effect.damageProc(), key -> new ArrayList<>());
            if (effect.canStackInfinitely() || list.size() < effect.maxStacks) {
                list.add(proc);
            } else {
                list.set(0, proc);
            }
        }

        public List<Proc> getProcs(StatusEffect effect) {
            return this.procs.computeIfAbsent(effect.damageProc(), key -> new ArrayList<>());
        }

        public void decrementProc(StatusEffect effect) {
            var list = this.procs.computeIfAbsent(effect.damageProc(), key -> new ArrayList<>());
            if (!list.isEmpty()) {
                list.removeFirst();
            }
        }
    }

    public record Proc(ResourceKey<DamageType> damageType, float damage, long gameTime) {
        public static final Codec<Proc> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        ResourceKey.codec(Registries.DAMAGE_TYPE).fieldOf("damageType").forGetter(Proc::damageType),
                        Codec.FLOAT.fieldOf("damage").forGetter(Proc::damage),
                        Codec.LONG.fieldOf("gameTime").forGetter(Proc::gameTime)
                ).apply(instance, Proc::new)
        );
    }

    static {
        Object2ObjectOpenHashMap<ResourceKey<DamageType>, WorldStatus> statusMap = new Object2ObjectOpenHashMap<>();
        statusMap.put(TCDamageTypes.IMPACT, WorldStatus.IMPACT);
        statusMap.put(TCDamageTypes.PUNCTURE, WorldStatus.PUNCTURE);
        statusMap.put(TCDamageTypes.SLASH, WorldStatus.SLASH);
        statusMap.put(TCDamageTypes.HEAT, WorldStatus.HEAT);
        statusMap.put(TCDamageTypes.COLD, WorldStatus.COLD);
        statusMap.put(TCDamageTypes.ELECTRICITY, WorldStatus.ELECTRICITY);
        statusMap.put(TCDamageTypes.TOXIC, WorldStatus.TOXIC);
        statusMap.put(TCDamageTypes.BLAST, WorldStatus.BLAST);
        statusMap.put(TCDamageTypes.CORROSIVE, WorldStatus.CORROSIVE);
        statusMap.put(TCDamageTypes.GAS, WorldStatus.GAS);
        statusMap.put(TCDamageTypes.MAGNETIC, WorldStatus.MAGNETIC);
        statusMap.put(TCDamageTypes.RADIATION, WorldStatus.RADIATION);
        statusMap.put(TCDamageTypes.VIRAL, WorldStatus.VIRAL);
        STATUS_BY_DAMAGE_TYPE = Collections.unmodifiableMap(statusMap);
    }
}
