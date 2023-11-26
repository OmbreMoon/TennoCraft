package com.ombremoon.tennocraft.object.item.mod;

import com.ombremoon.tennocraft.player.FrameAttribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class CorruptedModItem extends AbstractModItem {
    private final Supplier<FrameAttribute> bonusAttribute;
    private final Supplier<FrameAttribute> penaltyAttribute;
    private final float bonusAttributeModifier;
    private final float penaltyAttributeModifier;

    public CorruptedModItem(ModType modType, String modName, Supplier<FrameAttribute> bonusAttribute, float bonusAttributeModifier, Supplier<FrameAttribute> penaltyAttribute, float penaltyAttributeModifier, Properties pProperties) {
        super(modType, modName, 10, ModRarity.RARE, pProperties);
        this.bonusAttribute = bonusAttribute;
        this.penaltyAttribute = penaltyAttribute;
        this.bonusAttributeModifier = bonusAttributeModifier;
        this.penaltyAttributeModifier = penaltyAttributeModifier;
    }

    @Override
    protected void applyModifier(Player player, ItemStack itemStack) {
        increaseModifier(getBonusAttribute(), itemStack, this.getBonusAttributeModifier());
        increaseModifier(getPenaltyAttribute(), itemStack, this.getPenaltyAttributeModifier() );
        super.applyModifier(player, itemStack);
    }

    public FrameAttribute getBonusAttribute() {
        return this.bonusAttribute.get();
    }

    public FrameAttribute getPenaltyAttribute() {
        return this.penaltyAttribute.get();
    }

    public float getBonusAttributeModifier() {
        return this.bonusAttributeModifier;
    }

    public float getPenaltyAttributeModifier() {
        return this.penaltyAttributeModifier;
    }
}
