package com.ombremoon.tennocraft.client;

import com.ombremoon.tennocraft.TennoCraft;
import com.ombremoon.tennocraft.common.network.TCMessages;
import com.ombremoon.tennocraft.common.network.packet.server.ServerboundAbilityFourPacket;
import com.ombremoon.tennocraft.common.network.packet.server.ServerboundAbilityOnePacket;
import com.ombremoon.tennocraft.common.network.packet.server.ServerboundAbilityThreePacket;
import com.ombremoon.tennocraft.common.network.packet.server.ServerboundAbilityTwoPacket;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ClientEvents {

    @Mod.EventBusSubscriber(modid = TennoCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModBusEvents {

        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBinds.ABILITY_ONE_BINDING);
            event.register(KeyBinds.ABILITY_TWO_BINDING);
            event.register(KeyBinds.ABILITY_THREE_BINDING);
            event.register(KeyBinds.ABILITY_FOUR_BINDING);
        }

    }

    @Mod.EventBusSubscriber(modid = TennoCraft.MOD_ID, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onAbilityInput(InputEvent.Key event) {
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
