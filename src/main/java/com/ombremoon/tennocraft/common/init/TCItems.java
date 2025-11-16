package com.ombremoon.tennocraft.common.init;

import com.ombremoon.tennocraft.common.modholder.RangedWeaponHandler;
import com.ombremoon.tennocraft.common.modholder.api.mod.ModInstance;
import com.ombremoon.tennocraft.common.modholder.api.mod.Modification;
import com.ombremoon.tennocraft.common.world.SchemaDirectory;
import com.ombremoon.tennocraft.common.world.item.DebugItem;
import com.ombremoon.tennocraft.common.world.item.ModItem;
import com.ombremoon.tennocraft.common.world.item.TransferenceKeyItem;
import com.ombremoon.tennocraft.common.world.item.weapon.SecondaryWeapon;
import com.ombremoon.tennocraft.main.Constants;
import com.ombremoon.tennocraft.main.Keys;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class TCItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Constants.MOD_ID);
    public static final List<Supplier<? extends Item>> SIMPLE_ITEM_LIST = new ArrayList<>();
    public static final List<Supplier<? extends Item>> EXCLUDED_ITEMS = new ArrayList<>();
    public static final List<Supplier<? extends Item>> BLOCK_ITEM_LIST = new ArrayList<>();

    public static final Supplier<Item> DEBUG = ITEMS.register("debug", () -> new DebugItem(new Item.Properties()));
    public static final Supplier<Item> TRANSFERENCE_KEY = registerItem("transference_key", () -> new TransferenceKeyItem(itemProperties()), false, true);
    public static final Supplier<Item> SECONDARY_WEAPON = registerItem("secondary_weapon", () -> new SecondaryWeapon(itemProperties()), true, true);
    public static final Supplier<Item> MOD = registerItem("mod", () -> new ModItem(itemProperties()), false, true);

    public static final Supplier<CreativeModeTab> FRAME_TAB = CREATIVE_MODE_TABS.register("frames", () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP,0)
            .icon(() -> new ItemStack(TRANSFERENCE_KEY.get()))
            .displayItems(
                    (itemDisplayParameters, output)-> {
                        ITEMS.getEntries().forEach((registryObject)-> {
                            if (!EXCLUDED_ITEMS.contains(registryObject))
                                output.accept(new ItemStack(registryObject.get()));
                        });

                        SchemaDirectory.getFrames().forEach(schema -> {
                            output.accept(TransferenceKeyItem.createWithFrame(schema));
                        });
                    }).title(Component.translatable("itemGroup.tennocraft"))
            .build());
    public static final Supplier<CreativeModeTab> RANGED_TAB = CREATIVE_MODE_TABS.register("ranged", () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP,0)
            .icon(() -> new ItemStack(SECONDARY_WEAPON.get()))
            .displayItems(
                    (itemDisplayParameters, output)-> {
                        SchemaDirectory.getRangedWeapons().forEach(schema -> {
                            ItemStack stack = new ItemStack(SECONDARY_WEAPON.get());
                            stack.set(TCData.SCHEMA, schema);
                            stack.set(TCData.RANGED_WEAPON_HANDLER, new RangedWeaponHandler(new CompoundTag(), itemDisplayParameters.holders().holderOrThrow(schema.schemaKey()).value(), itemDisplayParameters.holders()));
                            output.accept(stack);
                        });
                    }).title(Component.translatable("itemGroup.ranged"))
            .build());
    public static final Supplier<CreativeModeTab> MOD_TAB = CREATIVE_MODE_TABS.register("mods", () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP,0)
            .icon(() -> new ItemStack(MOD.get()))
            .displayItems(
                    (itemDisplayParameters, output)-> {
                        itemDisplayParameters.holders().lookup(Keys.MOD).ifPresent(lookup -> {
                            generateMods(output, lookup);
                        });
                    }).title(Component.translatable("itemGroup.mods"))
            .build());

    private static void generateMods(
            CreativeModeTab.Output output, HolderLookup<Modification> mods
    ) {
        mods.listElements()
                .map(mod -> ModItem.createForMod(new ModInstance(mod, 0)))
                .forEach(output::accept);
    }

    public static Supplier<Item> registerSimpleItem(String name) {
        return registerItem(name, () -> new Item(itemProperties()));
    }

    public static Supplier<Item> registerItem(String name, Supplier<Item> itemSupplier) {
        return registerItem(name, itemSupplier, false);
    }

    public static Supplier<Item> registerItem(String name, Supplier<Item> itemSupplier, boolean excludeModel) {
        return registerItem(name, itemSupplier, excludeModel, false);
    }

    public static Supplier<Item> registerItem(String name, Supplier<Item> itemSupplier, boolean excludeModel, boolean excludeTab) {
        Supplier<Item> item = ITEMS.register(name, itemSupplier);

        if (!excludeModel)
            SIMPLE_ITEM_LIST.add(item);

        if (excludeTab)
            EXCLUDED_ITEMS.add(item);

        return item;
    }

    public static Supplier<Item> registerBlockItem(String name, DeferredBlock<? extends Block> block) {
        Supplier<Item> item = ITEMS.register(name, () -> new BlockItem(block.get(), itemProperties()));
        BLOCK_ITEM_LIST.add(item);
        return item;
    }

    public static Item.Properties itemProperties() {
        return new Item.Properties();
    }

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}
