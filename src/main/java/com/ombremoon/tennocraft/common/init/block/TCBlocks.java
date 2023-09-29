package com.ombremoon.tennocraft.common.init.block;

import com.ombremoon.tennocraft.TennoCraft;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TCBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TennoCraft.MOD_ID);

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }
}
