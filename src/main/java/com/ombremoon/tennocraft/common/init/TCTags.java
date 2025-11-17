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
