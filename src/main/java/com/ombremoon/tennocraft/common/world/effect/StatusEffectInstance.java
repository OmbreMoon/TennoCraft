package com.ombremoon.tennocraft.common.world.effect;

import com.ombremoon.tennocraft.common.init.TCData;
import com.ombremoon.tennocraft.common.init.TCTags;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class StatusEffectInstance extends MobEffectInstance {
    private final StatusEffect.Proc statusProc;
    private final ResourceKey<DamageType> damageType;
    private final StatusEffect statusEffect;

    public StatusEffectInstance(Holder<MobEffect> effect, StatusEffect.Proc statusProc) {
        this(effect, statusProc, 0);
    }

    public StatusEffectInstance(Holder<MobEffect> effect, StatusEffect.Proc statusProc, int duration) {
        this(effect, statusProc, duration, 0);
    }

    public StatusEffectInstance(Holder<MobEffect> effect, StatusEffect.Proc statusProc, int duration, int amplifier) {
        this(effect, statusProc, duration, amplifier, false, true);
    }

    public StatusEffectInstance(Holder<MobEffect> effect, StatusEffect.Proc statusProc, int duration, int amplifier, boolean ambient, boolean visible) {
        this(effect, statusProc, duration, amplifier, ambient, visible, visible);
    }

    public StatusEffectInstance(Holder<MobEffect> effect, StatusEffect.Proc statusProc, int duration, int amplifier, boolean ambient, boolean visible, boolean showIcon) {
        this(effect, statusProc, duration, amplifier, ambient, visible, showIcon, null);
    }

    public StatusEffectInstance(Holder<MobEffect> effect, StatusEffect.Proc statusProc, int duration, int amplifier, boolean ambient, boolean visible, boolean showIcon, @Nullable MobEffectInstance hiddenEffect) {
        super(effect, duration, amplifier, ambient, visible, showIcon, hiddenEffect);
        this.statusProc = statusProc;
        this.damageType = statusProc.damageType();
        if (!effect.is(TCTags.MobEffects.STATUS_EFFECT) || !(effect.value() instanceof StatusEffect))
            throw new IllegalArgumentException("Tried to create status effect instance with invalid mob effect holder: " + effect);

        this.statusEffect = (StatusEffect) effect.value();
    }

    @Override
    public void onEffectAdded(LivingEntity livingEntity) {
        super.onEffectAdded(livingEntity);
        StatusEffect.ProcEntries entries = livingEntity.getData(TCData.STATUS_PROCS);
        entries.addEntry(this.statusEffect, this.statusProc);
    }

    @Override
    public boolean tick(LivingEntity entity, Runnable onExpirationRunnable) {
        if (this.getDuration() > 0) {
            Level level = entity.level();
            if (!level.isClientSide) {
                StatusEffect.ProcEntries entries = entity.getData(TCData.STATUS_PROCS);
                var procs = entries.getProcs(this.statusEffect);
                for (StatusEffect.Proc proc : procs) {
                    long procEndTick = proc.gameTime();
                    if (procEndTick <= level.getGameTime()) {
                        entries.decrementProc(this.statusEffect);
                    }
                }
            }
        }
        return super.tick(entity, onExpirationRunnable);
    }

    public void removeStatusProc(LivingEntity livingEntity) {
        StatusEffect.ProcEntries entries = livingEntity.getData(TCData.STATUS_PROCS);
        entries.decrementProc(this.statusEffect);
    }
}
