package com.ombremoon.tennocraft.common.init.entity;

import com.ombremoon.tennocraft.TennoCraft;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TCEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, TennoCraft.MOD_ID);

    public static void init() {
        TCProjectiles.init();
//        VCMiscEntities.init();
        TCMobs.init();
    }

    public static void register( IEventBus eventBus) {
        init();
        ENTITY_TYPES.register(eventBus);
    }
}
