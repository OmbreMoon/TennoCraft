package com.ombremoon.tennocraft.common.init;

import com.ombremoon.tennocraft.main.Constants;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TCAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE, Constants.MOD_ID);

    public static final Holder<Attribute> MAX_SHIELD = register("max_shield", 100.0, 0.0, 10000.0);
    public static final Holder<Attribute> MAX_ENERGY = register("max_energy", 100.0, 0.0, 10000.0);
    public static final Holder<Attribute> SPRINT_SPEED = register("sprint_speed", 1.0, 0.0, 3.0);
    public static final Holder<Attribute> ABILITY_DURATION = register("ability_duration", 1.0, -20.0, 20.0);
    public static final Holder<Attribute> ABILITY_EFFICIENCY = register("ability_efficiency", 1.0, -20.0, 20.0);
    public static final Holder<Attribute> ABILITY_RANGE = register("ability_range", 1.0, -20.0, 20.0);
    public static final Holder<Attribute> ABILITY_STRENGTH = register("ability_strength", 1.0, -20.0, 20.0);

    public static final Holder<Attribute> CRIT_CHANCE = register("crit_chance", 1.0, -6.0, 6.0);

    public static final Holder<Attribute> IMPACT_DAMAGE = register("impact_damage", 1.0, 0, 1024.0);
    public static final Holder<Attribute> PUNCTURE_DAMAGE = register("puncture_damage", 1.0, 0, 1024.0);
    public static final Holder<Attribute> SLASH_DAMAGE = register("slash_damage", 1.0, 0, 1024.0);
    public static final Holder<Attribute> HEAT_DAMAGE = register("heat_damage", 1.0, 0, 1024.0);
    public static final Holder<Attribute> COLD_DAMAGE = register("cold_damage", 1.0, 0, 1024.0);
    public static final Holder<Attribute> ELECTRICITY_DAMAGE = register("electricity_damage", 1.0, 0, 1024.0);
    public static final Holder<Attribute> TOXIN_DAMAGE = register("toxin_damage", 1.0, 0, 1024.0);
    public static final Holder<Attribute> BLAST_DAMAGE = register("blast_damage", 1.0, 0, 1024.0);
    public static final Holder<Attribute> CORROSIVE_DAMAGE = register("corrosive_damage", 1.0, 0, 1024.0);
    public static final Holder<Attribute> GAS_DAMAGE = register("gas_damage", 1.0, 0, 1024.0);
    public static final Holder<Attribute> MAGNETIC_DAMAGE = register("magnetic_damage", 1.0, 0, 1024.0);
    public static final Holder<Attribute> RADIATION_DAMAGE = register("radiation_damage", 1.0, 0, 1024.0);
    public static final Holder<Attribute> VIRAL_DAMAGE = register("viral_damage", 1.0, 0, 1024.0);

    public static Holder<Attribute> register(String name, double defaultAmount, double minAmount, double maxAmount) {
        return ATTRIBUTES.register(name, () -> new RangedAttribute("attribute.tennocraft." + name, defaultAmount, minAmount, maxAmount));
    }

    public static void register(IEventBus modEventBus) {
        ATTRIBUTES.register(modEventBus);
    }
}
