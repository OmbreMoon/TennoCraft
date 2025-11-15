package com.ombremoon.tennocraft.common.init.schemas;

import com.ombremoon.tennocraft.common.modholder.api.weapon.schema.Schema;
import net.minecraft.data.worldgen.BootstrapContext;

public interface Schemas {

    static void bootstrap(BootstrapContext<Schema> context) {
        TCFrames.bootstrap(context);
        TCPrimaryWeapons.bootstrap(context);
        TCSecondaryWeapons.bootstrap(context);
    }
}
