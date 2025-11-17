package com.ombremoon.tennocraft.common.api.weapon.schema;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.weapon.DamageValue;
import com.ombremoon.tennocraft.common.api.weapon.TriggerType;
import com.ombremoon.tennocraft.common.init.TCSchemas;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class RangedWeaponSchema extends WeaponSchema {
    private final RangedUtilitySchema utility;
    private final RangedAttackProperties attacks;

    public RangedWeaponSchema(GeneralSchema general, RangedUtilitySchema utility, RangedAttackProperties attacks) {
        super(general);
        this.utility = utility;
        this.attacks = attacks;

        for (var entry : attacks.attacks().entrySet()) {
            if (entry.getValue().size() > 2) {
                throw new IllegalStateException("Cannot have more than 2 defined attacks per trigger type");
            }
        }

        if (general.triggerTypes().isEmpty()) {
            throw new IllegalStateException("Ranged weapon schema must have at least 1 defined trigger type");
        }

        var types = general.triggerTypes().get();
        if (types.size() != attacks.size()) {
            throw new IllegalStateException("Ranged weapon schema trigger types do not match attacks");
        }

        var list = new ArrayList<>(types);
        if (!list.containsAll(attacks.triggers())) {
            throw new IllegalStateException("Ranged weapon schema trigger types do not match attacks");
        }
    }

    public RangedAttackProperties getProperties() {
        return this.attacks;
    }

    public RangedUtilitySchema getUtility() {
        return this.utility;
    }

    @Override
    public SchemaSerializer<?> getSerializer() {
        return TCSchemas.RANGED_WEAPON_SCHEMA.get();
    }

    @Override
    public int getBaseDamage(@Nullable TriggerType triggerType) {
        var attacks = this.attacks.getAttacks(triggerType);
        var values = attacks.getFirst().damage;
        float f = 0.0F;
        for (DamageValue damage : values) {
            f += damage.amount();
        }

        return (int) f;
    }

    public static class Serializer implements SchemaSerializer<RangedWeaponSchema> {
        public static final MapCodec<RangedWeaponSchema> MAP_CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        GeneralSchema.CODEC.fieldOf("general").forGetter(schema -> schema.general),
                        RangedUtilitySchema.CODEC.fieldOf("utility").forGetter(schema -> schema.utility),
                        RangedAttackProperties.CODEC.fieldOf("attacks").forGetter(schema -> schema.attacks)
                ).apply(instance, RangedWeaponSchema::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, RangedWeaponSchema> STREAM_CODEC = StreamCodec.of(
                Serializer::toNetwork, Serializer::fromNetwork
        );

        @Override
        public MapCodec<RangedWeaponSchema> codec() {
            return MAP_CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, RangedWeaponSchema> streamCodec() {
            return STREAM_CODEC;
        }

        private static RangedWeaponSchema fromNetwork(RegistryFriendlyByteBuf buffer) {
            GeneralSchema generalSchema = GeneralSchema.STREAM_CODEC.decode(buffer);
            RangedUtilitySchema utilitySchema = RangedUtilitySchema.STREAM_CODEC.decode(buffer);
            RangedAttackProperties properties = RangedAttackProperties.STREAM_CODEC.decode(buffer);
            return new RangedWeaponSchema(generalSchema, utilitySchema, properties);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, RangedWeaponSchema schema) {
            GeneralSchema.STREAM_CODEC.encode(buffer, schema.general);
            RangedUtilitySchema.STREAM_CODEC.encode(buffer, schema.utility);
            RangedAttackProperties.STREAM_CODEC.encode(buffer, schema.attacks);
        }
    }
}
