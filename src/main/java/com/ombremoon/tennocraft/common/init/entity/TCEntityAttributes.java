package com.ombremoon.tennocraft.common.init.entity;

import com.ombremoon.tennocraft.object.entity.generic.TCEnemy;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;

public class TCEntityAttributes {
    public static void init(IEventBus modEventBus) {
        modEventBus.addListener(EventPriority.NORMAL, false, EntityAttributeCreationEvent.class, TCEntityAttributes::registerEntityAttributes);
    }

    private static void registerEntityAttributes(final EntityAttributeCreationEvent event) {
        registerGrineerStats(event);
    }

    private static void registerPassiveCreatureStats(final EntityAttributeCreationEvent event) {

    }

    private static void registerGrineerStats(final EntityAttributeCreationEvent event) {
        AttributeBuilder.createMonsterAttributes(TCMobs.GRINEER_LANCER.get()).getHealth(100).getFollowRange(5.0F).getMovementSpeed(0.25F).getAttackDamage(3.0F).getArmorResist(2.0F).build(event);

    }

    private static void registerBossStats(final EntityAttributeCreationEvent event) {

    }

    private static void registerMiniBossStats(final EntityAttributeCreationEvent event) {

    }

    private static void registerNPCStats(final EntityAttributeCreationEvent event) {

    }

    private static void registerUniqueStats(final EntityAttributeCreationEvent event) {

    }

    private static void registerSummonStats(final EntityAttributeCreationEvent event) {

    }

    private record AttributeBuilder(EntityType<? extends LivingEntity> entityType, AttributeSupplier.Builder builder) {
        private static AttributeBuilder createEntityAttributes(EntityType<? extends LivingEntity> entityType) {
            return new AttributeBuilder(entityType, LivingEntity.createLivingAttributes());
        }

        private static AttributeBuilder createMonsterAttributes(EntityType<? extends LivingEntity> entityType) {
            return new AttributeBuilder(entityType, TCEnemy.createAttributes());
        }

        private AttributeBuilder getHealth(double maxHealth) {
            builder.add(Attributes.MAX_HEALTH, maxHealth);
            return this;
        }

        private AttributeBuilder getFollowRange(double followRange) {
            builder.add(Attributes.FOLLOW_RANGE, followRange);
            return this;
        }

        private AttributeBuilder getKnockbackResistance(double knockbackResistance) {
            builder.add(Attributes.KNOCKBACK_RESISTANCE, knockbackResistance);
            return this;
        }

        private AttributeBuilder getMovementSpeed(double movementSpeed) {
            builder.add(Attributes.MOVEMENT_SPEED, movementSpeed);
            return this;
        }

        private AttributeBuilder getFlyingSpeed(double flyingSpeed) {
            builder.add(Attributes.FLYING_SPEED, flyingSpeed);
            return this;
        }

        private AttributeBuilder getAttackDamage(double attackDamage) {
            builder.add(Attributes.ATTACK_DAMAGE, attackDamage);
            return this;
        }

        private AttributeBuilder getAttackKnockback(double attackKnockback) {
            builder.add(Attributes.ATTACK_KNOCKBACK, attackKnockback);
            return this;
        }

        private AttributeBuilder getAttackSpeed(double attackSpeed) {
            builder.add(Attributes.ATTACK_SPEED, attackSpeed);
            return this;
        }

        private AttributeBuilder getArmorResist(double armorResist) {
            builder.add(Attributes.ARMOR, armorResist);
            return this;
        }

        private AttributeBuilder getArmorWithToughness(double armorResist, double armorToughness) {
            builder.add(Attributes.ARMOR, armorResist);
            builder.add(Attributes.ARMOR_TOUGHNESS, armorToughness);
            return this;
        }

        private AttributeBuilder getSwimSpeed(double swimSpeed) {
            builder.add(ForgeMod.SWIM_SPEED.get(), swimSpeed);
            return this;
        }

        private AttributeBuilder getNameTageDistance(double nameTageDistance) {
            builder.add(ForgeMod.NAMETAG_DISTANCE.get(), nameTageDistance);
            return this;
        }

        private AttributeBuilder getEntityGravity(double entityGravity) {
            builder.add(ForgeMod.ENTITY_GRAVITY.get(), entityGravity);
            return this;
        }

        private AttributeBuilder addSpawnReinforcementChance() {
            builder.add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
            return this;
        }

        private AttributeBuilder getStepHeight(double stepHeight) {
            builder.add(ForgeMod.STEP_HEIGHT_ADDITION.get(), stepHeight);
            return this;
        }

        private AttributeBuilder addExtraAttributes(Attribute... attributes) {
            for (Attribute attribute : attributes) {
                if (!this.builder.hasAttribute(attribute)) {
                    this.builder.add(attribute);
                }
            }
            return this;
        }

        private void build(EntityAttributeCreationEvent event) {
            event.put(entityType, builder.build());
        }
    }
}
