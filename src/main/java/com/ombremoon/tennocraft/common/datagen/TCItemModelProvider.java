package com.ombremoon.tennocraft.common.datagen;

import com.ombremoon.tennocraft.TennoCraft;
import com.ombremoon.tennocraft.common.init.item.TCFrames;
import com.ombremoon.tennocraft.common.init.item.TCItems;
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
        registerMineframeModels();
    }

    private void registerMineframeModels() {
        tempItem(TCFrames.VOLT_HELMET);
        tempItem(TCFrames.VOLT_CHASSIS);
        tempItem(TCFrames.VOLT_LEGGINGS);
        tempItem(TCFrames.VOLT_BOOTS);
        tempItem(TCFrames.EXCALIBUR_HELMET);
    }

    private void registerPrimaryWeaponModels() {

    }

    private void registerSecondaryWeaponModels() {

    }

    private void registerMeleeWeaponModels() {

    }

    private void registerSpawnEggs() {

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
