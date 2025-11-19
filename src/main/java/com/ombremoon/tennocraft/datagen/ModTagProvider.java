package com.ombremoon.tennocraft.datagen;

import com.ombremoon.tennocraft.common.init.TCStatusEffects;
import com.ombremoon.tennocraft.common.init.TCTags;
import com.ombremoon.tennocraft.common.init.schemas.TCFrames;
import com.ombremoon.tennocraft.common.init.schemas.TCMeleeWeapons;
import com.ombremoon.tennocraft.common.init.schemas.TCSecondaryWeapons;
import com.ombremoon.tennocraft.common.api.weapon.schema.Schema;
import com.ombremoon.tennocraft.main.Constants;
import com.ombremoon.tennocraft.main.Keys;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class ModTagProvider {

    public static class Items extends TagsProvider<Item> {

        public Items(PackOutput p_256596_, CompletableFuture<HolderLookup.Provider> p_256513_, @Nullable ExistingFileHelper existingFileHelper) {
            super(p_256596_, Registries.ITEM, p_256513_, Constants.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {

        }

        public void populateTag(TagKey<Item> tag, Supplier<Item>... items){
            for (Supplier<Item> item : items) {
                tag(tag).add(BuiltInRegistries.ITEM.getResourceKey(item.get()).get());
            }
        }
    }

    public static class Blocks extends TagsProvider<Block> {

        public Blocks(PackOutput pGenerator, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
            super(pGenerator, Registries.BLOCK, provider, Constants.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {
        }

        public void populateTag(TagKey<Block> tag, Block... blocks){
            for (Block block : blocks) {
                tag(tag).add(BuiltInRegistries.BLOCK.getResourceKey(block).get());
            }
        }
    }

    public static class EntityTypes extends EntityTypeTagsProvider {
        public EntityTypes(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, provider, Constants.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {

        }
    }

    public static class MobEffects extends TagsProvider<MobEffect> {
        public MobEffects(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, Registries.MOB_EFFECT, provider, Constants.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            populateTag(TCTags.MobEffects.STATUS_EFFECT,
                    TCStatusEffects.KNOCKBACK
//                    TCStatusEffects.WEAKENED,
//                    TCStatusEffects.BLEED,
//                    TCStatusEffects.IGNITE,
//                    TCStatusEffects.FREEZE,
//                    TCStatusEffects.TESLA_CHAIN,
//                    TCStatusEffects.POISON,
//                    TCStatusEffects.DETONATE,
//                    TCStatusEffects.CORROSION,
//                    TCStatusEffects.DISRUPT,
//                    TCStatusEffects.GAS_CLOUD,
//                    TCStatusEffects.CONFUSION,
//                    TCStatusEffects.VIRUS
            );
        }

        public void populateTag(TagKey<MobEffect> tag, Holder<MobEffect>... effects){
            for (Holder<MobEffect> effect : effects) {
                tag(tag).add(effect.getKey());
            }
        }

        public void populateTag(TagKey<MobEffect> tag, TagKey<MobEffect> effect){
            tag(tag).addTags(effect);
        }
    }

    public static class Schemas extends TagsProvider<Schema> {

        public Schemas(PackOutput pGenerator, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
            super(pGenerator, Keys.SCHEMA, provider, Constants.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {
            populateTag(TCTags.Schemas.FRAME, TCFrames.VOLT);
            populateTag(TCTags.Schemas.PISTOL, TCSecondaryWeapons.ATOMOS);
            populateTag(TCTags.Schemas.MELEE, TCMeleeWeapons.ORTHOS);
        }

        public void populateTag(TagKey<Schema> tag, ResourceKey<Schema>... schemas){
            for (ResourceKey<Schema> schema : schemas) {
                tag(tag).add(schema);
            }
        }
    }
}
