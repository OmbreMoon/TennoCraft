package com.ombremoon.tennocraft.common.datagen;

import com.ombremoon.tennocraft.TennoCraft;
import com.ombremoon.tennocraft.common.init.entity.TCDamageTypes;
import com.ombremoon.tennocraft.common.init.worldgen.TCConfiguredFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;

import java.util.concurrent.CompletableFuture;

public class DatapackRegistriesProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, TCConfiguredFeatures::bootstrap)
            .add(Registries.DAMAGE_TYPE, TCDamageTypes::bootstrap);

    public DatapackRegistriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(TennoCraft.MOD_ID));
    }
}
