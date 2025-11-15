package com.ombremoon.tennocraft.common.modholder.api.mod;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.modholder.api.weapon.schema.Schema;
import com.ombremoon.tennocraft.common.modholder.api.mod.effects.ModAttributeEffect;
import com.ombremoon.tennocraft.main.Keys;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.*;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.HolderSetCodec;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.function.UnaryOperator;

public record Modification(Component name, Component description, ModDefinition definition, HolderSet<Modification> exclusiveSet, DataComponentMap effects) {
    public static final Modification EMPTY = new Modification(Component.empty(), Component.empty(), ModDefinition.EMPTY, HolderSet.empty(), DataComponentMap.builder().build());
    public static final int MAX_LEVEL = 10;
    public static final Codec<Modification> DIRECT_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    ComponentSerialization.CODEC.fieldOf("name").forGetter(Modification::name),
                    ComponentSerialization.CODEC.fieldOf("description").forGetter(Modification::description),
                    ModDefinition.CODEC.fieldOf("definition").forGetter(Modification::definition),
                    RegistryCodecs.homogeneousList(Keys.MOD)
                            .optionalFieldOf("exclusive_set", HolderSet.direct())
                            .forGetter(Modification::exclusiveSet),
                    TCModEffectComponents.CODEC.optionalFieldOf("effects", DataComponentMap.EMPTY).forGetter(Modification::effects)
            ).apply(instance, Modification::new)
    );
    public static final Codec<Holder<Modification>> CODEC = RegistryFileCodec.create(Keys.MOD, DIRECT_CODEC);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Modification>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Keys.MOD);

    public static Drain constantDrain(int cost) {
        return new Drain(cost, 0);
    }

    public static Drain dynamicDrain(int base, int perLevel) {
        return new Drain(base, perLevel);
    }

    public static Drain dynamicDrain(int base) {
        return new Drain(base, 1);
    }

    public static ModDefinition definition(
            HolderSet<Schema> supportedSchemas,
//            HolderSet<Schema> incompatibleSchemas,
            ModType type,
            ModPolarity polarity,
            Drain drain,
            int maxRank,
            ModRarity rarity,
            Variant variant
    ) {
        return new ModDefinition(supportedSchemas, /*Optional.of(incompatibleSchemas), */type, polarity, drain, maxRank, rarity, Optional.of(variant));
    }

    public static ModDefinition definition(
            HolderSet<Schema> supportedSchemas,
//            HolderSet<Schema> incompatibleSchemas,
            ModType type,
            ModPolarity polarity,
            Drain drain,
            int maxRank,
            ModRarity rarity
    ) {
        return new ModDefinition(supportedSchemas, /*Optional.of(incompatibleSchemas), */type, polarity, drain, maxRank, rarity, Optional.empty());
    }

