package com.ombremoon.tennocraft.common.init;

import com.ombremoon.tennocraft.common.world.entity.BulletProjectile;
import com.ombremoon.tennocraft.main.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class TCEntities {
    public static final List<AttributesRegister<?>> SUPPLIERS = new ArrayList<>();
    public static final List<Supplier<? extends EntityType<?>>> MOBS = new ArrayList<>();
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister
            .create(Registries.ENTITY_TYPE, Constants.MOD_ID);

    public static final Supplier<EntityType<BulletProjectile>> BULLET_PROJECTILE = registerEntity("bullet", BulletProjectile::new, 0.5F, 0.5F);

    protected static <T extends Mob> Supplier<EntityType<T>> registerMob(String name, EntityType.EntityFactory<T> factory, MobCategory category, float width, float height, int clientTrackingRange, Supplier<AttributeSupplier.Builder> attributeSupplier) {
        return registerMob(name, factory, category, width, height, clientTrackingRange, attributeSupplier, true);
    }

    protected static <T extends Mob> Supplier<EntityType<T>> registerMob(String name, EntityType.EntityFactory<T> factory, MobCategory category, float width, float height, int clientTrackingRange, Supplier<AttributeSupplier.Builder> attributeSupplier, boolean hasLoot) {
        return registerMob(name, factory, category, width, height, height * 0.85F, clientTrackingRange, attributeSupplier, hasLoot);
    }

    protected static <T extends Mob> Supplier<EntityType<T>> registerMob(String name, EntityType.EntityFactory<T> factory, MobCategory category, float width, float height, float eyeHeight, int clientTrackingRange, Supplier<AttributeSupplier.Builder> attributeSupplier, boolean hasLoot) {
        return registerMob(name, factory, category, true, width, height, eyeHeight, clientTrackingRange, attributeSupplier, hasLoot);
    }

    protected static <T extends Mob> Supplier<EntityType<T>> registerMob(String name, EntityType.EntityFactory<T> factory, MobCategory mobCategory, boolean fireImmune, float width, float height, float eyeHeight, int clientTrackingRange, Supplier<AttributeSupplier.Builder> attributeSupplier, boolean hasLoot) {
        EntityType.Builder<T> builder = EntityType.Builder.of(factory, mobCategory).sized(width, height).eyeHeight(eyeHeight).clientTrackingRange(clientTrackingRange);

        if (fireImmune) {
            builder.fireImmune();
        }

        var entitySupplier = ENTITIES.register(name, () -> {
            EntityType<T> entityType = builder.build(name);
            if (attributeSupplier != null) {
                SUPPLIERS.add(new AttributesRegister<>(() -> entityType, attributeSupplier));
            }
            return entityType;
        });
        if (hasLoot) MOBS.add(entitySupplier);
        return entitySupplier;
    }

    protected static <T extends Entity> Supplier<EntityType<T>> registerEntity(String name, EntityType.EntityFactory<T> factory, float width, float height) {
        EntityType.Builder<T> builder = EntityType.Builder.of(factory, MobCategory.MISC).sized(width, height).fireImmune().clientTrackingRange(4);

        return ENTITIES.register(name, () -> {
            return builder.build(name);
        });
    }

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }

    public record AttributesRegister<E extends LivingEntity>(Supplier<EntityType<E>> entityTypeSupplier, Supplier<AttributeSupplier.Builder> attributeSupplier) {}
}
