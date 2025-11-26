package com.ombremoon.tennocraft.datagen;

import com.ombremoon.tennocraft.common.init.TCBullets;
import com.ombremoon.tennocraft.common.init.TCComboSets;
import com.ombremoon.tennocraft.common.init.mods.Modifications;
import com.ombremoon.tennocraft.common.init.schemas.Schemas;
import com.ombremoon.tennocraft.common.init.TCDamageTypes;
import com.ombremoon.tennocraft.main.Constants;
import com.ombremoon.tennocraft.main.Keys;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class DatapackRegistriesProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Keys.SCHEMA, Schemas::bootstrap)
            .add(Keys.MOD, Modifications::bootstrap)
            .add(Keys.COMBO_SET, TCComboSets::bootstrap)
            .add(Keys.BULLET, TCBullets::bootstrap)
            .add(Registries.DAMAGE_TYPE, TCDamageTypes::bootstrap);

    public DatapackRegistriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(Constants.MOD_ID));
    }
}
