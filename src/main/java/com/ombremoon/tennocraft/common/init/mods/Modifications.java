package com.ombremoon.tennocraft.common.init.mods;

import com.ombremoon.tennocraft.common.modholder.api.mod.Modification;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;

import java.util.List;

public interface Modifications {
    List<ResourceKey<Modification>> MODS = new ObjectArrayList<>();

    static void bootstrap(BootstrapContext<Modification> context) {
        TCSecondaryWeaponMods.bootstrap(context);
    }
}
