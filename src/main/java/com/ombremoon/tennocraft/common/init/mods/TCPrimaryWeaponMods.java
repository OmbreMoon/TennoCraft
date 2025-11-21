package com.ombremoon.tennocraft.common.init.mods;

import com.ombremoon.tennocraft.common.api.mod.*;
import com.ombremoon.tennocraft.common.api.mod.effects.value.AddValue;
import com.ombremoon.tennocraft.common.api.weapon.schema.Schema;
import com.ombremoon.tennocraft.common.init.TCModEffectComponents;
import com.ombremoon.tennocraft.common.init.TCTags;
import com.ombremoon.tennocraft.main.CommonClass;
import com.ombremoon.tennocraft.main.Keys;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;

public interface TCPrimaryWeaponMods {
    ResourceKey<Modification> HELLFIRE = key("hellfire");

    static void bootstrap(BootstrapContext<Modification> context) {
        HolderGetter<Schema> schemas = context.lookup(Keys.SCHEMA);
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
                        .withEffect(
                                TCModEffectComponents.HEAT.get(),
                                new AddValue(RankBasedValue.perLevel(0.15F))
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
