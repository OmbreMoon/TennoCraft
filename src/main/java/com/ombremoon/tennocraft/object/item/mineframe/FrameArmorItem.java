package com.ombremoon.tennocraft.object.item.mineframe;

import com.ombremoon.tennocraft.common.init.entity.TCDamageTypes;
import com.ombremoon.tennocraft.common.init.item.TCFrames;
import com.ombremoon.tennocraft.util.FrameUtil;
import net.minecraft.advancements.FrameType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class FrameArmorItem<T extends FrameArmorItem<T>> extends Item implements Equipable {
    private final EquipmentSlot equipmentSlot;

    public FrameArmorItem(EquipmentSlot equipmentSlot, Properties pProperties) {
        super(pProperties.stacksTo(1));
        this.equipmentSlot = equipmentSlot;
    }

    @Override
    public @Nullable EquipmentSlot getEquipmentSlot(ItemStack stack) {
        return this.equipmentSlot;
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (!pLevel.isClientSide()) {
            Player player = (Player) pEntity;
            if (FrameUtil.hasOnFrame(player)) {
                if (player.isInWater()) {
                    DamageSource damageSource = new DamageSource(player.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(TCDamageTypes.MALFUNCTION));
                    player.hurt(/*player.damageSources().generic()*/ damageSource, 1.0F);
                }
            }
        }
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
    }

    public FrameType getFrameType() {
        return FrameType.NONE;
    }

    //For Equipable Interface
    @Override
    public EquipmentSlot getEquipmentSlot() {
        return this.equipmentSlot;
    }

    public enum FrameType {
        NONE(null, 0, 0, 0, 0),
        VOLT(TCFrames.VOLT_HELMET, 270, 255, 105, 100),
        EXCALIBUR(TCFrames.EXCALIBUR_HELMET, 270, 270, 240, 100);

        private final Supplier<Item> frameArmorItem;
        private final int frameHealth;
        private final int frameShield;
        private final int frameArmor;
        private final int frameEnergy;

        FrameType(Supplier<Item> frameArmorItem, int frameHealth, int frameShield, int frameArmor, int frameEnergy) {
            this.frameArmorItem = frameArmorItem;
            this.frameHealth = frameHealth;
            this.frameShield = frameShield;
            this.frameArmor = frameArmor;
            this.frameEnergy = frameEnergy;
        }

        public Supplier<Item> getFrameArmorItem() {
            return frameArmorItem;
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
