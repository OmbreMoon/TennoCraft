package com.ombremoon.tennocraft.common.init.item;

import com.ombremoon.tennocraft.object.item.transference.ExcaliburKey;
import com.ombremoon.tennocraft.object.item.transference.MagKey;
import com.ombremoon.tennocraft.object.item.transference.VoltKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class TCFrames {
    public static void init() {}

    //TRANSFERENCE TOKENS
    public static final RegistryObject<Item> VOLT_KEY = registerTransferenceKey("volt_key",
            () -> new VoltKey(new Item.Properties()));
    public static final RegistryObject<Item> EXCALIBUR_KEY = registerTransferenceKey("excalibur_key",
            () -> new ExcaliburKey(new Item.Properties()));
    public static final RegistryObject<Item> MAG_KEY = registerTransferenceKey("mag_key",
            () -> new MagKey(new Item.Properties()));


    private static <T extends Item> RegistryObject<T> registerTransferenceKey(String name, Supplier<T> itemSupplier) {
        return TCItems.registerItem(name, itemSupplier);
    }
}
