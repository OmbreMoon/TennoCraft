package com.ombremoon.tennocraft.common.networking.serverbound;

import com.ombremoon.tennocraft.common.api.IRangedModHolder;
import com.ombremoon.tennocraft.common.api.handler.RangedWeaponHandler;
import com.ombremoon.tennocraft.common.api.mod.WeaponModContainer;
import com.ombremoon.tennocraft.common.api.weapon.ranged.projectile.ProjectileType;
import com.ombremoon.tennocraft.common.api.weapon.ranged.projectile.SolidProjectile;
import com.ombremoon.tennocraft.common.api.weapon.ranged.reload.ReloadType;
import com.ombremoon.tennocraft.common.api.weapon.ranged.trigger.*;
import com.ombremoon.tennocraft.common.api.weapon.schema.RangedWeaponSchema;
import com.ombremoon.tennocraft.common.api.weapon.schema.data.RangedAttack;
import com.ombremoon.tennocraft.common.init.TCData;
import com.ombremoon.tennocraft.common.networking.PayloadHandler;
import com.ombremoon.tennocraft.common.world.entity.BulletProjectile;
import com.ombremoon.tennocraft.main.CommonClass;
import com.ombremoon.tennocraft.main.Constants;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.tslat.smartbrainlib.util.RandomUtil;

import java.util.ArrayList;
import java.util.List;

public record ShootProjectilePayload(boolean useFallback, boolean updateReloadTime) implements CustomPacketPayload {
    public static final Type<ShootProjectilePayload> TYPE = new Type<>(CommonClass.customLocation("shoot_projectile"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ShootProjectilePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, ShootProjectilePayload::useFallback,
            ByteBufCodecs.BOOL, ShootProjectilePayload::updateReloadTime,
            ShootProjectilePayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ShootProjectilePayload payload, final IPayloadContext context) {
        ServerPlayer player = (ServerPlayer) context.player();
        ServerLevel level = (ServerLevel) player.level();
        ItemStack stack = player.getMainHandItem();
        RangedWeaponHandler handler = stack.get(TCData.RANGED_WEAPON_HANDLER);
        if (stack.getItem() instanceof IRangedModHolder modHolder && handler != null) {
            RangedWeaponSchema schema = modHolder.schema(stack);
            TriggerType<?> type = modHolder.getTriggerType(player, stack);
            type = type.is(ChargeTrigger.TYPE) && payload.useFallback ? new FallbackTrigger() : type;

            if (type.canFire(level, stack, (WeaponModContainer) modHolder.getMods(player, stack), handler)) {
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
                    multishot = autoSpool.spoolMultiShot(handler.shotCounter, multishot);
                    ammoCost = autoSpool.spoolAmmoCost(handler.shotCounter, ammoCost);
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

                handler.shotCounter++;
                handler.isFiring = true;
                PayloadHandler.updateBulletHandler(player, payload.updateReloadTime);
            }
        }
    }
}
