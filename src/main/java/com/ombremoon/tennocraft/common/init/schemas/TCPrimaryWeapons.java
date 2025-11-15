package com.ombremoon.tennocraft.common.init.schemas;

import com.ombremoon.tennocraft.common.modholder.api.weapon.WeaponBuilder;
import com.ombremoon.tennocraft.common.modholder.api.weapon.schema.Schema;
import com.ombremoon.tennocraft.main.CommonClass;
import com.ombremoon.tennocraft.main.Keys;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;

import java.util.List;

public interface TCPrimaryWeapons {
    List<ResourceKey<Schema>> PRIMARY_WEAPONS = new ObjectArrayList<>();

    static void bootstrap(BootstrapContext<Schema> context) {
    }

    private static void register(BootstrapContext<Schema> context, ResourceKey<Schema> key, WeaponBuilder.Ranged builder) {
        context.register(key, builder.build());
        PRIMARY_WEAPONS.add(key);
    }

    private static ResourceKey<Schema> key(String name) {
        return ResourceKey.create(Keys.SCHEMA, CommonClass.customLocation("ranged_weapon/" + name));
    }
}
