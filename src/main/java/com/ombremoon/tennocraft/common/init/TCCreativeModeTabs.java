package com.ombremoon.tennocraft.common.init;

import com.ombremoon.tennocraft.TennoCraft;
import com.ombremoon.tennocraft.common.init.item.TCFrames;
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
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(TCFrames.VOLT_HELMET.get()))
                    .title(Component.translatable("creativemodetab.tc_frames_tab")).build());

    private static void registerCustomTabObjects(final BuildCreativeModeTabContentsEvent event) {
        registerTCItems(event);
    }

    private static void registerTCItems(final BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == TCCreativeModeTabs.TC_FRAMES.get()) {
            event.accept(TCFrames.VOLT_HELMET);
            event.accept(TCFrames.VOLT_CHASSIS);
            event.accept(TCFrames.VOLT_LEGGINGS);
            event.accept(TCFrames.VOLT_BOOTS);
            event.accept(TCFrames.EXCALIBUR_HELMET);
        }
    }

    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(EventPriority.NORMAL, false, BuildCreativeModeTabContentsEvent.class, TCCreativeModeTabs::registerCustomTabObjects);
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}
