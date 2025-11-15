package com.ombremoon.tennocraft.common.init.mods;

import com.ombremoon.tennocraft.common.init.TCAttributes;
import com.ombremoon.tennocraft.common.init.TCTags;
import com.ombremoon.tennocraft.common.modholder.api.mod.*;
import com.ombremoon.tennocraft.common.modholder.api.mod.effects.ModAttributeEffect;
import com.ombremoon.tennocraft.common.modholder.api.weapon.schema.Schema;
import com.ombremoon.tennocraft.main.CommonClass;
import com.ombremoon.tennocraft.main.Keys;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public interface TCSecondaryWeaponMods {
    ResourceKey<Modification> PISTOL_GAMBIT = key("pistol_gambit");

    static void bootstrap(BootstrapContext<Modification> context) {
        HolderGetter<Schema> schemas = context.lookup(Keys.SCHEMA);
        register(
                context,
                PISTOL_GAMBIT,
                Modification.mod(
                        Modification.definition(
                                schemas.getOrThrow(TCTags.Schemas.PISTOL),
                                ModType.STANDARD,
                                ModPolarity.MADURAI,
                                Modification.dynamicDrain(4),
                                5,
                                ModRarity.COMMON
                        )
                )
                .withEffect(
                        TCModEffectComponents.ATTRIBUTES.get(),
                        new ModAttributeEffect(
                                CommonClass.customLocation("mod.pistol_gambit"),
                                TCAttributes.CRIT_CHANCE,
                                RankBasedValue.perLevel(0.2F),
                                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                        )
                )
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
