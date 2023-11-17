package com.ombremoon.tennocraft.common.init.block;

import com.ombremoon.tennocraft.TennoCraft;
import com.ombremoon.tennocraft.object.block.entity.ArsenalBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TCBlockEntities {
    public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, TennoCraft.MOD_ID);

    public static final RegistryObject<BlockEntityType<ArsenalBlockEntity>> ARSENAL_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("arsenal_block_entity",
            () -> BlockEntityType.Builder.of(ArsenalBlockEntity::new, TCBlocks.ARSENAL_BLOCK.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITY_TYPES.register(eventBus);
    }
}
