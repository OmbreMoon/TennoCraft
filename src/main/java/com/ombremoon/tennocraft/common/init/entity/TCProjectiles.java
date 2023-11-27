package com.ombremoon.tennocraft.common.init.entity;

import com.ombremoon.tennocraft.object.entity.projectile.AbstractBullet;
import com.ombremoon.tennocraft.object.item.weapon.AbstractProjectileWeapon;
import net.minecraft.SharedConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class TCProjectiles {

    public static void init() {}

    public static RegistryObject<EntityType<AbstractBullet>> ABSTRACT_BULLET = registerProjectile("abstract_bullet", AbstractBullet::new);

    private static <T extends Entity> RegistryObject<EntityType<T>> registerProjectile(String name, EntityType.EntityFactory<T> factory) {
        return registerProjectile(name, factory, 0.25F, 0.25F);
    }

    private static <T extends Entity> RegistryObject<EntityType<T>> registerProjectile(String name, EntityType.EntityFactory<T> factory, float width, float height) {
        return registerProjectile(name, factory, width, height, 1);
    }

    private static <T extends Entity> RegistryObject<EntityType<T>> registerProjectile(String name, EntityType.EntityFactory<T> factory, float width, float height, int updateInterval) {
        return registerProjectile(name, factory, width, height, updateInterval, EntityType.Builder::noSave);
    }

    /*Helper Method by Tslat*/
    private static <T extends Entity> RegistryObject<EntityType<T>> registerProjectile(String name, EntityType.EntityFactory<T> factory, float width, float height, int updateInterval, Consumer<EntityType.Builder<T>> builderConsumer) {
        EntityType.Builder<T> builder = EntityType.Builder.of(factory, MobCategory.MISC)
                .sized(width, height)
                .setTrackingRange(100)
                .updateInterval(updateInterval)
                .fireImmune()
                .noSummon()
                .setShouldReceiveVelocityUpdates(true);

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

    //Credit to MrCrayfish
    public static class ProjectileSpawner {
        private final ProjectileFactory GENERIC = (level, attackEntity, itemStack, projectileWeapon) -> new AbstractBullet(TCProjectiles.ABSTRACT_BULLET.get(), level, attackEntity, itemStack, projectileWeapon);
        private final Map<ResourceLocation, ProjectileFactory> projectileFactory = new HashMap<>();

        private static ProjectileSpawner instance = null;

        public static ProjectileSpawner getInstance() {
            if(instance == null)
                instance = new ProjectileSpawner();

            return instance;
        }

        public ProjectileFactory getProjectile(ResourceLocation resourceLocation) {
            return projectileFactory.getOrDefault(resourceLocation, GENERIC);
        }
    }

    public interface ProjectileFactory {

        AbstractBullet create(Level level, LivingEntity attackEntity, ItemStack itemStack, AbstractProjectileWeapon projectileWeapon);
    }
}
