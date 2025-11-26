package com.ombremoon.tennocraft.common.world.effect;

import com.ombremoon.tennocraft.common.api.IModHolder;
import com.ombremoon.tennocraft.common.api.IWeaponModHolder;
import com.ombremoon.tennocraft.common.api.weapon.schema.Schema;
import com.ombremoon.tennocraft.common.api.weapon.schema.WeaponSchema;
import com.ombremoon.tennocraft.common.init.TCDamageTypes;
import com.ombremoon.tennocraft.common.init.TCData;
import com.ombremoon.tennocraft.main.Constants;
import com.ombremoon.tennocraft.util.ModHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.util.RandomUtil;
import org.apache.commons.lang3.mutable.MutableFloat;

public class BleedEffect extends StatusEffect {

    public BleedEffect(MobEffectCategory category, int maxStacks, int duration, int color) {
        super(category, maxStacks, duration, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, IModHolder<?> modHolder, int amplifier) {
        Level level = livingEntity.level();
        if (!level.isClientSide) {
            ServerLevel serverLevel = (ServerLevel) level;
            boolean flag = livingEntity instanceof Player;
            ProcEntries entries = livingEntity.getData(TCData.STATUS_PROCS);
            MutableFloat mutableFloat = new MutableFloat();
            entries.forEachProc(this, proc -> {
                LivingEntity attacker = proc.attacker();
                ItemStack weapon = proc.stack();
                Schema schema = modHolder.schema(weapon);
                float moddedBaseDamage = proc.damage() * ModHelper.modifyDamage(serverLevel, weapon, modHolder.schema(weapon), attacker, livingEntity);
                float modifier = flag ? 0.1F : 0.35F;
                float dotDamage = modifier * moddedBaseDamage
                        * ModHelper.modifyFactionDamage(serverLevel, weapon, modHolder.schema(weapon), attacker, livingEntity)
                        * ModHelper.modifyStatusDamage(serverLevel, weapon, modHolder.schema(weapon), attacker, livingEntity);

                if (schema instanceof WeaponSchema weaponSchema) {
                    var weaponHolder = (IWeaponModHolder<?>) modHolder;
                    float modifiedCritChance = weaponSchema.getModdedCritChance(serverLevel, weapon, attacker, livingEntity, weaponHolder.getTriggerType(weapon));
                    int tier = Mth.floor(modifiedCritChance);
                    float potentialCrit = modifiedCritChance % 1.0F;
                    if (potentialCrit != 0 && RandomUtil.percentChance(potentialCrit)) {
                        tier++;
                    }

                    float modifiedCritMult = weaponSchema.getModdedCritDamage(serverLevel, weapon, attacker, livingEntity, weaponHolder.getTriggerType(weapon));;
                    dotDamage *= 1 + tier * (modifiedCritMult - 1);
                }

                mutableFloat.setValue(mutableFloat.floatValue() + dotDamage);
            });

            if (entries.hasProcsFor(this)) {
                LivingEntity lastAttacker = entries.getLastProc(this).attacker();
                DamageSource source = new DamageSource(livingEntity.registryAccess().holderOrThrow(damageProc()), lastAttacker);
                //If player, hurt frame if present
                Constants.LOG.info("{}", mutableFloat.floatValue());
                return livingEntity.hurt(source, mutableFloat.floatValue());
            }
        }

        return false;
    }

    @Override
    public ResourceKey<DamageType> damageProc() {
        return TCDamageTypes.SLASH;
    }
}
