package com.ombremoon.tennocraft.common.init.entity;

import com.ombremoon.tennocraft.TennoCraft;
import com.ombremoon.tennocraft.object.entity.effects.ImpactEffect;
import com.ombremoon.tennocraft.object.world.StatusEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TCMobEffects {
    public static DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, TennoCraft.MOD_ID);

    public static final RegistryObject<MobEffect> IMPACT = MOB_EFFECTS.register("impact", () -> new ImpactEffect(MobEffectCategory.HARMFUL, 7561558));

    public static void register(IEventBus modEventBus) {
        MOB_EFFECTS.register(modEventBus);
    }
}
