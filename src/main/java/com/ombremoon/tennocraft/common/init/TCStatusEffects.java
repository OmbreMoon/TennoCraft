package com.ombremoon.tennocraft.common.init;

import com.ombremoon.tennocraft.common.world.effect.*;
import com.ombremoon.tennocraft.main.Constants;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TCStatusEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, Constants.MOD_ID);

    public static final Holder<MobEffect> KNOCKBACK = MOB_EFFECTS.register("knockback", () -> new KnockbackEffect(MobEffectCategory.HARMFUL, 5, 20, 9154528));
    public static final Holder<MobEffect> WEAKENED = MOB_EFFECTS.register("weakened", () -> new WeakenedEffect(MobEffectCategory.HARMFUL, 200, 20, 9154528));
    public static final Holder<MobEffect> BLEED = MOB_EFFECTS.register("bleed", () -> new BleedEffect(MobEffectCategory.HARMFUL, 0, 120, 9154528));
    public static final Holder<MobEffect> IGNITE = MOB_EFFECTS.register("ignite", () -> new IgniteEffect(MobEffectCategory.HARMFUL, 0, 20, 9154528));
    public static final Holder<MobEffect> FREEZE = MOB_EFFECTS.register("freeze", () -> new FreezeEffect(MobEffectCategory.HARMFUL, 10, 20, 9154528));
    public static final Holder<MobEffect> TESLA_CHAIN = MOB_EFFECTS.register("tesla_chain", () -> new TeslaChainEffect(MobEffectCategory.HARMFUL, 0, 20, 9154528));
    public static final Holder<MobEffect> POISON = MOB_EFFECTS.register("poison", () -> new PoisonEffect(MobEffectCategory.HARMFUL, 0, 20, 9154528));
    public static final Holder<MobEffect> DETONATE = MOB_EFFECTS.register("detonate", () -> new DetonateEffect(MobEffectCategory.HARMFUL, 10, 20, 9154528));
    public static final Holder<MobEffect> CORROSION = MOB_EFFECTS.register("corrosion", () -> new CorrosionEffect(MobEffectCategory.HARMFUL, 10, 20, 9154528));
    public static final Holder<MobEffect> GAS_CLOUD = MOB_EFFECTS.register("gas_cloud", () -> new GasCloudEffect(MobEffectCategory.HARMFUL, 10, 20, 9154528));
    public static final Holder<MobEffect> DISRUPT = MOB_EFFECTS.register("disrupt", () -> new DisruptEffect(MobEffectCategory.HARMFUL, 10, 20, 9154528));
    public static final Holder<MobEffect> CONFUSION = MOB_EFFECTS.register("confusion", () -> new ConfusionEffect(MobEffectCategory.HARMFUL, 10, 20, 9154528));
    public static final Holder<MobEffect> VIRUS = MOB_EFFECTS.register("virus", () -> new VirusEffect(MobEffectCategory.HARMFUL, 10, 20, 9154528));

    public static void register(IEventBus modEventBus) {
        MOB_EFFECTS.register(modEventBus);
    }
}
