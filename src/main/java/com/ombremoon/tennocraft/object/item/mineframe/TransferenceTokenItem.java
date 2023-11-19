package com.ombremoon.tennocraft.object.item.mineframe;

import com.ombremoon.tennocraft.common.init.item.TCFrames;
import com.ombremoon.tennocraft.object.item.mod.ModType;
import com.ombremoon.tennocraft.player.ability.AbilityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class TransferenceTokenItem extends Item {
    protected Supplier<List<AbilityType<?>>> abilityList = ArrayList::new;
    protected float frameEnergy;

    public TransferenceTokenItem(Properties pProperties) {
        super(pProperties.stacksTo(1));
    }
    public float getFrameEnergy() {
        return this.frameEnergy;
    }

    public java.util.function.Supplier<List<AbilityType<?>>> getAbilityList() {
        return this.abilityList;
    }

    public FrameType getFrameType() {
        return FrameType.NONE;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag unused) {
        return CuriosApi.createCurioProvider(new ICurio() {

            @Override
            public ItemStack getStack() {
                return stack;
            }

            @Override
            public void curioTick(SlotContext slotContext) {
                // ticking logic here
            }
        });
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
