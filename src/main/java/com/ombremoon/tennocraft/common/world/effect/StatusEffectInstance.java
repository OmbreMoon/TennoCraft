package com.ombremoon.tennocraft.common.world.effect;

import com.ombremoon.tennocraft.common.api.IModHolder;
import com.ombremoon.tennocraft.common.init.TCData;
import com.ombremoon.tennocraft.common.init.TCTags;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class StatusEffectInstance extends MobEffectInstance {
    private final StatusEffect.Proc statusProc;
    private final IModHolder<?> modHolder;
    private final StatusEffect statusEffect;

    public StatusEffectInstance(Holder<MobEffect> effect, StatusEffect.Proc statusProc, IModHolder<?> modHolder) {
        this(effect, statusProc, modHolder, 0);
    }

    public StatusEffectInstance(Holder<MobEffect> effect, StatusEffect.Proc statusProc, IModHolder<?> modHolder, int duration) {
        this(effect, statusProc, modHolder, duration, 0);
    }

    public StatusEffectInstance(Holder<MobEffect> effect, StatusEffect.Proc statusProc, IModHolder<?> modHolder, int duration, int amplifier) {
        this(effect, statusProc, modHolder, duration, amplifier, false, true);
    }

    public StatusEffectInstance(Holder<MobEffect> effect, StatusEffect.Proc statusProc, IModHolder<?> modHolder, int duration, int amplifier, boolean ambient, boolean visible) {
        this(effect, statusProc, modHolder, duration, amplifier, ambient, visible, visible);
    }

    public StatusEffectInstance(Holder<MobEffect> effect, StatusEffect.Proc statusProc, IModHolder<?> modHolder, int duration, int amplifier, boolean ambient, boolean visible, boolean showIcon) {
        this(effect, statusProc, modHolder, duration, amplifier, ambient, visible, showIcon, null);
    }

    public StatusEffectInstance(Holder<MobEffect> effect, StatusEffect.Proc statusProc, IModHolder<?> modHolder, int duration, int amplifier, boolean ambient, boolean visible, boolean showIcon, @Nullable MobEffectInstance hiddenEffect) {
        super(effect, duration, amplifier, ambient, visible, showIcon, hiddenEffect);
        this.statusProc = statusProc;
        this.modHolder = modHolder;
        if (!effect.is(TCTags.MobEffects.STATUS_EFFECT) || !(effect.value() instanceof StatusEffect))
            throw new IllegalArgumentException("Tried to create status effect instance with invalid mob effect holder: " + effect);

        this.statusEffect = (StatusEffect) effect.value();
    }

    @Override
    public void onEffectAdded(LivingEntity livingEntity) {
        this.statusEffect.onEffectAdded(livingEntity, this.modHolder, this.amplifier);
        StatusEffect.ProcEntries entries = livingEntity.getData(TCData.STATUS_PROCS);
        entries.addEntry(this.statusEffect, this.statusProc);
    }

    @Override
    public void onEffectStarted(LivingEntity entity) {
        this.statusEffect.onEffectStarted(entity, this.modHolder, this.amplifier);
    }

    public void onEffectRemoved(LivingEntity livingEntity) {
        this.statusEffect.onEffectRemoved(livingEntity, this.modHolder);
    }

    @Override
    public boolean tick(LivingEntity entity, Runnable onExpirationRunnable) {
        if (this.hasRemainingDuration()) {
            int i = this.isInfiniteDuration() ? entity.tickCount : this.duration;
            if (this.statusEffect.shouldApplyEffectTickThisTick(i, this.amplifier) && !this.statusEffect.applyEffectTick(entity, this.modHolder, this.amplifier)) {
                entity.removeEffect(this.getEffect());
            }


            StatusEffect.ProcEntries entries = entity.getData(TCData.STATUS_PROCS);
            entries.tickStatusProcs(entity.level(), this.statusEffect);
            this.tickDownDuration();
            if (this.duration == 0 && this.hiddenEffect != null) {
                this.setDetailsFrom(this.hiddenEffect);
                this.hiddenEffect = this.hiddenEffect.hiddenEffect;
                onExpirationRunnable.run();
            }
        }

        this.blendState.tick(this);
        return this.hasRemainingDuration();
    }

    public void removeStatusProc(LivingEntity livingEntity) {
        StatusEffect.ProcEntries entries = livingEntity.getData(TCData.STATUS_PROCS);
        entries.decrementProc(this.statusEffect);
    }
}
