package com.ombremoon.tennocraft.common.init.item;

import com.ombremoon.tennocraft.TennoCraft;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class TCItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TennoCraft.MOD_ID);

    public static <T extends Item> RegistryObject<T> registerItem(String name, Supplier<T> item) {
        RegistryObject<T> registryObject = TCItems.ITEMS.register(name, item);

        return registryObject;
    }

    public static void register(IEventBus modEventBus) {
        TCFrames.init();
        TCMods.init();
        ITEMS.register(modEventBus);
    }
}
