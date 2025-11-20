package com.ombremoon.tennocraft.common.api.mod;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.mod.effects.ModValueEffect;
import com.ombremoon.tennocraft.common.api.weapon.schema.Schema;
import com.ombremoon.tennocraft.common.api.mod.effects.ModAttributeEffect;
import com.ombremoon.tennocraft.common.world.WorldStatus;
import com.ombremoon.tennocraft.common.world.level.loot.ModContextParams;
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
import net.minecraft.resources.RegistryFileCodec;
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
import org.apache.commons.lang3.mutable.MutableFloat;

import java.util.*;
import java.util.function.Consumer;
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
            HolderSet<Schema> incompatibleSchemas,
            ModType type,
            ModPolarity polarity,
            Drain drain,
            int maxRank,
            ModRarity rarity,
            Variant variant
    ) {
        return new ModDefinition(supportedSchemas, Optional.of(incompatibleSchemas), type, polarity, drain, maxRank, rarity, Optional.of(variant));
    }

    public static ModDefinition definition(
            HolderSet<Schema> supportedSchemas,
            HolderSet<Schema> incompatibleSchemas,
            ModType type,
            ModPolarity polarity,
            Drain drain,
            int maxRank,
            ModRarity rarity
    ) {
        return new ModDefinition(supportedSchemas, Optional.of(incompatibleSchemas), type, polarity, drain, maxRank, rarity, Optional.empty());
    }

    public static ModDefinition definition(
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
    }

    public HolderSet<Schema> getSupportedSchemas() {
        return this.definition.supportedSchemas;
    }

    public boolean isSupportedSchema(Holder<Schema> schema) {
        return this.definition.supportedSchemas.contains(schema);
    }

    public boolean isIncompatibleSchema(Holder<Schema> schema) {
        return this.definition.incompatibleSchemas.map(holders -> holders.contains(schema)).orElse(false);
    }

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

    public void modifyWeaponDamage(ServerLevel level, int modRank, Schema schema, Entity entity, MutableFloat damage) {
        this.modifyEntityFilteredValue(TCModEffectComponents.DAMAGE.get(), level, modRank, schema, entity, damage);
    }

    public void modifyTypeDamage(ServerLevel level, WorldStatus status, int modRank, Schema schema, Entity entity, MutableFloat damage) {
        this.modifyEntityFilteredValue(status.getDataComponentType().get(), level, modRank, schema, entity, damage);
    }

    public void modifyCritChance(ServerLevel level, int modRank, Schema schema, Entity entity, MutableFloat damage) {
        this.modifyEntityFilteredValue(TCModEffectComponents.CRIT_CHANCE.get(), level, modRank, schema, entity, damage);
    }

    public void modifyCritDamage(ServerLevel level, int modRank, Schema schema, Entity entity, MutableFloat damage) {
        this.modifyEntityFilteredValue(TCModEffectComponents.CRIT_MULTIPLIER.get(), level, modRank, schema, entity, damage);
        //modifyWithItemModifiers
    }

    //public void modifyElementalDamage (Data component type from status)

    public void modifyStatusChance(ServerLevel level, int modRank, Schema schema, Entity entity, MutableFloat damage) {
        this.modifyEntityFilteredValue(TCModEffectComponents.STATUS_CHANCE.get(), level, modRank, schema, entity, damage);
    }

    public void modifyEntityFilteredValue(
            DataComponentType<List<ConditionalModEffect<ModValueEffect>>> type,
            ServerLevel level,
            int modRank,
            Schema schema,
            Entity entity,
            MutableFloat value
    ) {
        applyEffects(
                this.getEffects(type),
                entityContext(level, schema, modRank, entity, entity.position()),
                valueEffect -> value.setValue(valueEffect.process(modRank, entity.getRandom(), value.floatValue()))
        );
    }

    public void modifyDamageFilteredValue(
            DataComponentType<List<ConditionalModEffect<ModValueEffect>>> type,
            ServerLevel level,
            int modRank,
            Schema schema,
            Entity entity,
            DamageSource source,
            MutableFloat value
    ) {
        applyEffects(
                this.getEffects(type),
                damageContext(level, schema, modRank, entity, source),
                valueEffect -> value.setValue(valueEffect.process(modRank, entity.getRandom(), value.floatValue()))
        );
    }

    public static LootContext damageContext(ServerLevel level, Schema schema, int modRank, Entity entity, DamageSource damageSource) {
        LootParams lootparams = new LootParams.Builder(level)
                .withParameter(LootContextParams.THIS_ENTITY, entity)
                .withParameter(ModContextParams.MOD_RANK, modRank)
                .withParameter(LootContextParams.ORIGIN, entity.position())
                .withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
                .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, damageSource.getEntity())
                .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, damageSource.getDirectEntity())
                .withOptionalParameter(ModContextParams.SCHEMA, schema)
                .create(ModContextParams.MODDED_DAMAGE);
        return new LootContext.Builder(lootparams).create(Optional.empty());
    }

    public static LootContext locationContext(ServerLevel level, int modRank, Entity entity, boolean enchantmentActive) {
        LootParams lootparams = new LootParams.Builder(level)
                .withParameter(LootContextParams.THIS_ENTITY, entity)
                .withParameter(ModContextParams.MOD_RANK, modRank)
                .withParameter(LootContextParams.ORIGIN, entity.position())
                .withParameter(LootContextParams.ENCHANTMENT_ACTIVE, enchantmentActive)
                .create(LootContextParamSets.ENCHANTED_LOCATION);
        return new LootContext.Builder(lootparams).create(Optional.empty());
    }

    public static LootContext entityContext(ServerLevel level, Schema schema, int modRank, Entity entity, Vec3 origin) {
        LootParams lootparams = new LootParams.Builder(level)
                .withParameter(LootContextParams.THIS_ENTITY, entity)
                .withParameter(ModContextParams.MOD_RANK, modRank)
                .withParameter(LootContextParams.ORIGIN, origin)
                .withOptionalParameter(ModContextParams.SCHEMA, schema)
                .create(ModContextParams.MODDED_ENTITY);
        return new LootContext.Builder(lootparams).create(Optional.empty());
    }

    public static LootContext blockHitContext(ServerLevel level, int modRank, Entity entity, Vec3 origin, BlockState state) {
        LootParams lootparams = new LootParams.Builder(level)
                .withParameter(LootContextParams.THIS_ENTITY, entity)
                .withParameter(ModContextParams.MOD_RANK, modRank)
                .withParameter(LootContextParams.ORIGIN, origin)
                .withParameter(LootContextParams.BLOCK_STATE, state)
                .create(LootContextParamSets.HIT_BLOCK);
        return new LootContext.Builder(lootparams).create(Optional.empty());
    }

    public static <T> void applyEffects(List<ConditionalModEffect<T>> effects, LootContext context, Consumer<T> applier) {
        for (ConditionalModEffect<T> effect : effects) {
            if (effect.matches(context)) {
                applier.accept(effect.effect());
            }
        }
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

    public record ModDefinition(HolderSet<Schema> supportedSchemas, Optional<HolderSet<Schema>> incompatibleSchemas, ModType type, ModPolarity polarity, Drain drain, int maxRank, ModRarity rarity, Optional<Variant> variant) {
        public static final MapCodec<ModDefinition> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        RegistryCodecs.homogeneousList(Keys.SCHEMA).fieldOf("supports").forGetter(ModDefinition::supportedSchemas),
                        RegistryCodecs.homogeneousList(Keys.SCHEMA, Schema.DIRECT_CODEC, false).optionalFieldOf("incompatible").forGetter(ModDefinition::incompatibleSchemas),
                        ModType.CODEC.fieldOf("type").forGetter(ModDefinition::type),
                        ModPolarity.CODEC.fieldOf("polarity").forGetter(ModDefinition::polarity),
                        Drain.CODEC.fieldOf("drain").forGetter(ModDefinition::drain),
                        Codec.INT.fieldOf("maxRank").forGetter(ModDefinition::maxRank),
                        ModRarity.CODEC.fieldOf("rarity").forGetter(ModDefinition::rarity),
                        Variant.CODEC.optionalFieldOf("variant").forGetter(ModDefinition::variant)
                ).apply(instance, ModDefinition::new)
        );

        private static final ModDefinition EMPTY = new ModDefinition(HolderSet.empty(), Optional.empty(), ModType.STANDARD, ModPolarity.ANY, constantDrain(0), 0, ModRarity.COMMON, Optional.empty());
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
        FRAME("frame", 10, false),
        PRIMARY("primary", 9, true),
        SECONDARY("secondary", 9, true),
        MELEE("melee", 10, true),
        ARCHWING("archwing", 8, true),
        ARCHGUN("archgun", 8, true),
        ARCHMELEE("archmelee", 8, false),
        COMPANION("companion", 10, false),
        COMPANION_WEAPON("companion_weapon", 8, true),
        K_DRIVE("k_drive", 8, false),
        EXALTED_WEAPON("exalted_weapon", 8, true),
        NECRAMECH("necramech", 12, false);

        public static final Codec<Compatibility> CODEC = StringRepresentable.fromEnum(Compatibility::values);
        private final String name;
        private final int maxSlots;
        private final boolean isWeapon;

        Compatibility(String name, int maxSlots, boolean isWeapon) {
            this.name = name;
            this.maxSlots = maxSlots;
            this.isWeapon = isWeapon;
        }

        public boolean isWeapon() {
            return this.isWeapon;
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
