package com.ombremoon.tennocraft.common.init.item;

import com.ombremoon.tennocraft.object.item.mineframe.ExcaliburFrame;
import com.ombremoon.tennocraft.object.item.mineframe.FrameArmorItem;
import com.ombremoon.tennocraft.object.item.mineframe.VoltFrameItem;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class TCFrames {
    public static void init() {}

    public static final RegistryObject<Item> VOLT_HELMET = registerFrameItem("volt_helmet",
            () -> new VoltFrameItem(ArmorMaterials.DIAMOND, ArmorItem.Type.HELMET, new Item.Properties()) {});
    public static final RegistryObject<Item> VOLT_CHASSIS = registerFrameItem("volt_chassis",
            () -> new VoltFrameItem(ArmorMaterials.DIAMOND, ArmorItem.Type.CHESTPLATE, new Item.Properties()) {});
    public static final RegistryObject<Item> VOLT_LEGGINGS = registerFrameItem("volt_leggings",
            () -> new VoltFrameItem(ArmorMaterials.DIAMOND, ArmorItem.Type.LEGGINGS, new Item.Properties()) {});
    public static final RegistryObject<Item> VOLT_BOOTS = registerFrameItem("volt_boots",
            () -> new VoltFrameItem(ArmorMaterials.DIAMOND, ArmorItem.Type.BOOTS, new Item.Properties()) {});
    public static final RegistryObject<Item> EXCALIBUR_HELMET = registerFrameItem("excalibur_helmet",
            () -> new ExcaliburFrame(ArmorMaterials.DIAMOND, ArmorItem.Type.HELMET, new Item.Properties()) {});

    private static FrameArmorItem.FrameType getFrameType(FrameArmorItem<?> frameArmorItem) {
        return frameArmorItem.getFrameType();
    }

    private static <T extends Item> RegistryObject<T> registerFrameItem(String name, Supplier<T> itemSupplier) {
        return TCItems.registerItem(name, itemSupplier);
    }
}
