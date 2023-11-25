package com.ombremoon.tennocraft.object.item.mineframe;

import com.ombremoon.tennocraft.common.init.entity.TCDamageTypes;
import com.ombremoon.tennocraft.player.ability.AbilityType;
import com.ombremoon.tennocraft.util.FrameUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class TransferenceKeyItem extends Item implements ICurioItem {
//    protected Supplier<List<AbilityType<?>>> abilityList = ArrayList::new;

    public TransferenceKeyItem(Properties pProperties) {
        super(pProperties.stacksTo(1));
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (!pLevel.isClientSide()) {
            Player player = (Player) pEntity;
            if (FrameUtil.getFrameStack(player).getItem() instanceof TransferenceKeyItem && FrameUtil.hasOnFrame(player)) {
                if (player.isInWater()) {
                    DamageSource damageSource = new DamageSource(player.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(TCDamageTypes.MALFUNCTION));
                    player.hurt(damageSource, 1.0F);
                }
            }
        }
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
    }

//    public Supplier<List<AbilityType<?>>> getAbilityList() {
//        return this.abilityList;
//    }

    public abstract Supplier<List<AbilityType<?>>> getAbilityList();

//    public FrameType getFrameType() {
//        return FrameType.NONE;
//    }

    public abstract FrameType getFrameType();

    public enum FrameType {
        NONE(0, 0, 0, 0),
        VOLT(270, 255, 105, 100),
        EXCALIBUR(270, 270, 240, 100),
        MAG(180, 455, 105, 140);

        private final int frameHealth;
        private final int frameShield;
        private final int frameArmor;
        private final int frameEnergy;

        FrameType(int frameHealth, int frameShield, int frameArmor, int frameEnergy) {
            this.frameHealth = frameHealth;
            this.frameShield = frameShield;
            this.frameArmor = frameArmor;
            this.frameEnergy = frameEnergy;
        }

        public int getFrameHealth() {
            return this.frameHealth;
        }

        public int getFrameShield() {
            return this.frameShield;
        }

        public int getFrameArmor() {
            return this.frameArmor;
        }

        public int getFrameEnergy() {
            return this.frameEnergy;
        }
    }
}
