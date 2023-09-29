package com.ombremoon.tennocraft.common.datagen;

import com.ombremoon.tennocraft.TennoCraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = TennoCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TCDataGen {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        CompletableFuture<HolderLookup.Provider> lookupProviderWithOwn = lookupProvider.thenApply(provider ->
                DatapackRegistriesProvider.BUILDER.buildPatch(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY), provider));

        generator.addProvider(true, new TCRecipeProvider(packOutput));
        generator.addProvider(true, TCLootTableProvider.create(packOutput));
        generator.addProvider(true, new TCBlockStateProvider(packOutput, existingFileHelper));
        generator.addProvider(true, new TCItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeServer(), new DatapackRegistriesProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new TCDamageTypeTagsProvider(packOutput, lookupProviderWithOwn, existingFileHelper));
    }
}
