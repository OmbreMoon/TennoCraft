package com.ombremoon.tennocraft.common.init.item;

import com.ombremoon.tennocraft.common.init.custom.FrameAttributes;
import com.ombremoon.tennocraft.object.item.mod.AbstractModItem;
import com.ombremoon.tennocraft.object.item.mod.CorruptedModItem;
import com.ombremoon.tennocraft.object.item.mod.ModType;
import com.ombremoon.tennocraft.object.item.mod.SimpleModItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class TCMods {
    public static void init() {}

    public static final RegistryObject<Item> CONTINUITY = registerModItem("continuity",
            () -> new SimpleModItem(ModType.MINEFRAME, "Continuity", 5, AbstractModItem.ModRarity.RARE, FrameAttributes.DURATION, 0.30F, new Item.Properties()));

    //CORRUPTED
    public static final RegistryObject<Item> NARROW_MINDED = registerModItem("narrow_minded",
            () -> new CorruptedModItem(ModType.MINEFRAME, "Narrow Minded", FrameAttributes.DURATION, 0.45F, FrameAttributes.RANGE, -0.3F, new Item.Properties()));

    private static <T extends Item> RegistryObject<T> registerModItem(String name, Supplier<T> itemSupplier) {
        return TCItems.registerItem(name, itemSupplier);
    }
}
