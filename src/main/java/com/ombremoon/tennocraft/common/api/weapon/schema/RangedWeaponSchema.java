package com.ombremoon.tennocraft.common.api.weapon.schema;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.weapon.DamageValue;
import com.ombremoon.tennocraft.common.api.weapon.TriggerType;
import com.ombremoon.tennocraft.common.init.TCSchemas;
import com.ombremoon.tennocraft.common.world.SlotGroup;
import com.ombremoon.tennocraft.common.world.WorldStatus;
import com.ombremoon.tennocraft.util.ModHelper;
import com.ombremoon.tennocraft.util.WeaponDamageResult;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Map;

public class RangedWeaponSchema extends WeaponSchema {
    private final Map<TriggerType, Integer> baseDamageByTriggerType = new Object2IntOpenHashMap<>();
    private final Map<TriggerType, WeaponDamageResult.Distribution> distributions = new Object2ObjectOpenHashMap<>();
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

        for (TriggerType type : types) {
            var properties = this.attacks.getAttacks(type);
            var values = properties.getFirst().damage;
            float f = 0.0F;
            for (DamageValue damage : values) {
                f += damage.amount();
            }

            int baseDamage = (int) f;
            this.baseDamageByTriggerType.put(type, baseDamage);
            this.distributions.put(type, WeaponDamageResult.createDistribution(values));
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
    public SlotGroup getType() {
        return SlotGroup.WEAPON;
    }

    @Override
    public int getBaseDamage(@Nullable TriggerType triggerType) {
        return this.baseDamageByTriggerType.get(triggerType);
    }

    @Override
    public WeaponDamageResult.Distribution getBaseDamageDistribution(@Nullable TriggerType triggerType) {
        return this.distributions.get(triggerType);
    }

    @Override
    public float getModdedTypeDamage(ServerLevel level, ItemStack stack, WorldStatus status, LivingEntity attacker, LivingEntity target, @Nullable TriggerType triggerType) {
        return 1.0F + Math.max(0.0F, ModHelper.modifyTypeDamage(level, status, stack, this, attacker, target, 0.0F));
    }

    @Override
    public float getModdedCritChance(ServerLevel level, ItemStack stack, LivingEntity attacker, LivingEntity target, @Nullable TriggerType triggerType) {
        return 1.0F + Math.max(0.0F, ModHelper.modifyCritChance(level, stack, this, attacker, target, 0.0F));
    }

    @Override
    public float getModdedCritDamage(ServerLevel level, ItemStack stack, LivingEntity attacker, LivingEntity target, @Nullable TriggerType triggerType) {
        return 0;
    }

    @Override
    public float getModdedStatusChance(ServerLevel level, ItemStack stack, LivingEntity attacker, LivingEntity target, @Nullable TriggerType triggerType) {
        return 0;
    }

    @Override
    public float getModdedRivenDisposition(ServerLevel level, ItemStack stack, LivingEntity attacker, LivingEntity target, @Nullable TriggerType triggerType) {
        return 0;
    }

/*    public float getModdedAccuracy(ServerLevel level, ItemStack stack, LivingEntity target, int modRank, @Nullable TriggerType triggerType) {

    }

    public float getModdedFireRate(ServerLevel level, ItemStack stack, LivingEntity target, int modRank, @Nullable TriggerType triggerType) {

    }

    public float getModdedReloadSpeed(ServerLevel level, ItemStack stack, LivingEntity target, int modRank, @Nullable TriggerType triggerType) {

    }

    public float getModdedMagSize(ServerLevel level, ItemStack stack, LivingEntity target, int modRank, @Nullable TriggerType triggerType) {

    }*/

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
