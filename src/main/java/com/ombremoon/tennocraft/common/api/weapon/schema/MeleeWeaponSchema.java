package com.ombremoon.tennocraft.common.api.weapon.schema;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.weapon.DamageValue;
import com.ombremoon.tennocraft.common.api.weapon.TriggerType;
import com.ombremoon.tennocraft.common.init.TCSchemas;
import com.ombremoon.tennocraft.util.ModHelper;
import com.ombremoon.tennocraft.util.WeaponDamageResult;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MeleeWeaponSchema extends WeaponSchema {
    private final MeleeUtilitySchema utility;
    private final MeleeAttackProperties attacks;
    private final int baseDamage;
    private final WeaponDamageResult.Distribution distribution;

    public MeleeWeaponSchema(GeneralSchema generalSchema, MeleeUtilitySchema utility, MeleeAttackProperties attacks) {
        super(generalSchema);
        this.utility = utility;
        this.attacks = attacks;

        var values = attacks.attack().damage;
        float f = 0;
        for (DamageValue damage : values) {
            f += damage.amount();
        }

        this.baseDamage = (int) f;
        this.distribution = WeaponDamageResult.createDistribution(values);
    }

    public MeleeUtilitySchema getUtility() {
        return this.utility;
    }

    public MeleeAttackProperties getAttacks() {
        return this.attacks;
    }

    @Override
    public SchemaSerializer<?> getSerializer() {
        return TCSchemas.MELEE_WEAPON_SCHEMA.get();
    }

    @Override
    public int getBaseDamage(TriggerType triggerType) {
        return this.baseDamage;
    }

    @Override
    public WeaponDamageResult.Distribution getBaseDamageDistribution(@Nullable TriggerType triggerType) {
        return this.distribution;
    }

    @Override
    public float getModdedCritChance(ServerLevel level, ItemStack stack, LivingEntity target, @Nullable TriggerType triggerType) {
        float critChance = this.attacks.attack().getCritChance();
        return critChance * (1.0F + Math.max(0.0F, ModHelper.modifyCritChance(level, stack, target, 0.0F)));
    }

    @Override
    public float getModdedCritDamage(ServerLevel level, ItemStack stack, LivingEntity target, @Nullable TriggerType triggerType) {
        return 0;
    }

    @Override
    public float getModdedStatusChance(ServerLevel level, ItemStack stack, LivingEntity target, @Nullable TriggerType triggerType) {
        return 0;
    }

    @Override
    public float getModdedRivenDisposition(ServerLevel level, ItemStack stack, LivingEntity target, @Nullable TriggerType triggerType) {
        return 0;
    }

    public static class Serializer implements SchemaSerializer<MeleeWeaponSchema> {
        public static final MapCodec<MeleeWeaponSchema> MAP_CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        GeneralSchema.CODEC.fieldOf("general").forGetter(schema -> schema.general),
                        MeleeUtilitySchema.CODEC.fieldOf("utility").forGetter(schema -> schema.utility),
                        MeleeAttackProperties.CODEC.fieldOf("attacks").forGetter(schema -> schema.attacks)
                ).apply(instance, MeleeWeaponSchema::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, MeleeWeaponSchema> STREAM_CODEC = StreamCodec.of(
                MeleeWeaponSchema.Serializer::toNetwork, MeleeWeaponSchema.Serializer::fromNetwork
        );

        @Override
        public MapCodec<MeleeWeaponSchema> codec() {
            return MAP_CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, MeleeWeaponSchema> streamCodec() {
            return STREAM_CODEC;
        }

        private static MeleeWeaponSchema fromNetwork(RegistryFriendlyByteBuf buffer) {
            GeneralSchema generalSchema = GeneralSchema.STREAM_CODEC.decode(buffer);
            MeleeUtilitySchema utilitySchema = MeleeUtilitySchema.STREAM_CODEC.decode(buffer);
            MeleeAttackProperties properties = MeleeAttackProperties.STREAM_CODEC.decode(buffer);
            return new MeleeWeaponSchema(generalSchema, utilitySchema, properties);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, MeleeWeaponSchema schema) {
            GeneralSchema.STREAM_CODEC.encode(buffer, schema.general);
            MeleeUtilitySchema.STREAM_CODEC.encode(buffer, schema.utility);
            MeleeAttackProperties.STREAM_CODEC.encode(buffer, schema.attacks);
        }
    }
}
