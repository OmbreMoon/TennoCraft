package com.ombremoon.tennocraft.event;

import com.ombremoon.tennocraft.TennoCraft;
import com.ombremoon.tennocraft.object.capability.ArsenalCapabilityProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

@Mod.EventBusSubscriber(modid = TennoCraft.MOD_ID)
public class TCEvents {

    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            if (!event.getObject().getCapability(ArsenalCapabilityProvider.ARSENAL_CAPABILITY).isPresent()) {
                ArsenalCapabilityProvider provider = new ArsenalCapabilityProvider();
                event.addCapability(ArsenalCapabilityProvider.ARSENAL_LOCATION, provider);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerDeath(PlayerEvent.Clone event) {
        event.getOriginal().reviveCaps();
    }

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();
        Item item = stack.getItem();
        event.addCapability(CuriosCapability.ID_ITEM, CuriosApi.createCurioProvider(new ICurio() {

            @Override
            public ItemStack getStack() {
                return stack;
            }

            @Override
            public void curioTick(SlotContext slotContext) {
                // ticking logic here
            }
        }));
    }
}
