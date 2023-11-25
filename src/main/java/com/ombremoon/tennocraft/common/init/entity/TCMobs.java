package com.ombremoon.tennocraft.common.init.entity;

import com.ombremoon.tennocraft.object.entity.mob.boss.VorBoss;
import com.ombremoon.tennocraft.object.entity.mob.grineer.GrineerLancerEntity;
import net.minecraft.SharedConstants;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.RegistryObject;

public class TCMobs {
    public static void init() {}

    //MOBS
    public static final RegistryObject<EntityType<GrineerLancerEntity>> GRINEER_LANCER = registerMob("grineer_lancer", GrineerLancerEntity::new, 0.6F, 1.95F);

    //BOSSES
    public static final RegistryObject<EntityType<VorBoss>> VOR = registerMob("vor", VorBoss::new, 0.6F, 1.95F);


    private static <T extends Mob> RegistryObject<EntityType<T>> registerMob(String name, EntityType.EntityFactory<T> factory, float width, float height) {
        return registerMob(name, factory, false, width, height);
    }

    /*Helper Method by Tslat*/
    private static <T extends Mob> RegistryObject<EntityType<T>> registerMob(String name, EntityType.EntityFactory<T> factory, boolean fireImmune, float width, float height) {
        EntityType.Builder<T> builder = EntityType.Builder.of(factory, MobCategory.MONSTER).sized(width, height);

        if (fireImmune) {
            builder.fireImmune();
        }

        RegistryObject<EntityType<T>> registryObject = TCEntities.ENTITY_TYPES.register(name, () -> {
            boolean dataFixers = SharedConstants.CHECK_DATA_FIXER_SCHEMA;
            SharedConstants.CHECK_DATA_FIXER_SCHEMA = false;

            EntityType<T> entityType = builder.build(name);
            SharedConstants.CHECK_DATA_FIXER_SCHEMA = dataFixers;

            return entityType;
        });

        return registryObject;
    }
}
