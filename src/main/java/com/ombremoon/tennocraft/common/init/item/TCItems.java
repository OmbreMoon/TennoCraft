package com.ombremoon.tennocraft.common.init.item;

import com.ombremoon.tennocraft.TennoCraft;
import com.ombremoon.tennocraft.common.init.entity.TCMobs;
import com.ombremoon.tennocraft.object.item.DebugItem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class TCItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TennoCraft.MOD_ID);

    public static final RegistryObject<Item> DEBUG = registerItem("debug", () -> new DebugItem(new Item.Properties()));

    public static final RegistryObject<Item> GRINEER_LANCER_SPAWN_EGG = spawnEggItem("grineer_lancer", TCMobs.GRINEER_LANCER, 402812, 3553336);


    public static <T extends Item> RegistryObject<T> registerItem(String name, Supplier<T> item) {
        RegistryObject<T> registryObject = TCItems.ITEMS.register(name, item);

        return registryObject;
    }

    private static RegistryObject<Item> spawnEggItem(String name, Supplier<? extends EntityType<? extends Mob>> entityType, int primaryColor, int secondaryColor) {
        return TCItems.registerItem(name + "_spawn_egg", () -> new ForgeSpawnEggItem(entityType, primaryColor, secondaryColor, new Item.Properties()));
    }

    public static void register(IEventBus modEventBus) {
        TCFrames.init();
        TCWeapons.init();
        TCMods.init();
        ITEMS.register(modEventBus);
    }
}
