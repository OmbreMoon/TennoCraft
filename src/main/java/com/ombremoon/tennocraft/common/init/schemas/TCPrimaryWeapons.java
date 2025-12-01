package com.ombremoon.tennocraft.common.init.schemas;

import com.ombremoon.tennocraft.common.api.mod.ModPolarity;
import com.ombremoon.tennocraft.common.api.mod.ModSlot;
import com.ombremoon.tennocraft.common.api.mod.Modification;
import com.ombremoon.tennocraft.common.api.weapon.Accuracy;
import com.ombremoon.tennocraft.common.api.weapon.DamageValue;
import com.ombremoon.tennocraft.common.api.weapon.NoiseLevel;
import com.ombremoon.tennocraft.common.api.weapon.WeaponBuilder;
import com.ombremoon.tennocraft.common.api.weapon.ranged.Bullet;
import com.ombremoon.tennocraft.common.api.weapon.ranged.projectile.Hitscan;
import com.ombremoon.tennocraft.common.api.weapon.ranged.projectile.SolidProjectile;
import com.ombremoon.tennocraft.common.api.weapon.ranged.reload.AmmoReloadType;
import com.ombremoon.tennocraft.common.api.weapon.ranged.reload.NoReloadType;
import com.ombremoon.tennocraft.common.api.weapon.ranged.trigger.*;
import com.ombremoon.tennocraft.common.api.weapon.schema.RangedAttackProperty;
import com.ombremoon.tennocraft.common.api.weapon.schema.Schema;
import com.ombremoon.tennocraft.common.api.weapon.schema.data.RangedAttack;
import com.ombremoon.tennocraft.common.init.TCBullets;
import com.ombremoon.tennocraft.common.init.TCDamageTypes;
import com.ombremoon.tennocraft.common.world.item.weapon.WeaponSlot;
import com.ombremoon.tennocraft.main.CommonClass;
import com.ombremoon.tennocraft.main.Keys;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public interface TCPrimaryWeapons {

    ResourceKey<Schema> BRATON = key("braton");
    ResourceKey<Schema> PARIS = key("paris");
    ResourceKey<Schema> BURSTON = key("burston");
    ResourceKey<Schema> TIGRIS = key("tigris");
    ResourceKey<Schema> PENTA = key("penta");
    ResourceKey<Schema> BATTACOR = key("battacor");

    static void bootstrap(BootstrapContext<Schema> context) {
        HolderGetter<Bullet> bullets = context.lookup(Keys.BULLET);
        HolderGetter<DamageType> damageTypes = context.lookup(Registries.DAMAGE_TYPE);
        HolderGetter<MobEffect> statuses = context.lookup(Registries.MOB_EFFECT);
        register(
                context,
                BRATON,
                WeaponBuilder.of()
                        .slot(WeaponSlot.PRIMARY)
                        .triggerTypes(AutoTrigger.TYPE)
                        .layout(Modification.Compatibility.PRIMARY, new ModSlot(ModPolarity.NARAMON, 0))
                        .ranged()
                            .utility(540, 45, 80, 1.35F)
                            .addAttack(
                                new AutoTrigger(),
                                new RangedAttackProperty(
                                        new RangedAttack(
                                                1,
                                                8.75F,
                                                1,
                                                0.0F,
                                                UniformFloat.of(2.0F, 5.0F),
                                                new AmmoReloadType(40),
                                                new Hitscan(bullets.getOrThrow(TCBullets.ATOMOS))
                                        ),
                                        0.12F,
                                        1.6F,
                                        0.06F,
                                        NoiseLevel.ALARMING,
                                        new DamageValue(damageTypes.getOrThrow(TCDamageTypes.IMPACT), 7.92F),
                                        new DamageValue(damageTypes.getOrThrow(TCDamageTypes.PUNCTURE), 7.92F),
                                        new DamageValue(damageTypes.getOrThrow(TCDamageTypes.SLASH), 8.16F)
                                )
                            )
        );
        register(
                context,
                PARIS,
                WeaponBuilder.of()
                        .slot(WeaponSlot.PRIMARY)
                        .triggerTypes(ChargeTrigger.TYPE, FallbackTrigger.TYPE)
                        .layout(Modification.Compatibility.PRIMARY, new ModSlot(ModPolarity.MADURAI, 0))
                        .ranged()
                            .utility(72, 1, 15, 1.0F)
                            .addAttack(
                                new ChargeTrigger(10, ChargeTrigger.Type.FULL_QUICKSHOT, -1),
                                new RangedAttackProperty(
                                        new RangedAttack(
                                                1,
                                                1.0F,
                                                1,
                                                2.0F,
                                                UniformFloat.of(0.0F, 12.0F),
                                                new AmmoReloadType(13),
                                                new SolidProjectile(
                                                        85.0F,
                                                        new Vec3(0.5, 0.5, 0.5),
                                                        bullets.getOrThrow(TCBullets.ATOMOS)
                                                )
                                        ),
                                        0.3F,
                                        2.0F,
                                        0.1F,
                                        NoiseLevel.SILENT,
                                        new DamageValue(damageTypes.getOrThrow(TCDamageTypes.IMPACT), 16),
                                        new DamageValue(damageTypes.getOrThrow(TCDamageTypes.PUNCTURE), 256),
                                        new DamageValue(damageTypes.getOrThrow(TCDamageTypes.SLASH), 48)
                                )
                            )
                            .addAttack(
                                    new FallbackTrigger(),
                                    new RangedAttackProperty(
                                            new RangedAttack(
                                                    1,
                                                    1,
                                                    1,
                                                    0.0F,
                                                    UniformFloat.of(0.0F, 12.0F),
                                                    new AmmoReloadType(13),
                                                    new SolidProjectile(
                                                            70.0F,
                                                            new Vec3(0.5, 0.5, 0.5),
                                                            bullets.getOrThrow(TCBullets.ATOMOS)
                                                    )
                                            ),
                                            0.3F,
                                            2.0F,
                                            0.1F,
                                            NoiseLevel.SILENT,
                                            new DamageValue(damageTypes.getOrThrow(TCDamageTypes.IMPACT), 8),
                                            new DamageValue(damageTypes.getOrThrow(TCDamageTypes.PUNCTURE), 120),
                                            new DamageValue(damageTypes.getOrThrow(TCDamageTypes.SLASH), 32)
                                    )
                            )
        );
        register(
                context,
                BURSTON,
                WeaponBuilder.of()
                        .slot(WeaponSlot.PRIMARY)
                        .triggerTypes(BurstTrigger.TYPE)
                        .layout(Modification.Compatibility.PRIMARY, new ModSlot(ModPolarity.MADURAI, 0))
                        .ranged()
                            .utility(540, 45, 60, 1.45F)
                            .addAttack(
                                BurstTrigger.burst(3, 1),
                                new RangedAttackProperty(
                                        new RangedAttack(
                                                1,
                                                5.0F,
                                                1,
                                                0.0F,
                                                UniformFloat.of(0.0F, 8.0F),
                                                new AmmoReloadType(40),
                                                new Hitscan(bullets.getOrThrow(TCBullets.ATOMOS))
                                        ),
                                        0.06F,
                                        1.6F,
                                        0.18F,
                                        NoiseLevel.ALARMING,
                                        new DamageValue(damageTypes.getOrThrow(TCDamageTypes.IMPACT), 10),
                                        new DamageValue(damageTypes.getOrThrow(TCDamageTypes.PUNCTURE), 10),
                                        new DamageValue(damageTypes.getOrThrow(TCDamageTypes.SLASH), 10)
                                )
                            )
        );
        register(
                context,
                TIGRIS,
                WeaponBuilder.of()
                        .mastery(7)
                        .slot(WeaponSlot.PRIMARY)
                        .triggerTypes(DuplexTrigger.TYPE)
                        .layout(Modification.Compatibility.PRIMARY, new ModSlot(ModPolarity.NARAMON, 0))
                        .ranged()
                            .utility(120, 2, 15, 2.0F)
                            .addAttack(
                                new DuplexTrigger(),
                                new RangedAttackProperty(
                                        new RangedAttack(
                                                1,
                                                2.0F,
                                                5,
                                                0.0F,
                                                UniformFloat.of(6.0F, 16.0F),
                                                new AmmoReloadType(36),
                                                new Hitscan(bullets.getOrThrow(TCBullets.ATOMOS))
                                        ),
                                        0.1F,
                                        2.0F,
                                        0.168F,
                                        NoiseLevel.ALARMING,
                                        new DamageValue(damageTypes.getOrThrow(TCDamageTypes.IMPACT), 21),
                                        new DamageValue(damageTypes.getOrThrow(TCDamageTypes.PUNCTURE), 21),
                                        new DamageValue(damageTypes.getOrThrow(TCDamageTypes.SLASH), 168)
                                )
                            )
        );

        //EXPLODE EFFECT, LIFETIME
        register(
                context,
                PENTA,
                WeaponBuilder.of()
                        .mastery(6)
                        .slot(WeaponSlot.PRIMARY)
                        .triggerTypes(ActiveTrigger.TYPE)
                        .layout(Modification.Compatibility.PRIMARY, new ModSlot(ModPolarity.NARAMON, 0))
                        .ranged()
                            .utility(20, 5, 5, 1.0F)
                            .addAttack(
                                new ActiveTrigger(5),
                                new RangedAttackProperty(
                                        new RangedAttack(
                                                1,
                                                1.0F,
                                                1,
                                                0.0F,
                                                UniformFloat.of(0.0F, 0.01F),
                                                new AmmoReloadType(50),
                                                new SolidProjectile(
                                                        20.0F,
                                                        new Vec3(0.5, 0.5, 0.5),
                                                        bullets.getOrThrow(TCBullets.ATOMOS))
                                        ),
                                        0.1F,
                                        2.0F,
                                        0.1F,
                                        NoiseLevel.ALARMING,
                                        new DamageValue(damageTypes.getOrThrow(TCDamageTypes.IMPACT), 75)
                                )
                            )
        );

        //Modify Damage Effect, Handler Kill Counter Predicate
        register(
                context,
                BATTACOR,
                WeaponBuilder.of()
                        .mastery(10)
                        .slot(WeaponSlot.PRIMARY)
                        .triggerTypes(BurstTrigger.TYPE, ChargeTrigger.TYPE)
                        .layout(Modification.Compatibility.PRIMARY, new ModSlot(ModPolarity.NARAMON, 0))
                        .ranged()
                            .utility(720, 60, 80, 2.50F)
                            .addAttack(
                                BurstTrigger.autoBurst(2, 2),
                                new RangedAttackProperty(
                                        new RangedAttack(
                                                1,
                                                2.5F,
                                                1,
                                                0.0F,
                                                UniformFloat.of(0.0F, 8.0F),
                                                new AmmoReloadType(40),
                                                new SolidProjectile(
                                                        120.0F,
                                                        new Vec3(0.5, 0.5, 0.5),
                                                        bullets.getOrThrow(TCBullets.ATOMOS))
                                        ),
                                        0.32F,
                                        2.4F,
                                        0.18F,
                                        NoiseLevel.ALARMING,
                                        new DamageValue(damageTypes.getOrThrow(TCDamageTypes.IMPACT), 24),
                                        new DamageValue(damageTypes.getOrThrow(TCDamageTypes.MAGNETIC), 42)
                                )
                            )
                            .addAttack(
                                    new ChargeTrigger(8, ChargeTrigger.Type.AUTO, 0),
                                    new RangedAttackProperty(
                                            new RangedAttack(
                                                    0,
                                                    5.0F,
                                                    1,
                                                    2.0F,
                                                    UniformFloat.of(0.0F, 0.01F),
                                                    new NoReloadType(),
                                                    new Hitscan(bullets.getOrThrow(TCBullets.ATOMOS))
                                            ),
                                            0.34F,
                                            3.0F,
                                            0.08F,
                                            NoiseLevel.ALARMING,
                                            new DamageValue(damageTypes.getOrThrow(TCDamageTypes.RADIATION), 208)
                                    )
                            )
        );
    }

    private static void register(BootstrapContext<Schema> context, ResourceKey<Schema> key, WeaponBuilder.Ranged builder) {
        context.register(key, builder.build());
        Schemas.SCHEMAS.add(key);
    }

    private static ResourceKey<Schema> key(String name) {
        return ResourceKey.create(Keys.SCHEMA, CommonClass.customLocation("ranged_weapon/" + name));
    }
}
