package com.ombremoon.tennocraft.common.init;

import com.ombremoon.tennocraft.TennoCraft;
import com.ombremoon.tennocraft.common.init.block.TCBlocks;
import com.ombremoon.tennocraft.common.init.item.TCFrames;
import com.ombremoon.tennocraft.common.init.item.TCItems;
import com.ombremoon.tennocraft.common.init.item.TCMods;
import com.ombremoon.tennocraft.common.init.item.TCWeapons;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class TCCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TennoCraft.MOD_ID);

    public static RegistryObject<CreativeModeTab> TC_FRAMES = CREATIVE_MODE_TABS.register("tc_frames_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(TCFrames.VOLT_KEY.get()))
                    .title(Component.translatable("creativemodetab.tc_frames_tab")).build());

    public static RegistryObject<CreativeModeTab> TC_WEAPONS = CREATIVE_MODE_TABS.register("tc_weapons_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(TCWeapons.LATO.get()))
                    .title(Component.translatable("creativemodetab.tc_weapons_tab")).build());

    public static RegistryObject<CreativeModeTab> TC_BLOCKS = CREATIVE_MODE_TABS.register("tc_blocks_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(TCBlocks.ARSENAL_BLOCK.get()))
                    .title(Component.translatable("creativemodetab.tc_blocks_tab")).build());

    public static RegistryObject<CreativeModeTab> TC_SPAWN_EGGS = CREATIVE_MODE_TABS.register("tc_spawn_eggs_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(TCItems.GRINEER_LANCER_SPAWN_EGG.get()))
                    .title(Component.translatable("creativemodetab.tc_spawn_eggs_tab")).build());

    private static void registerCustomTabObjects(final BuildCreativeModeTabContentsEvent event) {
        registerTCItems(event);
        registerTCWeapons(event);
        registerTCBlocks(event);
        registerTCSpawnEggs(event);
    }

    private static void registerTCItems(final BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == TCCreativeModeTabs.TC_FRAMES.get()) {
            event.accept(TCFrames.VOLT_KEY);
            event.accept(TCFrames.EXCALIBUR_KEY);
            event.accept(TCFrames.MAG_KEY);
            event.accept(TCMods.CONTINUITY);
            event.accept(TCMods.NARROW_MINDED);
        }
    }

    private static void registerTCWeapons(final BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == TCCreativeModeTabs.TC_WEAPONS.get()) {
            //SECONDARY
            event.accept(TCWeapons.LATO);
            event.accept(TCWeapons.ANGSTRUM);
            event.accept(TCWeapons.SKANA);
            event.accept(TCMods.HEATED_CHARGE);
            event.accept(TCMods.MOLTEN_IMPACT);
            event.accept(TCMods.VOLTAIC_EDGE);
        }
    }

    private static void registerTCBlocks(final BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == TCCreativeModeTabs.TC_BLOCKS.get()) {
            event.accept(TCBlocks.ARSENAL_BLOCK);
        }
    }

    private static void registerTCSpawnEggs(final BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == TCCreativeModeTabs.TC_SPAWN_EGGS.get()) {
            event.accept(TCItems.GRINEER_LANCER_SPAWN_EGG);
        }
    }

    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(EventPriority.NORMAL, false, BuildCreativeModeTabContentsEvent.class, TCCreativeModeTabs::registerCustomTabObjects);
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}
