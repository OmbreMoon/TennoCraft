package com.ombremoon.tennocraft.common.init.schemas;

import com.ombremoon.tennocraft.common.api.weapon.DamageValue;
import com.ombremoon.tennocraft.common.api.weapon.NoiseLevel;
import com.ombremoon.tennocraft.common.api.weapon.TriggerType;
import com.ombremoon.tennocraft.common.api.weapon.WeaponBuilder;
import com.ombremoon.tennocraft.common.api.weapon.projectile.AmmoReloadType;
import com.ombremoon.tennocraft.common.api.weapon.projectile.Bullet;
import com.ombremoon.tennocraft.common.api.weapon.projectile.Hitscan;
import com.ombremoon.tennocraft.common.api.weapon.projectile.SolidProjectile;
import com.ombremoon.tennocraft.common.api.weapon.schema.RangedAttackSchema;
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
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public interface TCSecondaryWeapons {

    ResourceKey<Schema> ATOMOS = key("atomos");

    static void bootstrap(BootstrapContext<Schema> context) {
        HolderGetter<Bullet> bullets = context.lookup(Keys.BULLET);
        HolderGetter<DamageType> damageTypes = context.lookup(Registries.DAMAGE_TYPE);
        HolderGetter<MobEffect> statuses = context.lookup(Registries.MOB_EFFECT);
        register(
                context,
                ATOMOS,
                WeaponBuilder.of()
                        .slot(WeaponSlot.SECONDARY)
                        .triggerTypes(TriggerType.HELD)
                        .ranged()
                            .utility(
                                    12.5F,
                                    8.0F,
                                    350,
                                    70,
                                    20,
                                    40,
                                    0.95F
                            )
                            .addAttack(
                                    TriggerType.HELD,
                                    new RangedAttackSchema(
                                            new RangedAttack(
                                                    1,
                                                    8.0F,
                                                    1,
                                                    0.0F,
                                                    UniformInt.of(8, 8),
                                                    new AmmoReloadType(),
//                                                    new Hitscan(bullets.getOrThrow(TCBullets.ATOMOS))
                                                    new SolidProjectile(
                                                            3.0F,
                                                            new Vec3(1.0F, 1.0F, 1.0F),
                                                            bullets.getOrThrow(TCBullets.ATOMOS))
                                            ),
                                            0.15F,
                                            1.7F,
                                            0.21F,
                                            NoiseLevel.ALARMING,
                                            new DamageValue(damageTypes.getOrThrow(TCDamageTypes.HEAT), 29)
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
