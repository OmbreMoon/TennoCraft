package com.ombremoon.tennocraft.common.init;

import com.ombremoon.tennocraft.TennoCraft;
import com.ombremoon.tennocraft.object.world.inventory.ArsenalMenu;
import com.ombremoon.tennocraft.object.world.inventory.PlayerArsenalMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TCMenuTypes {
    public static DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, TennoCraft.MOD_ID);

    public static final RegistryObject<MenuType<ArsenalMenu>> ARSENAL_MENU = registerMenuType("arsenal_menu", ArsenalMenu::new);
    public static final RegistryObject<MenuType<PlayerArsenalMenu>> PLAYER_ARSENAL_MENU = registerMenuType("player_arsenal_menu", PlayerArsenalMenu::fromNetwork);

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(String name, IContainerFactory<T> containerFactory) {
        return MENU_TYPES.register(name, () -> IForgeMenuType.create(containerFactory));
    }

    public static void register(IEventBus modEventBus) {
        MENU_TYPES.register(modEventBus);
    }
}
