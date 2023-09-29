package com.ombremoon.tennocraft;

import com.mojang.logging.LogUtils;
import com.ombremoon.tennocraft.common.init.TCCreativeModeTabs;
import com.ombremoon.tennocraft.common.init.block.TCBlocks;
import com.ombremoon.tennocraft.common.init.custom.FrameAbilities;
import com.ombremoon.tennocraft.common.init.item.TCItems;
import com.ombremoon.tennocraft.common.network.TCMessages;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

@Mod(TennoCraft.MOD_ID)
public class TennoCraft {
    public static final String MOD_ID = "tennocraft";
    public static final Logger LOGGER = LogUtils.getLogger();

    public TennoCraft() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);

        TCBlocks.register(modEventBus);
        TCItems.register(modEventBus);
//        TCCreativeModeTabs.init(modEventBus);
        TCCreativeModeTabs.register(modEventBus);
        FrameAbilities.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        TCMessages.register();
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    public static ResourceLocation customLocation(String name) {
        return new ResourceLocation(TennoCraft.MOD_ID, name);
    }
}
