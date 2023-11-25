/*
package com.ombremoon.tennocraft.object.entity.projectile;

import com.google.common.collect.Lists;
import com.ombremoon.tennocraft.common.init.entity.TCDamageTypes;
import com.ombremoon.tennocraft.util.DamageUtil;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AbstractBullet extends Projectile {
    private static final EntityDataAccessor<Byte> FLAGS = SynchedEntityData.defineId(AbstractBullet.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Float> PUNCTURE_MODIFIER = SynchedEntityData.defineId(AbstractBullet.class, EntityDataSerializers.FLOAT);
    private static final int PARTICLE = 1;
    private static final int NO_PHYSICS = 2;
    private static final int CRIT = 3;
    protected boolean hitBlock;
    private int life;
    private float baseDamage = 2;
    private int knockBack;
    private SoundEvent soundEvent = this.getDefaultHitGroundSoundEvent();
    @Nullable
    private IntOpenHashSet piercingIgnoreEntityIds;
    @Nullable
    private List<Entity> puncturedAndKilledEntities;

    private final IntOpenHashSet ignoredEntities = new IntOpenHashSet();

    protected AbstractBullet(EntityType<? extends AbstractBullet> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    protected AbstractBullet(EntityType<? extends AbstractBullet> pEntityType, double pX, double pY, double pZ, Level pLevel) {
        this(pEntityType, pLevel);
        this.setPos(pX, pY, pZ);
    }

    protected AbstractBullet(EntityType<? extends AbstractBullet> pEntityType, LivingEntity pShooter, Level pLevel) {
        this(pEntityType, pLevel);
        this.setOwner(pShooter);
    }

    public void setSoundEvent(SoundEvent soundEvent) {
        this.soundEvent = soundEvent;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(FLAGS, (byte)0);
        this.entityData.define(PUNCTURE_MODIFIER, (float)0);
    }

    @Override
    public void shoot(double pX, double pY, double pZ, float pVelocity, float pInaccuracy) {
        super.shoot(pX, pY, pZ, pVelocity, pInaccuracy);
        this.life = 0;
    }

    @Override
    public void lerpTo(double pX, double pY, double pZ, float pYRot, float pXRot, int pLerpSteps, boolean pTeleport) {
        super.lerpTo(pX, pY, pZ, pYRot, pXRot, pLerpSteps, pTeleport);
        this.setPos(pX, pY, pZ);
        this.setRot(pYRot, pXRot);
    }

    @Override
    public void lerpMotion(double pX, double pY, double pZ) {
        super.lerpMotion(pX, pY, pZ);
        this.life = 0;
    }

    @Override
    public void tick() {
        super.tick();
        boolean flag = this.isNoPhysics();
        Vec3 vec3 = this.getDeltaMovement();
        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
            double d0 = vec3.horizontalDistance();
            this.setYRot((float)(Mth.atan2(vec3.y, vec3.z) * (double)(180F / (float)Math.PI)));
            this.setXRot((float)(Mth.atan2(vec3.y, d0) * (double)(180F / (float)Math.PI)));
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }

        if (this.hitBlock && !flag) {
            if (!this.level().isClientSide) {
                Vec3 vec34 = this.position();
                this.doPostHitEffect(this.level(), vec34.x, vec34.y, vec34.z);
                this.discard();
            }
        } else {
            Vec3 vec31 = this.position();
            Vec3 vec32 = vec31.add(vec31);
            HitResult hitresult = this.level().clip(new ClipContext(vec31, vec32, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
            if (hitresult.getType() != HitResult.Type.MISS) {
                vec32 = hitresult.getLocation();
            }

            while (!this.isRemoved()) {
                EntityHitResult entityHitResult = this.findHitEnemy(vec31, vec32);
                if (entityHitResult != null) {
                    hitresult = entityHitResult;
                }

                if (hitresult != null & hitresult.getType() == HitResult.Type.ENTITY) {
                    Entity entity = ((EntityHitResult)hitresult).getEntity();
                    Entity owner = this.getOwner();
                    if (entity instanceof Player && owner instanceof Player && !((Player)owner).canHarmPlayer((Player) entity)) {
                        hitresult = null;
                        entityHitResult = null;
                    }
                }

                if (hitresult != null && hitresult.getType() != HitResult.Type.MISS && !flag) {
                    switch (ForgeEventFactory.onProjectileImpactResult(this, hitresult)) {
                        case SKIP_ENTITY -> {
                            if (hitresult.getType() != HitResult.Type.ENTITY) {
                                this.onHit(hitresult);
                                this.hasImpulse = true;
                                break;
                            }
                            ignoredEntities.add(entityHitResult.getEntity().getId());
                            entityHitResult = null;
                        }
                        case STOP_AT_CURRENT_NO_DAMAGE -> {
                            this.discard();
                            entityHitResult = null;
                        }
                        case STOP_AT_CURRENT -> {
                            this.setPunctureModifier((float)0);
                        }
                        case DEFAULT -> {
                            this.onHit(hitresult);
                            this.hasImpulse = true;
                        }
                    }
                }

                if (entityHitResult == null || this.getPunctureModifier() <= 0) {
                    break;
                }

                hitresult = null;
            }

            if (this.isRemoved())
                return;

            vec3 = this.getDeltaMovement();
            double d1 = vec3.x;
            double d2 = vec3.y;
            double d3 = vec3.z;
            if (this.isParticleBullet()) {
                doParticleEffects(this.level(), d1, d2, d3);
            }

            double d4 = this.getX() + d1;
            double d5 = this.getY() + d2;
            double d6 = this.getZ() + d3;
            double d7 = vec3.horizontalDistance();
            if (flag) {
                this.setYRot((float)(Mth.atan2(-d1, -d3) * (double)(180F / (float)Math.PI)));
            } else {
                this.setYRot((float)(Mth.atan2(d1, d3) * (double)(180F / (float)Math.PI)));
            }

            this.setXRot((float)(Mth.atan2(d2, d7) * (double)(180F / (float)Math.PI)));
            this.setXRot(lerpRotation(this.xRotO, this.getXRot()));
            this.setYRot(lerpRotation(this.yRotO, this.getYRot()));
            float f = 0.99F;
            float f1 = 0.05F;
            if (this.isInWater()) {
                for(int j = 0; j < 4; ++j) {
                    float f2 = 0.25F;
                    this.level().addParticle(ParticleTypes.BUBBLE, d4 - d1 * (double)f2, d5 - d2 * (double)f2, d6 - d3 * (double)f2, d1, d2, d3);
                }

                f = this.getWaterInertia();
            }

            this.setDeltaMovement(vec3.scale((double)f));
            if (!this.isNoGravity() && !flag) {
                Vec3 vec33 = this.getDeltaMovement();
                this.setDeltaMovement(vec33.x, vec33.y - (double)f1, vec33.z);
            }

            this.setPos(d4, d5, d6);
            this.checkInsideBlocks();
        }
    }

    private void resetPiercedEntities() {
        if (this.puncturedAndKilledEntities != null) {
            this.puncturedAndKilledEntities.clear();
        }

        if (this.piercingIgnoreEntityIds != null) {
            this.piercingIgnoreEntityIds.clear();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        Entity entity = pResult.getEntity();
        float f = (float)this.getDeltaMovement().length();
        //SUBJECT TO CHANGE
        int i = Mth.ceil(Mth.clamp((double)f * this.baseDamage, 0, (double)Integer.MAX_VALUE));
        if (this.getPunctureModifier() > 0) {
            if (this.piercingIgnoreEntityIds == null) {
                this.piercingIgnoreEntityIds = new IntOpenHashSet(5);
            }

            if (this.puncturedAndKilledEntities == null) {
                this.puncturedAndKilledEntities = Lists.newArrayListWithCapacity(5);
            }

            //SUBJECT TO CHANGE
            if (this.piercingIgnoreEntityIds.size() >= this.getPunctureModifier() + 1) {
                this.discard();
                return;
            }

            this.piercingIgnoreEntityIds.add(entity.getId());
        }

        if(this.isCritBullet()) {
            //Crit Logic
        }

        Entity owner = this.getOwner();
        DamageSource damageSource;
        if (owner == null) {
            damageSource = new DamageSource(entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(TCDamageTypes.BULLET));
        } else {
            damageSource = new DamageSource(owner.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(TCDamageTypes.BULLET));
            if (owner instanceof LivingEntity) {
                ((LivingEntity)owner).setLastHurtMob(entity);
            }
        }

        boolean flag = entity.getType() == EntityType.ENDERMAN;

        if (entity.hurt(damageSource, (float)i)) {
            if (flag) {
                return;
            }

            if (entity instanceof LivingEntity livingEntity) {
                if (this.knockBack > 0) {
                    double d0 = Math.max(0.0D, 1.0D - livingEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                    Vec3 vec3 = this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize().scale((double)this.knockBack * 0.6D * d0);
                    if (vec3.lengthSqr() > 0.0D) {
                        livingEntity.push(vec3.x, 0.1D, vec3.z);
                    }
                }

                if (!this.level().isClientSide && owner instanceof LivingEntity) {
                    DamageUtil.doPostHurtEffects(livingEntity, owner);
                    DamageUtil.doPostDamageEffects((LivingEntity)owner, livingEntity);
                }

                Vec3 vec31 = this.position();
                this.doPostHurtEffects(this.level(), livingEntity, vec31.x, vec31.y, vec31.z);
                if (owner != null && livingEntity != owner && livingEntity instanceof Player && owner instanceof ServerPlayer && !this.isSilent()) {
                    //TEMP
                }

                if (!entity.isAlive() && this.puncturedAndKilledEntities != null) {
                    this.puncturedAndKilledEntities.add(livingEntity);
                }
            }

            this.playSound(this.soundEvent, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
            if (this.getPunctureModifier() <= 0) {
                this.discard();
            }
        } else {
            if (!this.level().isClientSide) {
                this.discard();
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        this.playSound(this.getHitGroundSoundEvent(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        this.hitBlock = true;
        this.setCritBullet(false);
        this.setParticleBullet(false);
        this.setPunctureModifier((float)0);
        this.setSoundEvent(SoundEvents.ARROW_HIT);
        this.resetPiercedEntities();
    }

    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.ARROW_HIT;
    }

    protected SoundEvent getHitGroundSoundEvent() {
        return this.soundEvent;
    }

    protected void doPostHurtEffects(Level level, LivingEntity livingEntity, double vecX, double vecY, double vecZ) {
        doPostHitEffect(level, vecX, vecY, vecZ);
    }

    protected void doPostHitEffect(Level level, double vecX, double vecY, double vecZ) {

    }

    protected void doParticleEffects(Level level, double vecX, double vecY, double vecZ) {

    }

    @Nullable
    protected EntityHitResult findHitEnemy(Vec3 startVec, Vec3 endVec) {
        return ProjectileUtil.getEntityHitResult(this.level(), this, startVec, endVec, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D), this::canHitEntity);
    }

    @Override
    protected boolean canHitEntity(Entity pTarget) {
        return super.canHitEntity(pTarget);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putShort("life", (short) this.life);
        pCompound.putBoolean("hitBlock", this.hitBlock);
        pCompound.putFloat("damage", this.baseDamage);
        pCompound.putBoolean("crit", this.isCritBullet());
        pCompound.putBoolean("particle", this.isParticleBullet());
        pCompound.putFloat("PunctureModifier", this.getPunctureModifier());
        pCompound.putString("SoundEvent", ForgeRegistries.SOUND_EVENTS.getHolder(this.soundEvent).get().get().getLocation().toString());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.life = pCompound.getShort("life");
        this.hitBlock = pCompound.getBoolean("hitBlock");
        if (pCompound.contains("damage", 99)) {
            this.baseDamage = pCompound.getFloat("damage");
        }
        this.setCritBullet(pCompound.getBoolean("crit"));
        this.setParticleBullet(pCompound.getBoolean("particle"));
        this.setPunctureModifier(pCompound.getFloat("PunctureModifier"));
        if (pCompound.contains("SoundEvent", 8)) {
            this.soundEvent = ForgeRegistries.SOUND_EVENTS.getHolder(new ResourceLocation(pCompound.getString("SoundEvent"))).orElse(Holder.direct(this.getDefaultHitGroundSoundEvent())).get();
        }
    }

    @Override
    protected MovementEmission getMovementEmission() {
        return MovementEmission.NONE;
    }

    public void setBaseDamage(float baseDamage) {
        this.baseDamage = baseDamage;
    }

    public float getBaseDamage() {
        return this.baseDamage;
    }

    public void setKnockBack(int knockBack) {
        this.knockBack = knockBack;
    }

    public int getKnockBack() {
        return this.knockBack;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    protected float getEyeHeight(Pose pPose, EntityDimensions pDimensions) {
        return 0.13F;
    }

    public void setParticleBullet(boolean particleBullet) {
        this.setFlag(PARTICLE, particleBullet);
    }

    public void setCritBullet(boolean critBullet) {
        this.setFlag(CRIT, critBullet);
    }

    private void setPunctureModifier(float punctureModifier) {
        this.entityData.set(PUNCTURE_MODIFIER, punctureModifier);
    }

    private void setFlag(int id, boolean value) {
        byte b0 = this.entityData.get(FLAGS);
        if (value) {
            this.entityData.set(FLAGS, (byte)(b0 | id));
        } else {
            this.entityData.set(FLAGS, (byte)(b0 & ~id));
        }
    }

    public boolean isParticleBullet() {
        byte b0 = this.entityData.get(FLAGS);
        return (b0 & PARTICLE) != 0;
    }

    public boolean isCritBullet() {
        byte b0 = this.entityData.get(FLAGS);
        return (b0 & CRIT) != 0;
    }

    public float getPunctureModifier() {
        return this.entityData.get(PUNCTURE_MODIFIER);
    }

    public void setDamageEffectsFromEntity(LivingEntity shooter, float velocity) {

    }

    protected float getWaterInertia() {
        return 0.6f;
    }

    public void setNoPhysics(boolean noPhysics) {
        this.noPhysics = noPhysics;
        this.setFlag(NO_PHYSICS, noPhysics);
    }

    public boolean isNoPhysics() {
        if (!this.level().isClientSide) {
            return this.noPhysics;
        } else {
            return (this.entityData.get(FLAGS) & NO_PHYSICS) != 0;
        }
    }
}
*/
