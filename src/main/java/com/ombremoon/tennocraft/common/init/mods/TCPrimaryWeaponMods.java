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

public interface TCPrimaryWeaponMods {
    ResourceKey<Modification> PISTOL_GAMBIT = key("pistol_gambit");

    static void bootstrap(BootstrapContext<Modification> context) {
        HolderGetter<Schema> schemas = context.lookup(Keys.SCHEMA);
    }

    private static void register(BootstrapContext<Modification> context, ResourceKey<Modification> key, Modification.Builder builder) {
        context.register(key, builder.build(key.location()));
        Modifications.MODS.add(key);
    }

    private static ResourceKey<Modification> key(String name) {
        return ResourceKey.create(Keys.MOD, CommonClass.customLocation(name));
    }
}
