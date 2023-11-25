package com.ombremoon.tennocraft.common.init.entity;

import com.ombremoon.tennocraft.object.entity.projectile.GenericBullet;
import net.minecraft.SharedConstants;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;

public class TCProjectiles {

    public static void init() {}

    public static RegistryObject<EntityType<GenericBullet>> GENERIC_BULLET = registerProjectile("generic_bullet", GenericBullet::new);

    private static <T extends Entity> RegistryObject<EntityType<T>> registerProjectile(String name, EntityType.EntityFactory<T> factory) {
        return registerProjectile(name, factory, 0.25F, 0.25F);
    }

    private static <T extends Entity> RegistryObject<EntityType<T>> registerProjectile(String name, EntityType.EntityFactory<T> factory, float width, float height) {
        return registerProjectile(name, factory, width, height, 3);
    }

    private static <T extends Entity> RegistryObject<EntityType<T>> registerProjectile(String name, EntityType.EntityFactory<T> factory, float width, float height, int updateInterval) {
        return registerProjectile(name, factory, width, height, updateInterval, EntityType.Builder::noSave);
    }

    /*Helper Method by Tslat*/
    private static <T extends Entity> RegistryObject<EntityType<T>> registerProjectile(String name, EntityType.EntityFactory<T> factory, float width, float height, int updateInterval, Consumer<EntityType.Builder<T>> builderConsumer) {
        EntityType.Builder<T> builder = EntityType.Builder.of(factory, MobCategory.MISC).sized(width, height).clientTrackingRange(8).setTrackingRange(120).updateInterval(updateInterval);

        builderConsumer.accept(builder);

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
