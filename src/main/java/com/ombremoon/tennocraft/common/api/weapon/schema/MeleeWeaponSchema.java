package com.ombremoon.tennocraft.common.api.weapon.schema;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.weapon.DamageValue;
import com.ombremoon.tennocraft.common.api.weapon.ranged.trigger.TriggerType;
import com.ombremoon.tennocraft.common.init.TCSchemas;
import com.ombremoon.tennocraft.common.world.SlotGroup;
import com.ombremoon.tennocraft.util.ModHelper;
import com.ombremoon.tennocraft.util.WeaponDamageResult;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class MeleeWeaponSchema extends WeaponSchema {
    private final MeleeUtilityProperties utility;
    private final MeleeAttackProperties attacks;
    private final int baseDamage;
    private final WeaponDamageResult.Distribution distribution;

    public MeleeWeaponSchema(GeneralProperties generalProperties, MeleeUtilityProperties utility, MeleeAttackProperties attacks) {
        super(generalProperties);
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

    public MeleeUtilityProperties getUtility() {
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
    public SlotGroup getType() {
        return SlotGroup.WEAPON;
    }

    @Override
    public int getBaseDamage(TriggerType<?> triggerType) {
        return this.baseDamage;
    }

    @Override
    public WeaponDamageResult.Distribution getBaseDamageDistribution(@Nullable TriggerType<?> triggerType) {
        return this.distribution;
    }

    @Override
    public float getModdedBaseDamage(ServerLevel level, ItemStack stack, LivingEntity attacker, LivingEntity target, @Nullable TriggerType<?> triggerType) {
        return 0;
    }

    @Override
    public float getModdedCritChance(ServerLevel level, ItemStack stack, LivingEntity attacker, LivingEntity target, @Nullable TriggerType<?> triggerType) {
        float critChance = this.attacks.attack().getCritChance();
        return critChance * (1.0F + Math.max(0.0F, ModHelper.modifyCritChance(level, stack, this, attacker, target, 0.0F)));
    }

    @Override
    public float getModdedCritDamage(ServerLevel level, ItemStack stack, LivingEntity attacker, LivingEntity target, @Nullable TriggerType<?> triggerType) {
        float critMultiplier = this.attacks.attack().getCritMultiplier();
        return critMultiplier * (1.0F + Math.max(0.0F, ModHelper.modifyCritDamage(level, stack, this, attacker, target, 0.0F)));
    }

    @Override
    public float getModdedStatusChance(ServerLevel level, ItemStack stack, LivingEntity attacker, LivingEntity target, @Nullable TriggerType<?> triggerType) {
        float statusChance = this.attacks.attack().getStatus();
        return statusChance * (1.0F + Math.max(0.0F, ModHelper.modifyStatusChance(level, stack, this, attacker, target, 0.0F)));
    }

    @Override
    public float getModdedRivenDisposition(ServerLevel level, ItemStack stack, LivingEntity attacker, LivingEntity target, @Nullable TriggerType<?> triggerType) {
        return 0;
    }

    public static class Serializer implements SchemaSerializer<MeleeWeaponSchema> {
        public static final MapCodec<MeleeWeaponSchema> MAP_CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        GeneralProperties.CODEC.fieldOf("general").forGetter(schema -> schema.general),
                        MeleeUtilityProperties.CODEC.fieldOf("utility").forGetter(schema -> schema.utility),
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
            GeneralProperties generalProperties = GeneralProperties.STREAM_CODEC.decode(buffer);
            MeleeUtilityProperties utilitySchema = MeleeUtilityProperties.STREAM_CODEC.decode(buffer);
            MeleeAttackProperties properties = MeleeAttackProperties.STREAM_CODEC.decode(buffer);
            return new MeleeWeaponSchema(generalProperties, utilitySchema, properties);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, MeleeWeaponSchema schema) {
            GeneralProperties.STREAM_CODEC.encode(buffer, schema.general);
            MeleeUtilityProperties.STREAM_CODEC.encode(buffer, schema.utility);
            MeleeAttackProperties.STREAM_CODEC.encode(buffer, schema.attacks);
        }
    }
}
