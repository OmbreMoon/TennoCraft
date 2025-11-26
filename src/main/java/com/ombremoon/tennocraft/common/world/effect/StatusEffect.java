package com.ombremoon.tennocraft.common.world.effect;

import com.ombremoon.tennocraft.common.api.IModHolder;
import com.ombremoon.tennocraft.common.init.TCDamageTypes;
import com.ombremoon.tennocraft.common.world.WorldStatus;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.FastColor;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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

    public void onEffectAdded(LivingEntity livingEntity, IModHolder<?> modHolder, int amplifier) {
    }

    public void onEffectStarted(LivingEntity livingEntity, IModHolder<?> modHolder, int amplifier) {
    }

    public boolean applyEffectTick(LivingEntity livingEntity, IModHolder<?> modHolder, int amplifier) {
        return true;
    }

    public void onEffectRemoved(LivingEntity livingEntity, IModHolder<?> modHolder) {

    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }

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

    public static Proc proc(IModHolder<?> modHolder, LivingEntity attacker, @Nullable ItemStack stack, float damage, long gameTime) {
        return new Proc(modHolder, attacker, stack, damage, gameTime);
    }

    public record ProcEntries(Map<ResourceKey<DamageType>, List<Proc>> procs) {

        public void addEntry(StatusEffect effect, Proc proc) {
            var procs = this.getProcs(effect);
            if (!effect.canStackInfinitely()) {
                List<Proc> newProcs = new ArrayList<>(procs);
                newProcs.add(proc);
                if (newProcs.size() >= effect.maxStacks) {
                    newProcs.removeFirst();
                }

                this.procs.put(effect.damageProc(), newProcs);
            } else {
                List<Proc> newProcs;
                if (effect.damageProc() == TCDamageTypes.HEAT) {
                    newProcs = new ArrayList<>();
                    for (Proc proc1 : procs) {
                        Proc newProc = new Proc(proc1.modHolder, proc1.attacker, proc1.stack, proc1.damage, proc.endTime);
                        newProcs.add(newProc);
                    }
                } else {
                    newProcs = new ArrayList<>(procs);
                }

                newProcs.add(proc);
                this.procs.put(effect.damageProc(), newProcs);
            }
        }

        public void decrementProc(StatusEffect effect) {
            var procs = this.getProcs(effect);
            if (!procs.isEmpty()) {
                List<Proc> newProcs = new ArrayList<>(procs);
                newProcs.removeFirst();

                this.procs.put(effect.damageProc(), newProcs);
            }
        }

        public void tickStatusProcs(Level level, StatusEffect effect) {
            var procs = this.getProcs(effect);
            for (var proc : procs) {
                long procEndTick = proc.endTime();
                if (procEndTick <= level.getGameTime()) {
                    this.decrementProc(effect);
                }
            }
        }

        public boolean hasProcsFor(StatusEffect effect) {
            return !this.getProcs(effect).isEmpty();
        }

        public Proc getLastProc(StatusEffect effect) {
            var procs = this.getProcs(effect);
            return procs.getLast();
        }

        public Proc getOldestProc(StatusEffect effect) {
            var procs = this.getProcs(effect);
            return procs.getFirst();
        }

        public void forEachProc(StatusEffect effect, Consumer<Proc> action) {
            var procs = this.getProcs(effect);
            for (Proc proc : procs) {
                action.accept(proc);
            }
        }

        private List<Proc> getProcs(StatusEffect effect) {
            return this.procs.computeIfAbsent(effect.damageProc(), key -> new ArrayList<>());
        }

        public int getProcAmount(StatusEffect effect) {
            return this.getProcs(effect).size();
        }
    }

    public record Proc(IModHolder<?> modHolder, LivingEntity attacker, @Nullable ItemStack stack, float damage, long endTime) {}

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
