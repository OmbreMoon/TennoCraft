package com.ombremoon.tennocraft.common.api.handler;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ombremoon.tennocraft.common.api.IModHolder;
import com.ombremoon.tennocraft.common.api.IRangedModHolder;
import com.ombremoon.tennocraft.common.api.mod.Modification;
import com.ombremoon.tennocraft.common.api.mod.WeaponModContainer;
import com.ombremoon.tennocraft.common.api.weapon.Accuracy;
import com.ombremoon.tennocraft.common.api.weapon.ranged.projectile.ProjectileType;
import com.ombremoon.tennocraft.common.api.weapon.ranged.projectile.SolidProjectile;
import com.ombremoon.tennocraft.common.api.weapon.ranged.reload.ReloadType;
import com.ombremoon.tennocraft.common.api.weapon.ranged.trigger.*;
import com.ombremoon.tennocraft.common.api.weapon.schema.*;
import com.ombremoon.tennocraft.common.api.weapon.schema.data.DamageFalloff;
import com.ombremoon.tennocraft.common.api.weapon.schema.data.RangedAttack;
import com.ombremoon.tennocraft.common.init.TCData;
import com.ombremoon.tennocraft.common.networking.PayloadHandler;
import com.ombremoon.tennocraft.common.world.entity.BulletProjectile;
import com.ombremoon.tennocraft.main.Constants;
import com.ombremoon.tennocraft.util.Loggable;
import com.ombremoon.tennocraft.util.WeaponDamageResult;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.tslat.smartbrainlib.util.RandomUtil;

import javax.annotation.Nullable;
import java.util.*;

public class RangedWeaponHandler implements Loggable {
    public static final Codec<RangedWeaponHandler> CODEC = Codec.withAlternative(
            RecordCodecBuilder.create(
                    instance -> instance.group(
                            CompoundTag.CODEC.fieldOf("tag").forGetter(handler -> handler.tag),
                            Schema.DIRECT_CODEC.fieldOf("schema").forGetter(handler -> handler.schema)
                    ).apply(instance, RangedWeaponHandler::forCodec)
            ),
            RecordCodecBuilder.create(
                    instance -> instance.group(
                            CompoundTag.CODEC.fieldOf("tag").forGetter(handler -> handler.tag),
                            Schema.DIRECT_CODEC.fieldOf("schema").forGetter(handler -> handler.schema)
                    ).apply(instance, RangedWeaponHandler::forCodec)
            )
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, RangedWeaponHandler> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public RangedWeaponHandler decode(RegistryFriendlyByteBuf buffer) {
            CompoundTag tag = ByteBufCodecs.COMPOUND_TAG.decode(buffer);
            Schema schema = Schema.STREAM_CODEC.decode(buffer);
            return new RangedWeaponHandler(tag, schema, buffer.registryAccess());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buffer, RangedWeaponHandler handler) {
            ByteBufCodecs.COMPOUND_TAG.encode(buffer, handler.tag);
            Schema.STREAM_CODEC.encode(buffer, handler.schema);
        }
    };

    private final CompoundTag tag;
    private final RangedWeaponSchema schema;
    private final RangedAttackProperty attack;
    private final WeaponModContainer mods;
    private TriggerType<?> triggerType;
    @Nullable
    private HolderLookup.Provider registries;
    private int maxAmmo;
    private int magSize;
    private float fireRate;
    private float reloadSpeed;
    private boolean isReloading;
    public boolean isFiring;
    public int reloadTick;
    public boolean isChargingOrChanneling;
    public int chargeTick;
    public int chargedShotDelay;
    public int lastShotTick;
    public int shotCounter;

    private static RangedWeaponHandler forCodec(CompoundTag tag, Schema schema) {
        return new RangedWeaponHandler(tag, schema, null);
    }

    public RangedWeaponHandler(CompoundTag tag, Schema schema, @Nullable HolderLookup.Provider registries) {
        this.tag = tag;
        this.schema = (RangedWeaponSchema) schema;
        this.registries = registries;

        Modification.Compatibility compatibility = this.schema.getGeneral().layout().compatibility();
        RangedAttackProperties properties = this.schema.getProperties();
        this.triggerType = properties.triggers().stream().toList().getFirst();
        this.attack = properties.getAttack(this.triggerType);
        this.mods = new WeaponModContainer(compatibility, this.schema);
        this.loadStats();
        this.loadFromTag(tag, registries);
    }

