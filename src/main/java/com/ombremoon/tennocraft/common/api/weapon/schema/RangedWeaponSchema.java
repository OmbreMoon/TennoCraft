package com.ombremoon.tennocraft.common.api.weapon.schema;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.weapon.DamageValue;
import com.ombremoon.tennocraft.common.api.weapon.ranged.projectile.SolidProjectile;
import com.ombremoon.tennocraft.common.api.weapon.ranged.trigger.TriggerType;
import com.ombremoon.tennocraft.common.api.weapon.schema.data.RangedAttack;
import com.ombremoon.tennocraft.common.init.TCSchemas;
import com.ombremoon.tennocraft.common.world.SlotGroup;
import com.ombremoon.tennocraft.util.ModHelper;
import com.ombremoon.tennocraft.util.WeaponDamageResult;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class RangedWeaponSchema extends WeaponSchema {
    private final Map<TriggerType<?>, Integer> baseDamageByTriggerType = new Object2IntOpenHashMap<>();
    private final Map<TriggerType<?>, WeaponDamageResult.Distribution> distributions = new Object2ObjectOpenHashMap<>();
    private final RangedUtilityProperties utility;
    private final RangedAttackProperties attacks;

    public RangedWeaponSchema(GeneralProperties general, RangedUtilityProperties utility, RangedAttackProperties attacks) {
        super(general);
        this.utility = utility;
        this.attacks = attacks;

        var optional = general.triggerTypes();
        if (optional.isEmpty() || optional.get().isEmpty()) {
            throw new IllegalStateException("Ranged weapon schema must have at least 1 defined trigger type");
        }

        var types = optional.get();
        if (types.size() != attacks.size()) {
            throw new IllegalStateException("Ranged weapon schema trigger types do not match attacks: [" + types + "] [" + attacks.triggers() + "]");
        }

        var list = new ArrayList<>(types);
        var set = attacks.triggers()
                .stream()
                .map(triggerType -> triggerType.getSerializer().id())
                .collect(Collectors.toSet());
        if (!list.containsAll(set)) {
            throw new IllegalStateException("Ranged weapon schema trigger types do not match attacks");
        }

        for (TriggerType<?> type : attacks.triggers()) {
            var properties = this.attacks.getAttack(type);
            var values = properties.damage;
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

    public RangedUtilityProperties getUtility() {
        return this.utility;
    }

    public RangedAttack getAttack(TriggerType<?> type) {
        return this.attacks.getAttack(type).getAttack();
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
    public int getBaseDamage(@Nullable TriggerType<?> triggerType) {
        return this.baseDamageByTriggerType.get(triggerType);
    }

    @Override
    public WeaponDamageResult.Distribution getBaseDamageDistribution(@Nullable TriggerType<?> triggerType) {
        return this.distributions.get(triggerType);
    }

    @Override
    public float getModdedBaseDamage(ServerLevel level, ItemStack stack, LivingEntity attacker, LivingEntity target, @Nullable TriggerType<?> triggerType) {
        return 0;
    }

    @Override
    public float getModdedCritChance(ServerLevel level, ItemStack stack, LivingEntity attacker, LivingEntity target, @Nullable TriggerType<?> triggerType) {
        float critChance = this.attacks.getAttack(triggerType).critChance;
        return critChance * (1.0F + Math.max(0.0F, ModHelper.modifyCritChance(level, stack, this, attacker, target, 0.0F)));
    }

    @Override
    public float getModdedCritDamage(ServerLevel level, ItemStack stack, LivingEntity attacker, LivingEntity target, @Nullable TriggerType<?> triggerType) {
        float critMultiplier = this.attacks.getAttack(triggerType).critMultiplier;
        return critMultiplier * (1.0F + Math.max(0.0F, ModHelper.modifyCritChance(level, stack, this, attacker, target, 0.0F)));
    }

    @Override
    public float getModdedStatusChance(ServerLevel level, ItemStack stack, LivingEntity attacker, LivingEntity target, @Nullable TriggerType<?> triggerType) {
        float status = this.attacks.getAttack(triggerType).status;
        return status * (1.0F + Math.max(0.0F, ModHelper.modifyCritChance(level, stack, this, attacker, target, 0.0F)));
    }

    @Override
    public float getModdedRivenDisposition(ServerLevel level, ItemStack stack, LivingEntity attacker, LivingEntity target, @Nullable TriggerType<?> triggerType) {
        return 0;
    }

    public int getModdedReloadTime(ServerLevel level, ItemStack stack, LivingEntity attacker, TriggerType<?> triggerType) {
        int reloadTime = this.getAttack(triggerType).reloadType().reloadTime();
        return Math.max(0, Math.round(reloadTime * (1.0F + Math.max(0.0F, ModHelper.modifyReloadTime(level, stack, this, attacker)))));
    }

    public float getModdedMultishot(ServerLevel level, ItemStack stack, LivingEntity attacker, TriggerType<?> triggerType) {
        float multishot = this.getAttack(triggerType).multiShot();
        return multishot * (1.0F + Math.max(0.0F, ModHelper.modifyMultishot(level, stack, this, attacker)));
    }

    public float getModdedProjectileSpeed(ServerLevel level, ItemStack stack, LivingEntity attacker, TriggerType<?> triggerType) {
        var projectileType = this.getAttack(triggerType).projectileType();
        if (projectileType instanceof SolidProjectile solid) {
            return solid.projectileSpeed() * (1.0F + Math.max(0.0F, ModHelper.modifyProjectileSpeed(level, stack, this, attacker)));
        }

        return 0.0F;
    }

    public static class Serializer implements SchemaSerializer<RangedWeaponSchema> {
        public static final MapCodec<RangedWeaponSchema> MAP_CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        GeneralProperties.CODEC.fieldOf("general").forGetter(schema -> schema.general),
                        RangedUtilityProperties.CODEC.fieldOf("utility").forGetter(schema -> schema.utility),
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
            GeneralProperties generalProperties = GeneralProperties.STREAM_CODEC.decode(buffer);
            RangedUtilityProperties utilitySchema = RangedUtilityProperties.STREAM_CODEC.decode(buffer);
            RangedAttackProperties properties = RangedAttackProperties.STREAM_CODEC.decode(buffer);
            return new RangedWeaponSchema(generalProperties, utilitySchema, properties);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, RangedWeaponSchema schema) {
            GeneralProperties.STREAM_CODEC.encode(buffer, schema.general);
            RangedUtilityProperties.STREAM_CODEC.encode(buffer, schema.utility);
            RangedAttackProperties.STREAM_CODEC.encode(buffer, schema.attacks);
        }
    }
}
