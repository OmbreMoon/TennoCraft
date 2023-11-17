package com.ombremoon.tennocraft.common.datagen;

import com.ombremoon.tennocraft.common.init.block.TCBlocks;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class TCBlockLootTables extends BlockLootSubProvider {
    protected TCBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
this.dropSelf(TCBlocks.ARSENAL_BLOCK.get());
    }

    private void naturalBlockLootTables() {

    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return TCBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
