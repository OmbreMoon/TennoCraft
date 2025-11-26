package com.ombremoon.tennocraft.common.init.mods;

import com.ombremoon.tennocraft.common.api.mod.*;
import com.ombremoon.tennocraft.common.api.mod.effects.ModDamageEffect;
import com.ombremoon.tennocraft.common.api.mod.effects.value.AddValue;
import com.ombremoon.tennocraft.common.api.weapon.schema.Schema;
import com.ombremoon.tennocraft.common.init.TCDamageTypes;
import com.ombremoon.tennocraft.common.init.TCModEffectComponents;
import com.ombremoon.tennocraft.common.init.TCTags;
import com.ombremoon.tennocraft.main.CommonClass;
import com.ombremoon.tennocraft.main.Keys;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;

public interface TCPrimaryWeaponMods {
    ResourceKey<Modification> HELLFIRE = key("hellfire");
    ResourceKey<Modification> CRYO_ROUNDS = key("cryo_rounds");
    ResourceKey<Modification> THERMITE_ROUNDS = key("thermite_rounds");

    static void bootstrap(BootstrapContext<Modification> context) {
        HolderGetter<Schema> schemas = context.lookup(Keys.SCHEMA);
        HolderGetter<DamageType> damageTypes = context.lookup(Registries.DAMAGE_TYPE);
        register(
                context,
                HELLFIRE,
                Modification.mod(
                                Modification.definition(
                                        schemas.getOrThrow(TCTags.Schemas.RIFLE),
                                        ModType.STANDARD,
                                        ModPolarity.NARAMON,
                                        Modification.dynamicDrain(6),
                                        5,
                                        ModRarity.UNCOMMON
                                )
                        )
                        .withDamageEffect(
                                TCModEffectComponents.MODIFY_DAMAGE_TYPE.get(),
                                ModDamageEffect.modifyDamage(
                                        damageTypes.getOrThrow(TCDamageTypes.HEAT),
                                        new AddValue(RankBasedValue.perLevel(0.15F)),
                                        CommonClass.customLocation("mod.hellfire")
                                )
                        )
        );
        register(
                context,
                CRYO_ROUNDS,
                Modification.mod(
                                Modification.definition(
                                        schemas.getOrThrow(TCTags.Schemas.RIFLE),
                                        ModType.STANDARD,
                                        ModPolarity.NARAMON,
                                        Modification.dynamicDrain(6),
                                        5,
                                        ModRarity.UNCOMMON
                                )
                        )
                        .withDamageEffect(
                                TCModEffectComponents.MODIFY_DAMAGE_TYPE.get(),
                                ModDamageEffect.modifyDamage(
                                        damageTypes.getOrThrow(TCDamageTypes.COLD),
                                        new AddValue(RankBasedValue.perLevel(0.15F)),
                                        CommonClass.customLocation("mod.cryo_rounds")
                                )
                        )
        );
        register(
                context,
                THERMITE_ROUNDS,
                Modification.mod(
                                Modification.definition(
                                        schemas.getOrThrow(TCTags.Schemas.RIFLE),
                                        ModType.STANDARD,
                                        ModPolarity.MADURAI,
                                        Modification.dynamicDrain(4),
                                        3,
                                        ModRarity.RARE
                                )
                        )
                        .withDamageEffect(
                                TCModEffectComponents.MODIFY_DAMAGE_TYPE.get(),
                                ModDamageEffect.modifyDamage(
                                        damageTypes.getOrThrow(TCDamageTypes.HEAT),
                                        new AddValue(RankBasedValue.perLevel(0.15F)),
                                        CommonClass.customLocation("mod.thermite_rounds")
                                )
                        )
                        .withEffect(TCModEffectComponents.STATUS_CHANCE.get(),
                                new AddValue(RankBasedValue.perLevel(0.15F)))
        );
    }

    private static void register(BootstrapContext<Modification> context, ResourceKey<Modification> key, Modification.Builder builder) {
        context.register(key, builder.build(key.location()));
        Modifications.MODS.add(key);
    }

    private static ResourceKey<Modification> key(String name) {
        return ResourceKey.create(Keys.MOD, CommonClass.customLocation(name));
    }
}
