package com.ombremoon.tennocraft.common.init.schemas;

import com.ombremoon.tennocraft.common.api.weapon.schema.Schema;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;

import java.util.List;

public interface Schemas {
    List<ResourceKey<Schema>> SCHEMAS = new ObjectArrayList<>();

    static void bootstrap(BootstrapContext<Schema> context) {
        TCFrames.bootstrap(context);
        TCPrimaryWeapons.bootstrap(context);
        TCSecondaryWeapons.bootstrap(context);
        TCMeleeWeapons.bootstrap(context);
    }
}
