package com.ombremoon.tennocraft.common.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.mod.ModLayout;
import com.ombremoon.tennocraft.common.api.weapon.schema.*;
import com.ombremoon.tennocraft.common.init.TCAbilities;
import com.ombremoon.tennocraft.common.init.TCSchemas;
import com.ombremoon.tennocraft.util.IntPair;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Optional;

public record FrameSchema(
        ObjectOpenHashSet<AbilityType<?>> abilities,
        Optional<AbilityType<?>> passive,
        Component description,
        IntPair health,
        IntPair shield,
        IntPair energy,
        int armor,
        int startEnergy,
        float sprintSpeed,
        ModLayout layout
        ) implements Schema {

    @Override
    public SchemaSerializer<?> getSerializer() {
        return TCSchemas.FRAME_SCHEMA.get();
    }

    public static class Serializer implements SchemaSerializer<FrameSchema> {
        public static final MapCodec<FrameSchema> MAP_CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        TCAbilities.REGISTRY
                                .byNameCodec()
                                .listOf()
                                .fieldOf("abilities")
                                .flatXmap(list -> {
                                    AbilityType<?>[] abilityList = list.toArray(AbilityType<?>[]::new);
                                    if (abilityList.length == 0) {
                                        return DataResult.error(() -> "Invalid Mineframe. Must have at least 1 ability defined.");
                                    } else if (abilityList.length > 4) {
                                        return DataResult.error(() -> "Invalid Mineframe. Too many abilities defined, you greedy gremlin. The max is 4.");
                                    } else {
                                        return DataResult.success(ObjectOpenHashSet.of(abilityList));
                                    }
                                }, slotSet -> slotSet.size() > 10 ? DataResult.error(() -> "Invalid Mineframe. Too many abilities defined, you greedy gremlin. The max is 4.") : DataResult.success(slotSet.stream().toList()))
                                .forGetter(FrameSchema::abilities),
                        TCAbilities.REGISTRY.byNameCodec().optionalFieldOf("passive").forGetter(FrameSchema::passive),
                        ComponentSerialization.CODEC.fieldOf("description").forGetter(FrameSchema::description),
                        IntPair.CODEC.fieldOf("health").forGetter(FrameSchema::health),
                        IntPair.CODEC.fieldOf("shield").forGetter(FrameSchema::shield),
                        IntPair.CODEC.fieldOf("energy").forGetter(FrameSchema::energy),
                        Codec.INT.fieldOf("armor").forGetter(FrameSchema::armor),
                        Codec.INT.fieldOf("startEnergy").forGetter(FrameSchema::startEnergy),
                        Codec.FLOAT.fieldOf("sprintSpeed").forGetter(FrameSchema::sprintSpeed),
                        ModLayout.CODEC.fieldOf("layout").forGetter(FrameSchema::layout)
                ).apply(instance, FrameSchema::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, FrameSchema> STREAM_CODEC = StreamCodec.of(
                Serializer::toNetwork, Serializer::fromNetwork
        );

        @Override
        public MapCodec<FrameSchema> codec() {
            return MAP_CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, FrameSchema> streamCodec() {
            return STREAM_CODEC;
        }

        private static FrameSchema fromNetwork(RegistryFriendlyByteBuf buffer) {
            ObjectOpenHashSet<AbilityType<?>> abilities = ByteBufCodecs.registry(TCAbilities.RESOURCE_KEY).apply(ByteBufCodecs.collection(ObjectOpenHashSet::new)).decode(buffer);
            Optional<AbilityType<?>> passive = ByteBufCodecs.optional(ByteBufCodecs.registry(TCAbilities.RESOURCE_KEY)).decode(buffer);
            Component description = ComponentSerialization.STREAM_CODEC.decode(buffer);
            IntPair health = IntPair.STREAM_CODEC.decode(buffer);
            IntPair shield = IntPair.STREAM_CODEC.decode(buffer);
            IntPair energy = IntPair.STREAM_CODEC.decode(buffer);
            int armor = buffer.readVarInt();
            int startEnergy = buffer.readVarInt();
            float sprintSpeed = buffer.readFloat();
            ModLayout modLayout = ModLayout.STREAM_CODEC.decode(buffer);
            return new FrameSchema(abilities, passive, description, health, shield, energy, armor, startEnergy, sprintSpeed, modLayout);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, FrameSchema schema) {
            ByteBufCodecs.registry(TCAbilities.RESOURCE_KEY).apply(ByteBufCodecs.collection(ObjectOpenHashSet::new)).encode(buffer, schema.abilities);
            ByteBufCodecs.optional(ByteBufCodecs.registry(TCAbilities.RESOURCE_KEY)).encode(buffer, schema.passive);
            ComponentSerialization.STREAM_CODEC.encode(buffer, schema.description);
            IntPair.STREAM_CODEC.encode(buffer, schema.health);
            IntPair.STREAM_CODEC.encode(buffer, schema.shield);
            IntPair.STREAM_CODEC.encode(buffer, schema.energy);
            buffer.writeVarInt(schema.armor);
            buffer.writeVarInt(schema.startEnergy);
            buffer.writeFloat(schema.sprintSpeed);
            ModLayout.STREAM_CODEC.encode(buffer, schema.layout);
        }
    }
}
