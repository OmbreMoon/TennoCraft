package com.ombremoon.tennocraft.common.world.entity;

import com.ombremoon.tennocraft.common.api.weapon.ranged.Bullet;
import com.ombremoon.tennocraft.common.api.weapon.ranged.projectile.ProjectileType;
import com.ombremoon.tennocraft.common.api.weapon.ranged.projectile.SolidProjectile;
import com.ombremoon.tennocraft.common.api.weapon.ranged.trigger.TriggerType;
import com.ombremoon.tennocraft.common.api.weapon.schema.data.RangedAttack;
import com.ombremoon.tennocraft.common.init.TCData;
import com.ombremoon.tennocraft.common.init.TCEntities;
import com.ombremoon.tennocraft.main.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.event.EventHooks;
import org.joml.Vector3f;
import org.slf4j.Logger;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;

public class BulletProjectile extends Projectile implements GeoEntity {
    private static final Logger LOGGER = Constants.LOG;
    private static final EntityDataAccessor<Vector3f> ORIGIN = SynchedEntityData.defineId(BulletProjectile.class, EntityDataSerializers.VECTOR3);
    private static final EntityDataAccessor<Float> PUNCH_THROUGH = SynchedEntityData.defineId(BulletProjectile.class, EntityDataSerializers.FLOAT);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    protected boolean inGround;
    private int life;
    private RangedAttack attackData;
    private ItemStack firedFromWeapon;
    private TriggerType<?> triggerType;
    private Holder<Bullet> bullet;

