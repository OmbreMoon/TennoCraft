package com.ombremoon.tennocraft.client;

import com.ombremoon.tennocraft.TennoCraft;
//import com.ombremoon.tennocraft.client.model.frames.VoltModel;
import com.ombremoon.tennocraft.client.gui.ArsenalGUI;
import com.ombremoon.tennocraft.client.gui.PlayerArsenalGUI;
import com.ombremoon.tennocraft.client.model.frames.ExcaliburModel;
import com.ombremoon.tennocraft.client.model.frames.VoltModel;
import com.ombremoon.tennocraft.client.render.layers.ExcaliburLayer;
import com.ombremoon.tennocraft.client.render.layers.VoltLayer;
import com.ombremoon.tennocraft.common.init.TCMenuTypes;
import com.ombremoon.tennocraft.common.network.TCMessages;
import com.ombremoon.tennocraft.common.network.packet.server.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientEvents {

    @Mod.EventBusSubscriber(modid = TennoCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModBusEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            MenuScreens.register(TCMenuTypes.PLAYER_ARSENAL_MENU.get(), PlayerArsenalGUI::new);
            MenuScreens.register(TCMenuTypes.ARSENAL_MENU.get(), ArsenalGUI::new);
        }

        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBinds.OPEN_PLAYER_ARSENAL_BINDING);
            event.register(KeyBinds.ABILITY_ONE_BINDING);
            event.register(KeyBinds.ABILITY_TWO_BINDING);
            event.register(KeyBinds.ABILITY_THREE_BINDING);
            event.register(KeyBinds.ABILITY_FOUR_BINDING);
        }

        @SubscribeEvent
        public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(VoltModel.VOLT_LAYER_LOCATION, VoltModel::createBodyLayer);
            event.registerLayerDefinition(ExcaliburModel.EXCALIBUR_LAYER_LOCATION, ExcaliburModel::createBodyLayer);
        }

        @SubscribeEvent
        public static void registerLayerDefinitions(EntityRenderersEvent.AddLayers event) {
            for (final String skin : event.getSkins()) {
                final LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> playerRenderer = event.getSkin(skin);
                if (playerRenderer == null)
                    continue;
                playerRenderer.addLayer(new VoltLayer<>(playerRenderer));
                playerRenderer.addLayer(new ExcaliburLayer<>(playerRenderer));
            }
        }
    }

    @Mod.EventBusSubscriber(modid = TennoCraft.MOD_ID, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onAbilityInput(InputEvent.Key event) {
            if (KeyBinds.OPEN_PLAYER_ARSENAL_BINDING.consumeClick()) {
                TCMessages.sendToServer(new ServerboundOpenArsenalPacket());
            }
            if (KeyBinds.TRANSFERENCE_BINDING.consumeClick()) {
                TCMessages.sendToServer(new ServerboundTransferencePacket());
            }
            if (KeyBinds.ABILITY_ONE_BINDING.consumeClick()) {
                TCMessages.sendToServer(new ServerboundAbilityOnePacket());
            }
            if (KeyBinds.ABILITY_TWO_BINDING.consumeClick()) {
                TCMessages.sendToServer(new ServerboundAbilityTwoPacket());
            }
            if (KeyBinds.ABILITY_THREE_BINDING.consumeClick()) {
                TCMessages.sendToServer(new ServerboundAbilityThreePacket());
            }
            if (KeyBinds.ABILITY_FOUR_BINDING.consumeClick()) {
                TCMessages.sendToServer(new ServerboundAbilityFourPacket());
            }
        }
    }
}
