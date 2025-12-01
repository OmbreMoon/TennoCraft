package com.ombremoon.tennocraft.common.init.schemas;

import com.ombremoon.tennocraft.common.api.mod.ModPolarity;
import com.ombremoon.tennocraft.common.api.mod.ModSlot;
import com.ombremoon.tennocraft.common.api.mod.Modification;
import com.ombremoon.tennocraft.common.api.weapon.DamageValue;
import com.ombremoon.tennocraft.common.api.weapon.NoiseLevel;
import com.ombremoon.tennocraft.common.api.weapon.WeaponBuilder;
import com.ombremoon.tennocraft.common.api.weapon.ranged.Bullet;
import com.ombremoon.tennocraft.common.api.weapon.ranged.projectile.Hitscan;
import com.ombremoon.tennocraft.common.api.weapon.ranged.projectile.SolidProjectile;
import com.ombremoon.tennocraft.common.api.weapon.ranged.reload.AmmoReloadType;
import com.ombremoon.tennocraft.common.api.weapon.ranged.trigger.BurstTrigger;
import com.ombremoon.tennocraft.common.api.weapon.ranged.trigger.ChargeTrigger;
import com.ombremoon.tennocraft.common.api.weapon.ranged.trigger.HeldTrigger;
import com.ombremoon.tennocraft.common.api.weapon.ranged.trigger.SemiTrigger;
import com.ombremoon.tennocraft.common.api.weapon.schema.RangedAttackProperty;
import com.ombremoon.tennocraft.common.api.weapon.schema.Schema;
import com.ombremoon.tennocraft.common.api.weapon.schema.data.RangedAttack;
import com.ombremoon.tennocraft.common.init.TCBullets;
import com.ombremoon.tennocraft.common.init.TCDamageTypes;
import com.ombremoon.tennocraft.common.world.item.weapon.WeaponSlot;
import com.ombremoon.tennocraft.main.CommonClass;
import com.ombremoon.tennocraft.main.Keys;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.phys.Vec3;

public interface TCSecondaryWeapons {

    ResourceKey<Schema> LATO = key("lato");
    ResourceKey<Schema> ATOMOS = key("atomos");
    ResourceKey<Schema> ANGSTRUM = key("angstrum");
    ResourceKey<Schema> PANDERO = key("pandero");

