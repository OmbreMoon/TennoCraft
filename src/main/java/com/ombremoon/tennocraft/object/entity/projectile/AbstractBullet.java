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
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import net.tslat.smartbrainlib.util.RandomUtil;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class AbstractBullet extends Projectile implements IEntityAdditionalSpawnData {
    protected int attackEntityId;
    protected LivingEntity attackEntity;
    private ItemStack weaponStack;
    private AbstractProjectileWeapon projectileWeapon;
    protected float range;
    protected int life;
    protected BlockPos startPos;

    public AbstractBullet(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public AbstractBullet(EntityType<? extends Projectile> pEntityType, double pX, double pY, double pZ, BlockPos startPos, Level pLevel) {
        this(pEntityType, pLevel);
        this.setPos(pX, pY, pZ);
        this.startPos = startPos;
    }

    public AbstractBullet(EntityType<? extends Projectile> pEntityType, Level pLevel, LivingEntity attackEntity, ItemStack weaponStack, AbstractProjectileWeapon projectileWeapon) {
        this(pEntityType, attackEntity.getX(), attackEntity.getEyeY() - (double)0.1F, attackEntity.getZ(), attackEntity.getOnPos(), pLevel);
        this.attackEntityId = attackEntity.getId();
        this.attackEntity = attackEntity;
        this.weaponStack = weaponStack;
        this.range = projectileWeapon.getRange();
        this.life = projectileWeapon.getProjectileLife();
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

    public BlockPos getStartPos() {
        return this.startPos;
    }

    public AbstractProjectileWeapon getProjectileWeapon() {
        return this.projectileWeapon;
    }

    @Override
    public void tick() {
        super.tick();
        BlockPos blockPos = this.blockPosition();
        Vec3 vec3 = this.getDeltaMovement();
        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
            double d0 = vec3.horizontalDistance();
            this.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * (double)(180F / (float)Math.PI)));
            this.setXRot((float)(Mth.atan2(vec3.y, d0) * (double)(180F / (float)Math.PI)));
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }

        if (blockPos.distToCenterSqr(startPos.getCenter()) >= this.range) {
            this.discard();
        }

        if (!this.isRemoved()) {
            Vec3 vec32 = this.position();
            Vec3 vec33 = vec32.add(vec3);
            HitResult hitresult = this.level().clip(new ClipContext(vec32, vec33, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
            if (hitresult.getType() != HitResult.Type.MISS) {
                vec33 = hitresult.getLocation();
            }

            EntityHitResult entityhitresult = this.findHitEntity(vec32, vec33);
            if (entityhitresult != null) {
                hitresult = entityhitresult;
            }

            if (hitresult != null && hitresult.getType() == HitResult.Type.ENTITY) {
                Entity entity = ((EntityHitResult) hitresult).getEntity();
                Entity entity1 = this.getOwner();
                if (entity instanceof Player && entity1 instanceof Player && !((Player) entity1).canHarmPlayer((Player) entity)) {
                    hitresult = null;
                    entityhitresult = null;
                }
            }

            if (hitresult != null && hitresult.getType() != HitResult.Type.MISS) {
                this.onHit(hitresult);
                this.hasImpulse = true;
            }

            hitresult = null;


            vec3 = this.getDeltaMovement();
            double d5 = vec3.x;
            double d6 = vec3.y;
            double d1 = vec3.z;

            double d7 = this.getX() + d5;
            double d2 = this.getY() + d6;
            double d3 = this.getZ() + d1;
            double d4 = vec3.horizontalDistance();
            this.setYRot((float) (Mth.atan2(d5, d1) * (double) (180F / (float) Math.PI)));

            this.setXRot((float) (Mth.atan2(d6, d4) * (double) (180F / (float) Math.PI)));
            this.setXRot(lerpRotation(this.xRotO, this.getXRot()));
            this.setYRot(lerpRotation(this.yRotO, this.getYRot()));

            this.setDeltaMovement(vec3.scale((double) 0.99F));
            Vec3 vec34 = this.getDeltaMovement();
            this.setDeltaMovement(vec34.x, vec34.y - (double) 0.05F, vec34.z);

            this.setPos(d7, d2, d3);
        }

        if (this.tickCount >= this.life) {
            this.discard();
        }
    }

    @Override
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        this.discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        Entity entity = pResult.getEntity();
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

    @Nullable
    protected EntityHitResult findHitEntity(Vec3 pStartVec, Vec3 pEndVec) {
        return ProjectileUtil.getEntityHitResult(this.level(), this, pStartVec, pEndVec, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D), this::canHitEntity);
    }

    @Override
    protected void defineSynchedData() {

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
        buffer.writeFloat(this.range);
        buffer.writeVarInt(this.life);
        buffer.writeBlockPos(this.startPos);
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

        this.range = additionalData.readFloat();
        this.life = additionalData.readVarInt();
        this.startPos = additionalData.readBlockPos();
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
