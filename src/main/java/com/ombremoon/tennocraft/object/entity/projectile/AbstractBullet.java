package com.ombremoon.tennocraft.object.entity.projectile;

import com.google.common.collect.Maps;
import com.ombremoon.tennocraft.common.AttributeHandler;
import com.ombremoon.tennocraft.object.entity.generic.TCEnemy;
import com.ombremoon.tennocraft.object.item.weapon.AbstractProjectileWeapon;
import com.ombremoon.tennocraft.object.world.DamageType;
import com.ombremoon.tennocraft.player.FrameAttribute;
import com.ombremoon.tennocraft.util.DamageUtil;
import com.ombremoon.tennocraft.util.WeaponUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import net.tslat.smartbrainlib.util.RandomUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class AbstractBullet extends Entity implements IEntityAdditionalSpawnData {
    protected int attackEntityId;
    protected LivingEntity attackEntity;
    private ItemStack weaponStack;
    private AbstractProjectileWeapon projectileWeapon;
    protected int life;

    public AbstractBullet(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public AbstractBullet(EntityType<?> pEntityType, Level pLevel, LivingEntity attackEntity, ItemStack weaponStack, AbstractProjectileWeapon projectileWeapon) {
        super(pEntityType, pLevel);
        this.attackEntityId = attackEntity.getId();
        this.attackEntity = attackEntity;
        this.life = projectileWeapon.getProjectileLife();

        Vec3 vec3 = this.getRotVector(weaponStack, attackEntity.getXRot(), attackEntity.getYRot());
        double projectileSpeed = projectileWeapon.getProjectileSpeed();
        this.setDeltaMovement(vec3.x * projectileSpeed, vec3.y * projectileSpeed, vec3.z * projectileSpeed);
        this.interpolateMotion();
    }


    public int getAttackEntityId() {
        return this.attackEntityId;
    }

    public LivingEntity getAttackEntity() {
        return this.attackEntity;
    }

    public void setWeaponStack(ItemStack weaponStack) {
        this.weaponStack = weaponStack;
    }

    public ItemStack getWeaponStack() {
        return this.weaponStack;
    }

    public void setProjectileWeapon(AbstractProjectileWeapon projectileWeapon) {
        this.projectileWeapon = projectileWeapon;
    }

    public AbstractProjectileWeapon getProjectileWeapon() {
        return this.projectileWeapon;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount % 10 == 0) {
            this.onStartTick();
        }
        this.interpolateMotion();
        this.onTick();

        if (!this.level().isClientSide) {
            Vec3 startPosition = this.position();
            Vec3 stopPosition = startPosition.add(this.getDeltaMovement());
            HitResult hitResult = this.level().clip(new ClipContext(stopPosition, stopPosition, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
            if (hitResult.getType() != HitResult.Type.MISS) {
                stopPosition = hitResult.getLocation();
            }

            List<EntityHitResult> entityHitResults = null;
            if (/*temp conditon*/ true) {
                EntityHitResult entityHitResult = this.getHitEntity(startPosition, stopPosition);
                if (entityHitResult != null) {
                    entityHitResults = Collections.singletonList(entityHitResult);
                }
            } else {
                this.getHitEntities(startPosition, stopPosition);
            }

            if (entityHitResults != null && entityHitResults.size() > 0) {
                for (EntityHitResult entityHitResult : entityHitResults) {
                    hitResult = entityHitResult;
                    if (((EntityHitResult)hitResult).getEntity() instanceof Player player) {
                        if (this.getAttackEntity() instanceof Player && !(((Player) this.getAttackEntity()).canHarmPlayer(player))) {
                            hitResult = null;
                        }
                    }
                    if (hitResult != null) {
                        this.onHit(hitResult, startPosition, stopPosition);
                    }
                }
            } else {
                this.onHit(hitResult, startPosition, stopPosition);
            }
        }
        double xPos = this.getX() + this.getDeltaMovement().x;
        double yPos = this.getY() + this.getDeltaMovement().y;
        double zPos = this.getZ() + this.getDeltaMovement().z;
        this.setPos(xPos, yPos, zPos);
        if (this.tickCount % this.life == 0) {
            if (!this.isRemoved()) {
                this.onEndTick();
            }
            this.remove(RemovalReason.KILLED);
        }
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double pDistance) {
        return true;
    }

    public void onHit(HitResult hitResult, Vec3 startPosition, Vec3 stopPosition) {
        if (hitResult instanceof  BlockHitResult blockHitResult) {
            if (blockHitResult.getType() == HitResult.Type.MISS) {
                return;
            }

            Vec3 vec3 = hitResult.getLocation();
            BlockPos blockPos = blockHitResult.getBlockPos();
            BlockState blockState = this.level().getBlockState(blockPos);
            Block block = blockState.getBlock();

            this.remove(RemovalReason.KILLED);

            this.onHitBlock(blockHitResult, vec3);

            if (block instanceof BellBlock bellBlock) {
                bellBlock.attemptToRing(this.level(), blockPos, blockHitResult.getDirection());
            }
            return;
        }

        if (hitResult instanceof EntityHitResult entityHitResult) {
            Entity entity = entityHitResult.getEntity();
            if (entity.getId() == this.getAttackEntityId()) {
                return;
            }

            this.onHitEntity(entity, startPosition, stopPosition);
            this.remove(RemovalReason.KILLED);

            entity.invulnerableTime = 0;
        }
    }

    public void onHitEntity(Entity entity, Vec3 startPosition, Vec3 stopPosition) {
        ItemStack itemStack = this.getWeaponStack();
        Item item = itemStack.getItem();
        if (item instanceof AbstractProjectileWeapon projectileWeapon) {
            float moddedDamage = projectileWeapon.getModdedDamage(itemStack);
            System.out.println(moddedDamage);

            float critChance = projectileWeapon.getModdedCritChance(itemStack);
            if (projectileWeapon.isCriticalHit(critChance)) {
                float critMultiplier = projectileWeapon.calculateCritDamage(itemStack);
                float guaranteedCrits = critChance > 1 ? Mth.floor(critChance) : 1;
                if (critChance > 1) {
                    float potentialCrit = critChance - Mth.floor(critChance);
                    if (projectileWeapon.isCriticalHit(potentialCrit)) {
                        guaranteedCrits += 1;
                    }
                }
                moddedDamage *= critMultiplier * guaranteedCrits;
            }
            System.out.println(moddedDamage);

            float elementalDamage = moddedDamage * (1 + WeaponUtil.getElementalDamageModifier(itemStack));
            System.out.println(elementalDamage);

            //Armor Resist
            if (entity instanceof TCEnemy<?> enemy) {
                elementalDamage = Mth.floor(elementalDamage * ThreadLocalRandom.current().nextFloat(0.25F, 0.55F));
            }
            System.out.println(elementalDamage);
            entity.hurt(entity.damageSources().generic(), elementalDamage);

            if (entity instanceof LivingEntity livingEntity) {
                if (RandomUtil.percentChance(projectileWeapon.getModdedStatus(itemStack))) {
                    System.out.println("Status");
                    Map<DamageType, Float> damageMap = Maps.newEnumMap(DamageType.class);
                    float damageModifier = (1 + WeaponUtil.getDamageModifier(itemStack));
                    for (Map.Entry<DamageType, Float> entry : projectileWeapon.getDamageMap().entrySet()) {
                        damageMap.put(entry.getKey(), entry.getValue() * damageModifier); //WORKS
                    }
                    for (Map.Entry<FrameAttribute, Float> entry : AttributeHandler.getFrameAttributes(itemStack).entrySet()) {
                        if (entry.getKey().getDamageType() != null) {
                            DamageType damageType = entry.getKey().getDamageType();
                            if (damageMap.containsKey(damageType)) {
                                damageMap.put(damageType, damageMap.get(damageType) * (1 + entry.getValue())); //TEST
                                break;
                            }
                            damageMap.put(damageType, moddedDamage * projectileWeapon.getModdedAttributeDamage(entry.getKey(), itemStack)); //WORKS
                        }
                    }
                    System.out.println(damageMap);
                    float statusDamage = (float) damageMap.values().stream().mapToDouble(Float::doubleValue).sum();
                    for (Map.Entry<DamageType, Float> entry : damageMap.entrySet()) {
                        if (RandomUtil.percentChance(entry.getValue() / statusDamage)) {
                            if (!this.level().isClientSide) {
                                if (entry.getKey().getFrameAttribute() != null) {
                                    FrameAttribute frameAttribute = entry.getKey().getFrameAttribute().get();
                                    DamageUtil.doPostDamageEffects(frameAttribute, this.getAttackEntity(), livingEntity);
                                    DamageUtil.doPostHurtEffects(frameAttribute, livingEntity, this.getAttackEntity());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void onHitBlock(BlockHitResult hitResult, Vec3 positionVec) {

    }

    public void onStartTick() {

    }

    public void onTick() {

    }

    public void onEndTick() {

    }

    @Nullable
    protected EntityHitResult getHitEntity(Vec3 startPosition, Vec3 stopPosition) {
        double d0 = Double.MAX_VALUE;
        Entity targetEntity = null;
        Vec3 vec3 = null;
        List<Entity> entityList = this.level().getEntities(this, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0), this::canHitEntity);
        for (Entity entity : entityList) {
            if (entity != this.getAttackEntity()) {
                EntityHitResult entityHitResult = this.getEntityHitResult(startPosition, stopPosition);
                if (entityHitResult == null)
                    continue;
                Vec3 hitLocation = entityHitResult.getLocation();
                double hitDistance = startPosition.distanceTo(hitLocation);
                if (hitDistance < d0) {
                    vec3 = hitLocation;
                    targetEntity = entity;
                    d0 = hitDistance;
                }
            }
        }
        return targetEntity == null ? null : new EntityHitResult(targetEntity, vec3);
    }

    @Nullable
    protected List<EntityHitResult> getHitEntities(Vec3 startPosition, Vec3 stopPosition) {
        List<EntityHitResult> hitResults = new ArrayList<>();
        List<Entity> entityList = this.level().getEntities(this, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0), this::canHitEntity);
        for (Entity entity : entityList) {
            if (!entity.equals(this.attackEntity)) {
                EntityHitResult entityHitResult = this.getEntityHitResult(startPosition, stopPosition);
                if (entityHitResult != null) {
                    hitResults.add(entityHitResult);
                }
            }
        }
        return hitResults;
    }

    public EntityHitResult getEntityHitResult(Vec3 startPosition, Vec3 stopPosition) {
        return ProjectileUtil.getEntityHitResult(this.getAttackEntity(), startPosition, stopPosition, this.getBoundingBox(), this::canHitEntity, 0.3);
    }

    public void interpolateMotion() {
        Vec3 vec3 = this.getDeltaMovement();
        double d0 = vec3.horizontalDistance();
        this.setXRot((float)(Mth.atan2(vec3.y(), d0) * (double)(180F / (float)Math.PI)));
        this.setYRot((float)(Mth.atan2(vec3.x(), vec3.z() * (double)(180F / (float)Math.PI))));
        this.xRotO = this.getXRot();
        this.yRotO = this.getYRot();
    }

    protected boolean canHitEntity(Entity entity) {
        return entity != null && entity.isPickable() && !entity.isSpectator();
    }

    private Vec3 getRotVector(ItemStack itemStack, float pitch, float yaw) {
        float f = Mth.cos(-yaw * 0.017453292F - (float) Math.PI);
        float f1 = Mth.sin(-yaw * 0.017453292F - (float) Math.PI);
        float f2 = -Mth.cos(-pitch * 0.017453292F);
        float f3 = Mth.sin(-pitch * 0.017453292F);
        return new Vec3((double) (f1 * f2), (double) f3, (double) (f * f2));
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        this.life = pCompound.getInt("Life");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putInt("life", this.life);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeInt(this.getAttackEntityId());

        if (this.getWeaponStack().isEmpty()) {
            buffer.writeShort(-1);
        } else {
            buffer.writeShort(Item.getId(this.getWeaponStack().getItem()));
            buffer.writeByte(this.getWeaponStack().getCount());
        }
        buffer.writeVarInt(this.life);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        this.attackEntityId = additionalData.readInt();

        int itemId = additionalData.readShort();
        if (itemId < 0) {
            this.weaponStack = ItemStack.EMPTY;
        } else {
            this.weaponStack = new ItemStack(Item.byId(itemId), additionalData.readByte());
        }

        this.life = additionalData.readVarInt();
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
