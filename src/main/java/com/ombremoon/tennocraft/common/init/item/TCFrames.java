package com.ombremoon.tennocraft.common.init.item;

import com.ombremoon.tennocraft.TennoCraft;
import com.ombremoon.tennocraft.object.item.mineframe.ExcaliburFrameItem;
import com.ombremoon.tennocraft.object.item.mineframe.FrameArmorItem;
import com.ombremoon.tennocraft.object.item.mineframe.TransferenceTokenItem;
import com.ombremoon.tennocraft.object.item.mineframe.helmet.ExcaliburHelmetItem;
import com.ombremoon.tennocraft.object.item.mineframe.helmet.VoltHelmetItem;
import com.ombremoon.tennocraft.object.item.mineframe.helmet.FrameHelmetItem;
import com.ombremoon.tennocraft.object.item.mineframe.VoltFrameItem;
import com.ombremoon.tennocraft.object.item.mineframe.token.VoltToken;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class TCFrames {
    public static void init() {}

    //TRANSFERENCE TOKENS
    public static final RegistryObject<Item> VOLT_TOKEN = registerTokenItem("volt_token",
            () -> new VoltToken(new Item.Properties()));

    public static final RegistryObject<Item> VOLT_HELMET = registerFrameItem("volt_helmet",
            () -> new VoltHelmetItem(new Item.Properties()) {});
    public static final RegistryObject<Item> VOLT_CHASSIS = registerFrameItem("volt_chassis",
            () -> new VoltFrameItem(EquipmentSlot.CHEST, new Item.Properties()) {});
    public static final RegistryObject<Item> VOLT_LEGGINGS = registerFrameItem("volt_leggings",
            () -> new VoltFrameItem(EquipmentSlot.LEGS, new Item.Properties()) {});
    public static final RegistryObject<Item> VOLT_BOOTS = registerFrameItem("volt_boots",
            () -> new VoltFrameItem(EquipmentSlot.FEET, new Item.Properties()) {});
    public static final RegistryObject<Item> EXCALIBUR_HELMET = registerFrameItem("excalibur_helmet",
            () -> new ExcaliburHelmetItem(new Item.Properties()) {});
    public static final RegistryObject<Item> EXCALIBUR_CHASSIS = registerFrameItem("excalibur_chassis",
            () -> new ExcaliburFrameItem(EquipmentSlot.CHEST, new Item.Properties()) {});

    private static <T extends Item> RegistryObject<T> registerFrameItem(String name, Supplier<T> itemSupplier) {
        return TCItems.registerItem(name, itemSupplier);
    }

    private static <T extends Item> RegistryObject<T> registerTokenItem(String name, Supplier<T> itemSupplier) {
        return TCItems.registerItem(name, itemSupplier);
    }
}
