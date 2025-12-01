package com.ombremoon.tennocraft.common.init.schemas;

import com.ombremoon.tennocraft.common.api.mod.ModPolarity;
import com.ombremoon.tennocraft.common.api.mod.ModSlot;
import com.ombremoon.tennocraft.common.api.mod.Modification;
import com.ombremoon.tennocraft.common.api.weapon.DamageValue;
import com.ombremoon.tennocraft.common.api.weapon.NoiseLevel;
import com.ombremoon.tennocraft.common.api.weapon.WeaponBuilder;
import com.ombremoon.tennocraft.common.api.weapon.schema.AttackProperty;
import com.ombremoon.tennocraft.common.api.weapon.schema.Schema;
import com.ombremoon.tennocraft.common.api.weapon.schema.data.ComboSet;
import com.ombremoon.tennocraft.common.api.weapon.schema.data.SlamAttack;
import com.ombremoon.tennocraft.common.init.TCComboSets;
import com.ombremoon.tennocraft.common.init.TCDamageTypes;
import com.ombremoon.tennocraft.common.world.item.weapon.WeaponSlot;
import com.ombremoon.tennocraft.main.CommonClass;
import com.ombremoon.tennocraft.main.Keys;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffects;

public interface TCMeleeWeapons {

    ResourceKey<Schema> ORTHOS = key("orthos");

    static void bootstrap(BootstrapContext<Schema> context) {
        HolderGetter<DamageType> damageTypes = context.lookup(Registries.DAMAGE_TYPE);
        HolderGetter<ComboSet> comboSets = context.lookup(Keys.COMBO_SET);
        register(
                context,
                ORTHOS,
                WeaponBuilder.of()
                        .slot(WeaponSlot.MELEE)
                        .mastery(2)
                        .layout(
                                Modification.Compatibility.MELEE,
                                new ModSlot(ModPolarity.NARAMON, 0)
                        )
                        .melee()
                            .utility(
                                    0.92F,
                                    55,
                                    100,
                                    1.0F,
                                    0.6F
                            )
                            .attackProperties(
                                    comboSets.getOrThrow(TCComboSets.STAFF),
                                    AttackProperty.createAttack(
                                            0.06F,
                                            1.5F,
                                            0.18F,
                                            NoiseLevel.SILENT,
                                            new DamageValue(damageTypes.getOrThrow(TCDamageTypes.IMPACT), 27.75F),
                                            new DamageValue(damageTypes.getOrThrow(TCDamageTypes.PUNCTURE), 27.75F),
                                            new DamageValue(damageTypes.getOrThrow(TCDamageTypes.SLASH), 129.5F)
                                    ),
                                    18,
                                    new SlamAttack(
                                            TCDamageTypes.BLAST,
                                            MobEffects.MOVEMENT_SLOWDOWN,
                                            7.0F
                                    ),
                                    new SlamAttack(
                                            TCDamageTypes.BLAST,
                                            MobEffects.MOVEMENT_SLOWDOWN,
                                            8.0F
                                    )
                            )

        );
    }

    private static void register(BootstrapContext<Schema> context, ResourceKey<Schema> key, WeaponBuilder.Melee builder) {
        context.register(key, builder.build());
        Schemas.SCHEMAS.add(key);
    }

    private static ResourceKey<Schema> key(String name) {
        return ResourceKey.create(Keys.SCHEMA, CommonClass.customLocation("melee_weapon/" + name));
    }
}