    static void bootstrap(BootstrapContext<Schema> context) {
        HolderGetter<Bullet> bullets = context.lookup(Keys.BULLET);
        HolderGetter<DamageType> damageTypes = context.lookup(Registries.DAMAGE_TYPE);
        HolderGetter<MobEffect> statuses = context.lookup(Registries.MOB_EFFECT);
        register(
                context,
                LATO,
                WeaponBuilder.of()
                        .slot(WeaponSlot.SECONDARY)
                        .triggerTypes(SemiTrigger.TYPE)
                        .layout(
                                Modification.Compatibility.SECONDARY,
                                new ModSlot(ModPolarity.MADURAI, 0)
                        )
                        .ranged()
                        .utility(
                                210,
                                15,
                                40,
                                1.4F
                        )
                        .addAttack(
                                new SemiTrigger(),
                                new RangedAttackProperty(
                                        new RangedAttack(
                                                1,
                                                4.5F,
                                                1,
                                                0.0F,
                                                UniformFloat.of(0.5F, 10.5F),
                                                new AmmoReloadType(20),
                                                new Hitscan(bullets.getOrThrow(TCBullets.ATOMOS))
                                        ),
                                        0.1F,
                                        1.8F,
                                        0.06F,
                                        NoiseLevel.ALARMING,
                                        new DamageValue(damageTypes.getOrThrow(TCDamageTypes.IMPACT), 10),
                                        new DamageValue(damageTypes.getOrThrow(TCDamageTypes.PUNCTURE), 10),
                                        new DamageValue(damageTypes.getOrThrow(TCDamageTypes.SLASH), 20)
                                )
                        )
        );

        //CHAIN BEAM EFFECT
        register(
                context,
                ATOMOS,
                WeaponBuilder.of()
                        .slot(WeaponSlot.SECONDARY)
                        .triggerTypes(HeldTrigger.TYPE)
                        .layout(Modification.Compatibility.SECONDARY, new ModSlot(ModPolarity.MADURAI, 0))
                        .ranged()
                            .utility(
                                    350,
                                    70,
                                    20,
                                    0.95F
                            )
                            .addAttack(
                                    new HeldTrigger(0.35F, 12, 16, 40),
                                    new RangedAttackProperty(
                                            new RangedAttack(
                                                    1,
                                                    8.0F,
                                                    1,
                                                    0.0F,
                                                    UniformFloat.of(8.0F, 8.01F),
                                                    new AmmoReloadType(40),
                                                    new Hitscan(15.0F, bullets.getOrThrow(TCBullets.ATOMOS))
                                            ),
                                            0.15F,
                                            1.7F,
                                            0.21F,
                                            NoiseLevel.ALARMING,
                                            new DamageValue(damageTypes.getOrThrow(TCDamageTypes.HEAT), 29)
                                    )
                            )
        );

        //CONSUME AMMO EFFECT WITH CHARGE PREDICATE, EXPLODE EFFECT
        register(
                context,
                ANGSTRUM,
                WeaponBuilder.of()
                        .slot(WeaponSlot.SECONDARY)
                        .triggerTypes(ChargeTrigger.TYPE)
                        .layout(Modification.Compatibility.SECONDARY, new ModSlot(ModPolarity.VAZARIN, 0))
                        .ranged()
                            .utility(18, 3, 3, 1.35F)
                            .addAttack(
                                    new ChargeTrigger(10, ChargeTrigger.Type.MIN_AUTO, 0),
                                    new RangedAttackProperty(
                                            new RangedAttack(
                                                    1,
                                                    2.0F,
                                                    1,
                                                    0.0F,
                                                    UniformFloat.of(2.5F, 5.0F),
                                                    new AmmoReloadType(50),
                                                    new SolidProjectile(
                                                            150.0F,
                                                            new Vec3(0.25, 0.25, 0.25),
                                                            bullets.getOrThrow(TCBullets.ATOMOS)
                                                    )
                                            ),
                                            0.16F,
                                            2.0F,
                                            0.22F,
                                            NoiseLevel.ALARMING,
                                            new DamageValue(damageTypes.getOrThrow(TCDamageTypes.BLAST), 200)
                                    )
                            )
        );

        //MODIFY ITEM EFFECT WITH FRAME PREDICATE && HEADSHOT PREDICATE
        register(
                context,
                PANDERO,
                WeaponBuilder.of()
                        .slot(WeaponSlot.SECONDARY)
                        .triggerTypes(SemiTrigger.TYPE, BurstTrigger.TYPE)
                        .layout(Modification.Compatibility.SECONDARY, new ModSlot(ModPolarity.MADURAI, 0))
                        .ranged()
                            .utility(210, 8, 40, 1.2F)
                            .addAttack(
                                    new SemiTrigger(),
                                    new RangedAttackProperty(
                                            new RangedAttack(
                                                    1,
                                                    3.0F,
                                                    1,
                                                    0.0F,
                                                    UniformFloat.of(0.5F, 12.0F),
                                                    new AmmoReloadType(20),
                                                    new Hitscan(bullets.getOrThrow(TCBullets.ATOMOS))
                                            ),
                                            0.3F,
                                            2.8F,
                                            0.2F,
                                            NoiseLevel.ALARMING,
                                            new DamageValue(damageTypes.getOrThrow(TCDamageTypes.IMPACT), 18),
                                            new DamageValue(damageTypes.getOrThrow(TCDamageTypes.PUNCTURE), 18),
                                            new DamageValue(damageTypes.getOrThrow(TCDamageTypes.SLASH), 36)
                                    )
                            )
                            .addAttack(
                                    BurstTrigger.magBurst(2, 4),
                                    new RangedAttackProperty(
                                            new RangedAttack(
                                                    1,
                                                    4.17F,
                                                    1,
                                                    0.0F,
                                                    UniformFloat.of(6.5F, 18.0F),
                                                    new AmmoReloadType(20),
                                                    new Hitscan(bullets.getOrThrow(TCBullets.ATOMOS))
                                            ),
                                            0.3F,
                                            2.8F,
                                            0.1F,
                                            NoiseLevel.ALARMING,
                                            new DamageValue(damageTypes.getOrThrow(TCDamageTypes.IMPACT), 18),
                                            new DamageValue(damageTypes.getOrThrow(TCDamageTypes.PUNCTURE), 18),
                                            new DamageValue(damageTypes.getOrThrow(TCDamageTypes.SLASH), 36)
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
