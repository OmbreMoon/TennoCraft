package com.ombremoon.tennocraft.datagen;

import com.google.common.collect.ImmutableMap;
import com.ombremoon.tennocraft.common.init.mods.Modifications;
import com.ombremoon.tennocraft.common.modholder.api.mod.Modification;
import com.ombremoon.tennocraft.common.modholder.api.weapon.schema.Schema;
import com.ombremoon.tennocraft.common.init.schemas.TCFrames;
import com.ombremoon.tennocraft.common.init.TCItems;
import com.ombremoon.tennocraft.common.init.schemas.TCPrimaryWeapons;
import com.ombremoon.tennocraft.common.init.schemas.TCSecondaryWeapons;
import com.ombremoon.tennocraft.main.Constants;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class ModLangProvider extends LanguageProvider {

    protected static final Map<String, String> REPLACE_LIST = ImmutableMap.of(
            "tnt", "TNT",
            "sus", ""
    );

    public ModLangProvider(PackOutput gen) {
        super(gen, Constants.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        TCItems.ITEMS.getEntries().forEach(this::itemLang);
        TCFrames.FRAMES.forEach(this::schemaLang);
        TCPrimaryWeapons.PRIMARY_WEAPONS.forEach(this::schemaLang);
        TCSecondaryWeapons.SECONDARY_WEAPONS.forEach(this::schemaLang);
        Modifications.MODS.forEach(this::modLang);
        manualEntries();
    }

    protected void itemLang(DeferredHolder<Item, ? extends Item> entry) {
        if (!(entry.get() instanceof BlockItem) || entry.get() instanceof ItemNameBlockItem) {
            addItem(entry, checkReplace(entry));
        }
    }

    protected void blockLang(DeferredHolder<Block, ? extends Block> entry) {
        addBlock(entry, checkReplace(entry));
    }

    protected void entityLang(DeferredHolder<EntityType<?>, ? extends EntityType<?>> entry) {
        addEntityType(entry, checkReplace(entry));
    }

    protected void schemaLang(ResourceKey<Schema> key) {
        ResourceLocation id = key.location();
        String namespace = id.getNamespace();
        String[] name = id.getPath().split("/");
        String path = name[1];
        String translation = namespace + "." + name[0] + "." + path;
        add(translation, checkReplace(path));
    }

    protected void modLang(ResourceKey<Modification> key) {
        ResourceLocation location = key.location();
        String path = location.getPath();
        add("modification." + location.getNamespace() + "." + path, checkReplace(path));
    }

    protected void manualEntries() {
        add("tennocraft.frame.missing_description", "Missing Description");
        add("itemGroup.tennocraft", "TennoCraft");
        add("itemGroup.ranged", "TennoCraft Ranged Weapons");
        add("itemGroup.mods", "TennoCraft Mods");
    }

    protected String checkReplace(DeferredHolder<?, ?> holder) {
        return Arrays.stream(holder.getId().getPath().split("_"))
                .map(this::replaceFromList)
                .filter(s -> !s.isBlank())
                .collect(Collectors.joining(" "))
                .trim();
    }

    protected String checkReplace(String path) {
        return Arrays.stream(path.split("_"))
                .map(this::replaceFromList)
                .filter(s -> !s.isBlank())
                .collect(Collectors.joining(" "))
                .trim();
    }

    protected String replaceFromList(String string) {
        return REPLACE_LIST.containsKey(string) ? REPLACE_LIST.get(string) : StringUtils.capitalize(string);
    }

}
