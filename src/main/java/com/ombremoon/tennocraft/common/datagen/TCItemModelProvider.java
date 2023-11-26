package com.ombremoon.tennocraft.common.datagen;

import com.ombremoon.tennocraft.TennoCraft;
import com.ombremoon.tennocraft.common.init.item.TCFrames;
import com.ombremoon.tennocraft.common.init.item.TCItems;
import com.ombremoon.tennocraft.common.init.item.TCMods;
import com.ombremoon.tennocraft.common.init.item.TCWeapons;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class TCItemModelProvider extends ItemModelProvider {

    public TCItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, TennoCraft.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        registerTransferenceKeys();
        registerSecondaryWeaponModels();
        registerMeleeWeaponModels();
        registerModModels();
        registerSpawnEggs();
    }

    private void registerTransferenceKeys() {
        tempItem(TCFrames.VOLT_KEY);
        tempItem(TCFrames.EXCALIBUR_KEY);
        tempItem(TCFrames.MAG_KEY);
    }

    private void registerPrimaryWeaponModels() {

    }

    private void registerSecondaryWeaponModels() {
        tempItem(TCWeapons.LATO);
        tempItem(TCWeapons.ANGSTRUM);
    }

    private void registerMeleeWeaponModels() {
        tempItem(TCWeapons.SKANA);
    }

    private void registerModModels() {
        tempItem(TCMods.CONTINUITY);
        tempItem(TCMods.NARROW_MINDED);
        tempItem(TCMods.HEATED_CHARGE);
        tempItem(TCMods.MOLTEN_IMPACT);
        tempItem(TCMods.VOLTAIC_EDGE);
    }

    private void registerSpawnEggs() {
        withExistingParent(TCItems.GRINEER_LANCER_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
    }

    private ItemModelBuilder crossItem(RegistryObject<Block> block) {
        return withExistingParent(block.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(TennoCraft.MOD_ID, "block/" + block.getId().getPath()));
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(TennoCraft.MOD_ID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder tempItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(TennoCraft.MOD_ID, "item/" + "temp_texture"));
    }

    private ItemModelBuilder handheldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(TennoCraft.MOD_ID,"item/" + item.getId().getPath()));
    }
}
