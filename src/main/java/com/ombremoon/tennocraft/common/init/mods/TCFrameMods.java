package com.ombremoon.tennocraft.common.init.mods;

import com.ombremoon.tennocraft.common.api.mod.*;
import com.ombremoon.tennocraft.common.api.mod.effects.ModAttributeEffect;
import com.ombremoon.tennocraft.common.api.weapon.schema.Schema;
import com.ombremoon.tennocraft.common.init.TCAttributes;
import com.ombremoon.tennocraft.common.init.TCTags;
import com.ombremoon.tennocraft.main.CommonClass;
import com.ombremoon.tennocraft.main.Keys;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public interface TCFrameMods {
    ResourceKey<Modification> CONTINUITY = key("continuity");

    static void bootstrap(BootstrapContext<Modification> context) {
        HolderGetter<Schema> schemas = context.lookup(Keys.SCHEMA);
        register(
                context,
                CONTINUITY,
                Modification.mod(
                        Modification.definition(
                                schemas.getOrThrow(TCTags.Schemas.FRAME),
                                ModType.STANDARD,
                                ModPolarity.MADURAI,
                                Modification.dynamicDrain(2),
                                5,
                                ModRarity.RARE
                        )
                )
                .withEffect(
                        TCModEffectComponents.ATTRIBUTES.get(),
                        new ModAttributeEffect(
                                CommonClass.customLocation("mod.continuity"),
                                TCAttributes.ABILITY_DURATION,
                                RankBasedValue.perLevel(0.05F),
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
