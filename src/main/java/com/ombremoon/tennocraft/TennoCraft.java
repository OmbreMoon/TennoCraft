package com.ombremoon.tennocraft;

import com.mojang.logging.LogUtils;
import com.ombremoon.tennocraft.client.ClientEvents;
import com.ombremoon.tennocraft.common.init.TCCreativeModeTabs;
import com.ombremoon.tennocraft.common.init.TCMenuTypes;
import com.ombremoon.tennocraft.common.init.block.TCBlockEntities;
import com.ombremoon.tennocraft.common.init.block.TCBlocks;
import com.ombremoon.tennocraft.common.init.custom.FrameAbilities;
import com.ombremoon.tennocraft.common.init.custom.FrameAttributes;
import com.ombremoon.tennocraft.common.init.entity.TCEntities;
import com.ombremoon.tennocraft.common.init.entity.TCEntityAttributes;
import com.ombremoon.tennocraft.common.init.entity.TCMobEffects;
import com.ombremoon.tennocraft.common.init.item.TCItems;
import com.ombremoon.tennocraft.common.network.TCMessages;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
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
        TCBlockEntities.BLOCK_ENTITY_TYPES.register(modEventBus);
        TCItems.register(modEventBus);
        TCCreativeModeTabs.register(modEventBus);
        TCEntities.register(modEventBus);
        TCMenuTypes.register(modEventBus);
        TCMobEffects.register(modEventBus);
        TCEntityAttributes.init(modEventBus);
        FrameAbilities.register(modEventBus);
        FrameAttributes.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(ClientEvents.ClientModEvents::init);
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