    public BulletProjectile(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    public BulletProjectile(
            Level level,
            LivingEntity shooter,
            RangedAttack attackData,
            SolidProjectile projectile,
            ItemStack firedFromWeapon,
            TriggerType<?> triggerType
    ) {
        super(TCEntities.BULLET_PROJECTILE.get(), level);
        this.attackData = attackData;
        this.triggerType = triggerType;
        this.bullet = projectile.bullet();
        if (level instanceof ServerLevel serverLevel) {
            if (firedFromWeapon.isEmpty())
                throw new IllegalArgumentException("Invalid weapon firing a bullet");

            this.firedFromWeapon = firedFromWeapon.copy();
        }

        this.setOwner(shooter);
        this.setData(TCData.PROJECTILE, projectile);
        this.setPos(shooter.position());
        this.setOrigin(shooter.position());
        this.setPunchThrough(attackData.punchThrough());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(ORIGIN, new Vector3f());
        builder.define(PUNCH_THROUGH, 0.0F);
    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        super.shoot(x, y, z, velocity, inaccuracy);
        this.life = 0;
    }

    @Override
    public void lerpTo(double x, double y, double z, float yRot, float xRot, int steps) {
        this.setPos(x, y, z);
        this.setRot(yRot, xRot);
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 vec3 = this.getDeltaMovement();
        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
            double d0 = vec3.horizontalDistance();
            this.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * 180.0F / (float)Math.PI));
            this.setXRot((float)(Mth.atan2(vec3.y, d0) * 180.0F / (float)Math.PI));
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }

        BlockPos blockpos = this.blockPosition();
        BlockState blockstate = this.level().getBlockState(blockpos);
        if (!blockstate.isAir() && this.getPunchThrough() > 0) {
            VoxelShape voxelshape = blockstate.getCollisionShape(this.level(), blockpos);
            if (!voxelshape.isEmpty()) {
                Vec3 vec31 = this.position();

                for (AABB aabb : voxelshape.toAabbs()) {
                    if (aabb.move(blockpos).contains(vec31)) {
                        this.inGround = true;
                        break;
                    }
                }
            }
        }

        SolidProjectile projectile = this.getData(TCData.PROJECTILE);
        Vec3 origin = this.getOrigin();
        double distToOrigin = this.distanceToSqr(origin);
        if (projectile != null) {
            boolean flag = projectile.lifetime().isPresent();
            if (!this.level().isClientSide) {
                this.tickDespawn(projectile);
            }

            if (projectile.range().isPresent()) {
                float range = projectile.range().get();
                if (distToOrigin >= range * range) {
                    if (flag) {
                        //Stop moving
                    } else if (!this.level().isClientSide) {
                        this.discard();
                    }
                }
            }
        }

        if (distToOrigin >= 64 * 64 && !this.level().isClientSide) {
            this.discard();
        }

        if (this.inGround) {
            //Stuck in ground logic
        } else {
            //In flight logic
            Vec3 vec32 = this.position();
            Vec3 vec33 = vec32.add(vec3);
            HitResult hitResult = this.level().clip(new ClipContext(vec32, vec33, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
            if (hitResult.getType() != HitResult.Type.MISS) {
                vec33 = hitResult.getLocation();
            }

            while (!this.isRemoved()) {
                EntityHitResult entityhitresult = this.findHitEntity(vec32, vec33);
                if (entityhitresult != null) {
                    hitResult = entityhitresult;
                }

                if (hitResult instanceof EntityHitResult result) {
                    Entity entity = result.getEntity();
                    Entity entity1 = this.getOwner();
                    if (entity instanceof Player player && entity1 instanceof Player player1 && !player1.canHarmPlayer(player)) {
                        hitResult = null;
                        entityhitresult = null;
                    }
                }

                if (hitResult != null && hitResult.getType() != HitResult.Type.MISS) {
                    if (EventHooks.onProjectileImpact(this, hitResult))
                        break;
                    ProjectileDeflection projectiledeflection = this.hitTargetOrDeflectSelf(hitResult);
                    this.hasImpulse = true;
                    if (projectiledeflection != ProjectileDeflection.NONE) {
                        break;
                    }
                }

                if (entityhitresult == null || this.getPunchThrough() <= 0) {
                    break;
                }

                hitResult = null;
            }

            vec3 = this.getDeltaMovement();
            double d0 = vec3.x;
            double d1 = vec3.y;
            double d2 = vec3.z;
            double d3 = this.getX() + d0;
            double d4 = this.getY() + d1;
            double d5 = this.getZ() + d2;
            double d6 = vec3.horizontalDistance();
            this.setYRot((float)(Mth.atan2(d0, d2) * 180.0F / (float)Math.PI));
            this.setXRot((float)(Mth.atan2(d1, d6) * 180.0F / (float)Math.PI));
            this.setXRot(lerpRotation(this.xRotO, this.getXRot()));
            this.setYRot(lerpRotation(this.yRotO, this.getYRot()));
            float f = 0.99F;
            if (this.isInWater()) {
                for (int j = 0; j < 4; j++) {
                    float f1 = 0.25F;
                    this.level().addParticle(ParticleTypes.BUBBLE, d3 - d0 * f1, d4 - d1 * f1, d5 - d2 * f1, d0, d1, d2);
                }

                f = this.getWaterInertia();
                this.setDeltaMovement(vec3.scale(f));
            }

            this.setDeltaMovement(vec3.scale(f));
            this.applyGravity();
            this.setPos(d3, d4, d5);
            this.checkInsideBlocks();
        }
    }

    @Override
    protected double getDefaultGravity() {
        SolidProjectile projectile = this.getData(TCData.PROJECTILE);
        return projectile != null && projectile.gravity().isPresent() ? projectile.gravity().get() : super.getDefaultGravity();
    }

    @Override
    protected AABB makeBoundingBox() {
        SolidProjectile projectile = this.getData(TCData.PROJECTILE);
        return projectile != null ? this.makeBoundingBox(projectile, this.position()) : super.makeBoundingBox();
    }

    public AABB makeBoundingBox(SolidProjectile projectile, Vec3 pos) {
        return this.makeBoundingBox(projectile, pos.x, pos.y, pos.z);
    }

    public AABB makeBoundingBox(SolidProjectile projectile, double x, double y, double z) {
        Vec3 dimensions = projectile.dimensions();
        float f = (float) dimensions.x / 2;
        float f1 = (float) dimensions.y / 2;
        float f2 = (float) dimensions.z / 2;
        return new AABB(x - f, y - f1, z - f2, x + f, y + f1, z + f2);
    }

    protected void tickDespawn(SolidProjectile projectile) {
        var optional = projectile.lifetime();
        if (optional.isPresent()) {
            int lifetime = optional.get();
            if (lifetime > 0) {
                if (this.life >= lifetime) {
                    this.discard();
                }
            } else {
                Entity owner = this.getOwner();
                if (owner instanceof LivingEntity livingEntity) {
//                    this.modHolder.onReload(livingEntity, livingEntity.level(), this, (byte) 0);

                    //On reload, check component for a list of persistent projectiles
                    //If not empty, iterate and call ^^
                }
            }

            this.life++;
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
    }

    @Nullable
    protected EntityHitResult findHitEntity(Vec3 startVec, Vec3 endVec) {
        return ProjectileUtil.getEntityHitResult(
                this.level(), this, startVec, endVec, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0), this::canHitEntity
        );
    }

    @Override
    protected boolean canHitEntity(Entity target) {
        return super.canHitEntity(target) /*&& Punch Through*/;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        SolidProjectile projectile = this.getData(TCData.PROJECTILE);
        if (projectile != null)
            projectile.save(compound);

        RangedAttack.CODEC
                .encodeStart(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), this.attackData)
                .resultOrPartial(LOGGER::error)
                .ifPresent(tag -> compound.put("Attack Data", tag));
        compound.put("Weapon", this.firedFromWeapon.save(this.registryAccess(), new CompoundTag()));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        ProjectileType.parse(compound)
                .ifPresent(projectileType -> this.setData(TCData.PROJECTILE, (SolidProjectile) projectileType));
        RangedAttack.CODEC
                .parse(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), compound.get("Attack Data"))
                .resultOrPartial(LOGGER::error)
                .ifPresent(attack -> this.attackData = attack);
        this.firedFromWeapon = ItemStack.parseOptional(this.registryAccess(), compound.getCompound("Weapon"));
    }

    @Override
    protected MovementEmission getMovementEmission() {
        return MovementEmission.NONE;
    }

    private void setOrigin(Vec3 origin) {
        this.entityData.set(ORIGIN, new Vector3f((float) origin.x, (float) origin.y, (float) origin.z));
    }

    public Vec3 getOrigin() {
        return new Vec3(this.entityData.get(ORIGIN));
    }

    private void setPunchThrough(float punchThrough) {
        this.entityData.set(PUNCH_THROUGH, punchThrough);
    }

    public float getPunchThrough() {
        return this.entityData.get(PUNCH_THROUGH);
    }

    protected float getWaterInertia() {
        return 0.6F;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
