package com.ombremoon.tennocraft.common.init;

import com.ombremoon.tennocraft.common.api.weapon.schema.Schema;
import com.ombremoon.tennocraft.main.CommonClass;
import com.ombremoon.tennocraft.main.Keys;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;

public class TCTags {

    public static class EntityTypes {
        public static final TagKey<EntityType<?>> WEAK_TO_IMPACT = tag("weak_to_impact");
        public static final TagKey<EntityType<?>> WEAK_TO_PUNCTURE = tag("weak_to_puncture");
        public static final TagKey<EntityType<?>> WEAK_TO_SLASH = tag("weak_to_slash");
        public static final TagKey<EntityType<?>> WEAK_TO_HEAT = tag("weak_to_heat");
        public static final TagKey<EntityType<?>> WEAK_TO_COLD = tag("weak_to_cold");
        public static final TagKey<EntityType<?>> WEAK_TO_ELECTRICITY = tag("weak_to_electricity");
        public static final TagKey<EntityType<?>> WEAK_TO_TOXIC = tag("weak_to_toxic");
        public static final TagKey<EntityType<?>> WEAK_TO_BLAST = tag("weak_to_blast");
        public static final TagKey<EntityType<?>> WEAK_TO_CORROSIVE = tag("weak_to_corrosive");
        public static final TagKey<EntityType<?>> WEAK_TO_MAGNETIC = tag("weak_to_magnetic");
        public static final TagKey<EntityType<?>> WEAK_TO_GAS = tag("weak_to_gas");
        public static final TagKey<EntityType<?>> WEAK_TO_RADIATION = tag("weak_to_radiation");
        public static final TagKey<EntityType<?>> WEAK_TO_VIRAL = tag("weak_to_viral");

        public static final TagKey<EntityType<?>> RESISTANT_TO_IMPACT = tag("resistant_to_impact");
        public static final TagKey<EntityType<?>> RESISTANT_TO_PUNCTURE = tag("resistant_to_puncture");
        public static final TagKey<EntityType<?>> RESISTANT_TO_SLASH = tag("resistant_to_slash");
        public static final TagKey<EntityType<?>> RESISTANT_TO_HEAT = tag("resistant_to_heat");
        public static final TagKey<EntityType<?>> RESISTANT_TO_COLD = tag("resistant_to_cold");
        public static final TagKey<EntityType<?>> RESISTANT_TO_ELECTRICITY = tag("resistant_to_electricity");
        public static final TagKey<EntityType<?>> RESISTANT_TO_TOXIC = tag("resistant_to_toxic");
        public static final TagKey<EntityType<?>> RESISTANT_TO_BLAST = tag("resistant_to_blast");
        public static final TagKey<EntityType<?>> RESISTANT_TO_CORROSIVE = tag("resistant_to_corrosive");
        public static final TagKey<EntityType<?>> RESISTANT_TO_MAGNETIC = tag("resistant_to_magnetic");
        public static final TagKey<EntityType<?>> RESISTANT_TO_GAS = tag("resistant_to_gas");
        public static final TagKey<EntityType<?>> RESISTANT_TO_RADIATION = tag("resistant_to_radiation");
        public static final TagKey<EntityType<?>> RESISTANT_TO_VIRAL = tag("resistant_to_viral");

        public static final TagKey<EntityType<?>> IMMUNE_TO_KNOCKBACK = tag("immune_to_knockback");
        public static final TagKey<EntityType<?>> IMMUNE_TO_WEAKENED = tag("immune_to_weakened");
        public static final TagKey<EntityType<?>> IMMUNE_TO_BLEED = tag("immune_to_bleed");
        public static final TagKey<EntityType<?>> IMMUNE_TO_IGNITE = tag("immune_to_ignite");
        public static final TagKey<EntityType<?>> IMMUNE_TO_FREEZE = tag("immune_to_freeze");
        public static final TagKey<EntityType<?>> IMMUNE_TO_TESLA_CHAIN = tag("immune_to_tesla_chain");
        public static final TagKey<EntityType<?>> IMMUNE_TO_POISON = tag("immune_to_poison");
        public static final TagKey<EntityType<?>> IMMUNE_TO_DETONATE = tag("immune_to_detonate");
        public static final TagKey<EntityType<?>> IMMUNE_TO_CORROSION = tag("immune_to_corrosion");
        public static final TagKey<EntityType<?>> IMMUNE_TO_DISRUPT = tag("immune_to_disrupt");
        public static final TagKey<EntityType<?>> IMMUNE_TO_GAS_CLOUD = tag("immune_to_gas_cloud");
        public static final TagKey<EntityType<?>> IMMUNE_TO_CONFUSION = tag("immune_to_confusion");
        public static final TagKey<EntityType<?>> IMMUNE_TO_VIRUS = tag("immune_to_virus");

        private static TagKey<EntityType<?>> tag(String name) {
            return TagKey.create(Registries.ENTITY_TYPE, CommonClass.customLocation(name));
        }
    }

    public static class DamageTypes {
        public static final TagKey<DamageType> BYPASSES_TECH_SHIELD = tag("bypasses_tech_shield");
        public static final TagKey<DamageType> PHYSICAL = tag("physical");
        public static final TagKey<DamageType> ELEMENTAL = tag("elemental");

        private static TagKey<DamageType> tag(String name) {
            return TagKey.create(Registries.DAMAGE_TYPE, CommonClass.customLocation(name));
        }
    }

    public static class MobEffects {
        public static final TagKey<MobEffect> APPLICABLE_FORCED_PROC = tag("applicable_forced_proc");
        public static final TagKey<MobEffect> STATUS_EFFECT = tag("status_effect");

        private static TagKey<MobEffect> tag(String name) {
            return TagKey.create(Registries.MOB_EFFECT, CommonClass.customLocation(name));
        }
    }

    public static class Schemas {
        public static final TagKey<Schema> FRAME = tag("frame");
        public static final TagKey<Schema> RIFLE = tag("rifle");
        public static final TagKey<Schema> SHOTGUN = tag("shotgun");
        public static final TagKey<Schema> PISTOL = tag("pistol");
        public static final TagKey<Schema> MELEE = tag("melee");

        private static TagKey<Schema> tag(String name) {
            return TagKey.create(Keys.SCHEMA, CommonClass.customLocation(name));
        }
    }
}