    public void setRegistries(HolderLookup.Provider registries) {
        if (this.registries == null) {
            this.registries = registries;
            this.loadFromTag(this.tag, registries);
        }
    }

    public void ensureRegistryAccess(RegistryAccess registryAccess) {
        if (this.registries == null) {
            setRegistries(registryAccess);
        }
    }

    public void shoot(ServerLevel level, Player player, ItemStack stack, boolean updateReload, boolean useFallback) {
        if (stack.getItem() instanceof IRangedModHolder modHolder) {
            TriggerType<?> type = this.triggerType;
            type = type.is(ChargeTrigger.TYPE) && useFallback ? findTypeInList(this.schema, type.getSerializer().id()) : type;

            if (type.canFire(level, stack, (WeaponModContainer) modHolder.getMods(player, stack), this)) {
                RangedAttack attack = schema.getAttack(type);
                ProjectileType<?> projectileType = attack.projectileType();
                ReloadType<?> reloadType = attack.reloadType();

                int ammoCost = attack.ammoCost();
                float multishot = schema.getModdedMultishot(level, stack, player, type);
                float potentialMultishot = multishot % 1.0F;
                if (RandomUtil.percentChance(potentialMultishot))
                    multishot++;

                if (type instanceof ActiveTrigger active) {
                    multishot = Mth.clamp(multishot, 0, active.maxDeployables());
                } else if (type instanceof AutoSpoolTrigger autoSpool) {
                    multishot = autoSpool.spoolMultiShot(this.shotCounter, multishot);
                    ammoCost = autoSpool.spoolAmmoCost(this.shotCounter, ammoCost);
                } else if (type instanceof BurstTrigger burst && burst.delayTicks() == 0) {
                    int magCount = stack.getOrDefault(TCData.MAG_COUNT, 0);
                    multishot += magCount;
                    ammoCost = magCount;
                } else if (type.is(HeldTrigger.TYPE)) {
                    multishot = attack.multiShot();
                    ammoCost /= 2;
                }

                for (int i = 0; i < multishot; i++) {
                    if (projectileType instanceof SolidProjectile solid) {
                        BulletProjectile projectile = new BulletProjectile(level, player, attack, solid, stack, type);
                        if (type.is(ActiveTrigger.TYPE)) {
                            List<Integer> list = stack.getOrDefault(TCData.ACTIVE_DEPLOYABLES, new ArrayList<>());
                            list.add(projectile.getId());
                        }

                        float projectileSpeed = schema.getModdedProjectileSpeed(level, stack, player, type) / 20.0F;
                        projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, projectileSpeed, 1.0F);
                        level.addFreshEntity(projectile);
                        Constants.LOG.info("{}", player.tickCount);
                    }
                }

                if (reloadType.consumeWhen().orElse(ReloadType.ConsumeEvent.ON_FIRE) == ReloadType.ConsumeEvent.ON_FIRE)
                    modHolder.consumeAmmo(stack, ammoCost);

                this.shotCounter++;
                this.isFiring = true;
                PayloadHandler.updateBulletHandler((ServerPlayer) player, updateReload);
            }
        }
    }

    public void handleBulletDamage(ItemStack stack, LivingEntity attacker, LivingEntity target, WeaponDamageResult.Partial partial) {
        float damage = partial.getDamage();

        //ADD FIRE RATE DAMAGE

        //ADD HELD DAMAGE RAMP/DECAY

        RangedAttack attack = this.attack.getAttack();


        Optional<DamageFalloff> optional1 = attack.projectileType().getFalloff();
        if (optional1.isPresent()) {
            damage *= optional1.get().calculate(attacker.distanceTo(target));
        }

        partial.setDamage(damage);
    }

    public void stopShooting() {
        this.isFiring = false;
        this.shotCounter = 0;
        this.chargedShotDelay = 0;
    }

    public void confirmModChanges(Player player, ItemStack stack) {
        Mutable mutable = new Mutable(this);
        mutable.confirmModChanges(player, stack, this.mods);
        stack.set(TCData.RANGED_WEAPON_HANDLER, mutable.toImmutable());
    }

    public void cycleAlternateFire(ItemStack stack) {
        Mutable mutable = new Mutable(this);
        mutable.cycleAlternateFire(this.triggerType);
        stack.set(TCData.RANGED_WEAPON_HANDLER, mutable.toImmutable());
    }

    public CompoundTag getTag() {
        return this.tag;
    }

    public RangedWeaponSchema getSchema() {
        return this.schema;
    }

    public WeaponModContainer getMods() {
        return this.mods;
    }

    public TriggerType<?> getTriggerType() {
        return this.triggerType;
    }

    public int getMaxAmmo() {
        return this.maxAmmo;
    }

    public int getMagSize() {
        return this.magSize;
    }

    public float getFireRate() {
        return this.fireRate;
    }

    public float getReloadSpeed() {
        return this.reloadSpeed;
    }

    public boolean isReloading() {
        return this.isReloading;
    }

    public void setReloading(boolean reloading) {
        this.isReloading = reloading;
    }

    private void loadStats() {
        RangedUtilityProperties utility = this.schema.getUtility();
        this.maxAmmo = utility.maxAmmo();
        this.magSize = utility.magSize();

        RangedAttack rangedAttack = this.attack.getAttack();
        this.fireRate = rangedAttack.fireRate();

        ReloadType<?> reloadType = rangedAttack.reloadType();
        this.reloadSpeed = reloadType.reloadTime();
    }

    private void loadFromTag(CompoundTag tag, HolderLookup.Provider registries) {
        if (registries != null && tag.contains("Mods")) {
            this.mods.deserializeNBT(registries, tag.getCompound("Mods"));
        }

        if (tag.contains("Trigger Type")) {
            TriggerType.parse(tag).ifPresent(type -> this.triggerType = type);
        }
    }

    private static <T> T findNextTypeInList(Collection<T> list, T current) {
        Iterator<T> iterator = list.iterator();

        while (iterator.hasNext()) {
            if (iterator.next().equals(current)) {
                if (iterator.hasNext()) {
                    return iterator.next();
                }
                return list.iterator().next();
            }
        }

        return list.isEmpty() ? current : list.iterator().next();
    }

    public static TriggerType<?> findTypeInList(RangedWeaponSchema schema, ResourceLocation current) {
        return findTypeInList(schema.getProperties().triggers(), current);
    }

    private static TriggerType<?> findTypeInList(Collection<TriggerType<?>> list, ResourceLocation current) {
        for (TriggerType<?> type : list) {
            if (type.getSerializer().id().equals(current)) {
                return type;
            }
        }

        return null;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof RangedWeaponHandler handler && handler.tag == this.tag && handler.schema == this.schema;
    }

    @Override
    public int hashCode() {
        return this.tag.hashCode();
    }

    public static class Mutable {
        private final CompoundTag tag;
        private final RangedWeaponSchema schema;
        private final HolderLookup.Provider registries;

        public Mutable(RangedWeaponHandler handler) {
            this.tag = handler.tag;
            this.schema = handler.schema;
            this.registries = handler.registries;
        }

        public void confirmModChanges(Player player, ItemStack stack, WeaponModContainer mods) {
            mods.confirmMods(player, (IModHolder<?>) stack.getItem(), stack);

            //Modify Ammo Count
            //Modify Fire Rate
            //Modify Reload Speed
            //Modify Accuracy

            this.saveChanges(mods);
        }


        public <T extends TriggerType<?>> void cycleAlternateFire(T triggerType) {
            var type = findNextTypeInList(this.schema.getProperties().triggers(), triggerType);
            boolean flag = this.schema.getGeneral().triggerTypes().map(list -> list.contains(type.getSerializer().id())).orElse(false);
            if (type != triggerType && flag) {
                type.save(this.tag);
            }
        }

        private void saveChanges(WeaponModContainer mods) {
            if (this.registries != null)
                this.tag.put("Mods", mods.serializeNBT(this.registries));
        }

        public RangedWeaponHandler toImmutable() {
            return new RangedWeaponHandler(this.tag, this.schema, this.registries);
        }
    }
}
