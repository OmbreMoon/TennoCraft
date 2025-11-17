package com.ombremoon.tennocraft.common.init.mods;

import com.ombremoon.tennocraft.common.api.mod.Modification;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;

import java.util.List;

public interface Modifications {
    List<ResourceKey<Modification>> MODS = new ObjectArrayList<>();

    static void bootstrap(BootstrapContext<Modification> context) {
        TCFrameMods.bootstrap(context);
        TCPrimaryWeaponMods.bootstrap(context);
        TCSecondaryWeaponMods.bootstrap(context);
        TCMeleeWeaponMods.bootstrap(context);
    }
}