/*    public static ModDefinition definition(
            HolderSet<Schema> supportedSchemas,
            ModType type,
            ModPolarity polarity,
            Drain drain,
            int maxRank,
            ModRarity rarity,
            Variant variant
    ) {
        return new ModDefinition(supportedSchemas, Optional.empty(), type, polarity, drain, maxRank, rarity, Optional.of(variant));
    }

    public static ModDefinition definition(
            HolderSet<Schema> supportedSchemas,
            ModType type,
            ModPolarity polarity,
            Drain drain,
            int maxRank,
            ModRarity rarity
    ) {
        return new ModDefinition(supportedSchemas, Optional.empty(), type, polarity, drain, maxRank, rarity, Optional.empty());
    }*/

    /*public HolderSet<Schema> getSupportedSchemas() {
        return this.definition.supportedSchemas;
    }

    public boolean isSupportedSchema(Holder<Schema> schema) {
        return this.definition.supportedSchemas.contains(schema);
    }

    public boolean isIncompatibleSchema(Holder<Schema> schema) {
        return this.definition.incompatibleSchemas.map(holders -> holders.contains(schema)).orElse(false);
    }*/

    public ModType getType() {
        return this.definition.type;
    }

    public ModPolarity getPolarity() {
        return this.definition.polarity;
    }

    public int getEnergyDrain(int rank) {
        return this.definition.drain.calculate(rank);
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }

    @Override
    public String toString() {
        return "Mod " + this.name.getString();
    }

    public static boolean areCompatible(Holder<Modification> first, Holder<Modification> second) {
        return !first.equals(second) && !first.value().exclusiveSet.contains(second) && !second.value().exclusiveSet.contains(first);
    }

    public static Component getFullName(Holder<Modification> mod, int level) {
        MutableComponent mutablecomponent = mod.value().name.copy();
        return mutablecomponent;
    }

    public <T>List<T> getEffects(DataComponentType<List<T>> component) {
        return this.effects.getOrDefault(component, List.of());
    }

    public static LootContext damageContext(ServerLevel level, int modRank, Entity entity, DamageSource damageSource) {
        LootParams lootparams = new LootParams.Builder(level)
                .withParameter(LootContextParams.THIS_ENTITY, entity)
                .withParameter(Keys.MOD_RANK, modRank)
                .withParameter(LootContextParams.ORIGIN, entity.position())
                .withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
                .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, damageSource.getEntity())
                .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, damageSource.getDirectEntity())
                .create(LootContextParamSets.ENCHANTED_DAMAGE);
        return new LootContext.Builder(lootparams).create(Optional.empty());
    }

    public static LootContext itemContext(ServerLevel level, int modRank, ItemStack tool) {
        LootParams lootparams = new LootParams.Builder(level)
                .withParameter(LootContextParams.TOOL, tool)
                .withParameter(Keys.MOD_RANK, modRank)
                .create(LootContextParamSets.ENCHANTED_ITEM);
        return new LootContext.Builder(lootparams).create(Optional.empty());
    }

    public static LootContext locationContext(ServerLevel level, int modRank, Entity entity, boolean enchantmentActive) {
        LootParams lootparams = new LootParams.Builder(level)
                .withParameter(LootContextParams.THIS_ENTITY, entity)
                .withParameter(Keys.MOD_RANK, modRank)
                .withParameter(LootContextParams.ORIGIN, entity.position())
                .withParameter(LootContextParams.ENCHANTMENT_ACTIVE, enchantmentActive)
                .create(LootContextParamSets.ENCHANTED_LOCATION);
        return new LootContext.Builder(lootparams).create(Optional.empty());
    }

    public static LootContext entityContext(ServerLevel level, int modRank, Entity entity, Vec3 origin) {
        LootParams lootparams = new LootParams.Builder(level)
                .withParameter(LootContextParams.THIS_ENTITY, entity)
                .withParameter(Keys.MOD_RANK, modRank)
                .withParameter(LootContextParams.ORIGIN, origin)
                .create(LootContextParamSets.ENCHANTED_ENTITY);
        return new LootContext.Builder(lootparams).create(Optional.empty());
    }

    public static LootContext blockHitContext(ServerLevel level, int modRank, Entity entity, Vec3 origin, BlockState state) {
        LootParams lootparams = new LootParams.Builder(level)
                .withParameter(LootContextParams.THIS_ENTITY, entity)
                .withParameter(Keys.MOD_RANK, modRank)
                .withParameter(LootContextParams.ORIGIN, origin)
                .withParameter(LootContextParams.BLOCK_STATE, state)
                .create(LootContextParamSets.HIT_BLOCK);
        return new LootContext.Builder(lootparams).create(Optional.empty());
    }

    public static Modification.Builder mod(ModDefinition definition) {
        return new Builder(definition);
    }

    public static class Builder {
        private final ModDefinition definition;
        private Component description = Component.empty();
        private HolderSet<Modification> exclusiveSet = HolderSet.direct();
        private final Map<DataComponentType<?>, List<?>> effectLists = new HashMap<>();
        private final DataComponentMap.Builder effectMapBuilder = DataComponentMap.builder();
        protected UnaryOperator<MutableComponent> nameFactory = UnaryOperator.identity();

        Builder(ModDefinition definition) {
            this.definition = definition;
        }

        public Builder exclusiveWith(HolderSet<Modification> exclusiveSet) {
            this.exclusiveSet = exclusiveSet;
            return this;
        }

        public <E> Builder withEffect(DataComponentType<List<ConditionalModEffect<E>>> componentType, E effect, AuraEffect aura, int duration, LootItemCondition.Builder requirements) {
            this.getEffectsList(componentType).add(new ConditionalModEffect<>(effect, Optional.of(requirements.build()), Optional.of(aura), Optional.of(duration)));
            return this;
        }

        public <E> Builder withEffect(DataComponentType<List<ConditionalModEffect<E>>> componentType, E effect, AuraEffect aura, LootItemCondition.Builder requirements) {
            this.getEffectsList(componentType).add(new ConditionalModEffect<>(effect, Optional.of(requirements.build()), Optional.of(aura), Optional.empty()));
            return this;
        }

        public <E> Builder withEffect(DataComponentType<List<ConditionalModEffect<E>>> componentType, E effect, AuraEffect aura, int duration) {
            this.getEffectsList(componentType).add(new ConditionalModEffect<>(effect, Optional.empty(), Optional.of(aura), Optional.of(duration)));
            return this;
        }

        public <E> Builder withEffect(DataComponentType<List<ConditionalModEffect<E>>> componentType, E effect, AuraEffect aura) {
            this.getEffectsList(componentType).add(new ConditionalModEffect<>(effect, Optional.empty(), Optional.of(aura), Optional.empty()));
            return this;
        }

        public <E> Builder withEffect(DataComponentType<List<ConditionalModEffect<E>>> componentType, E effect, LootItemCondition.Builder requirements) {
            this.getEffectsList(componentType).add(new ConditionalModEffect<>(effect, Optional.of(requirements.build()), Optional.empty(), Optional.empty()));
            return this;
        }

        public <E> Builder withEffect(DataComponentType<List<ConditionalModEffect<E>>> componentType, E effect) {
            this.getEffectsList(componentType).add(new ConditionalModEffect<>(effect, Optional.empty(), Optional.empty(), Optional.empty()));
            return this;
        }

        public Builder withEffect(DataComponentType<List<ModAttributeEffect>> componentType, ModAttributeEffect effect) {
            this.getEffectsList(componentType).add(effect);
            return this;
        }

        public Builder withCustomName(UnaryOperator<MutableComponent> nameFactory) {
            this.nameFactory = nameFactory;
            return this;
        }

        public Builder description(Component description) {
            this.description = description;
            return this;
        }

        @SuppressWarnings("unchecked")
        private <E> List<E> getEffectsList(DataComponentType<List<E>> componentType) {
            return (List<E>) this.effectLists.computeIfAbsent(componentType, dataComponent -> {
                ArrayList<E> arrayList = new ArrayList<>();
                this.effectMapBuilder.set(componentType, arrayList);
                return arrayList;
            });
        }

        public Modification build(ResourceLocation location) {
            return new Modification(
                    this.nameFactory.apply(Component.translatable(Util.makeDescriptionId("modification", location))),
                    this.description, this.definition, this.exclusiveSet, this.effectMapBuilder.build()
            );
        }
    }

    public record ModDefinition(HolderSet<Schema> supportedSchemas, /*Optional<HolderSet<Schema>> incompatibleSchemas, */ModType type, ModPolarity polarity, Drain drain, int maxRank, ModRarity rarity, Optional<Variant> variant) {
        public static final MapCodec<ModDefinition> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        RegistryCodecs.homogeneousList(Keys.SCHEMA).fieldOf("supports").forGetter(ModDefinition::supportedSchemas),
//                        RegistryCodecs.homogeneousList(Keys.SCHEMA, Schema.DIRECT_CODEC, false).optionalFieldOf("incompatible").forGetter(ModDefinition::incompatibleSchemas),
                        ModType.CODEC.fieldOf("type").forGetter(ModDefinition::type),
                        ModPolarity.CODEC.fieldOf("polarity").forGetter(ModDefinition::polarity),
                        Drain.CODEC.fieldOf("drain").forGetter(ModDefinition::drain),
                        Codec.INT.fieldOf("maxRank").forGetter(ModDefinition::maxRank),
                        ModRarity.CODEC.fieldOf("rarity").forGetter(ModDefinition::rarity),
                        Variant.CODEC.optionalFieldOf("variant").forGetter(ModDefinition::variant)
                ).apply(instance, ModDefinition::new)
        );

        private static final ModDefinition EMPTY = new ModDefinition(HolderSet.empty(), /*Optional.empty(), */ModType.STANDARD, ModPolarity.ANY, constantDrain(0), 0, ModRarity.COMMON, Optional.empty());
    }

    public record Drain(int base, int perRank) {
        public static final Codec<Drain> CODEC = RecordCodecBuilder.create(
                p_345979_ -> p_345979_.group(
                                Codec.INT.fieldOf("base").forGetter(Drain::base),
                                Codec.INT.fieldOf("per_rank").forGetter(Drain::perRank)
                        )
                        .apply(p_345979_, Drain::new)
        );

        public int calculate(int rank) {
            return this.base + this.perRank * (rank - 1);
        }
    }

    public enum Compatibility implements StringRepresentable {
        FRAME("frame", 10),
        PRIMARY("primary", 9),
        SECONDARY("secondary", 9),
        MELEE("melee", 10),
        ARCHWING("archwing", 8),
        ARCHGUN("archgun", 8),
        ARCHMELEE("archmelee", 8),
        COMPANION("companion", 10),
        COMPANION_WEAPON("companion_weapon", 8),
        K_DRIVE("k_drive", 8),
        EXALTED_WEAPON("exalted_weapon", 8),
        NECRAMECH("necramech", 12);

        public static final Codec<Compatibility> CODEC = StringRepresentable.fromEnum(Compatibility::values);
        private final String name;
        private final int maxSlots;

        Compatibility(String name, int maxSlots) {
            this.name = name;
            this.maxSlots = maxSlots;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }

        public int getMaxSlots() {
            return this.maxSlots;
        }
    }

    public enum Variant implements StringRepresentable {
        FLAWED("flawed"),
        PRIMED("primed"),
        UMBRA("umbra"),
        AMALGAM("amalgam"),
        GALVANIZED("galvanized"),
        ARCHON("archon");

        public static final Codec<Variant> CODEC = StringRepresentable.fromEnum(Variant::values);
        private final String name;

        Variant(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}
