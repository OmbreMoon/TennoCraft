package com.ombremoon.tennocraft.object.item.mod;

import com.ombremoon.tennocraft.player.attribute.FrameAttribute;
import com.ombremoon.tennocraft.util.FrameUtil;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class CorruptedModItem extends AbstractModItem {
    private final Supplier<FrameAttribute> bonusAttribute;
    private final Supplier<FrameAttribute> penaltyAttribute;
    private final float bonusAttributeModifer;
    private final float penaltyAttributeModifer;

    public CorruptedModItem(ModType modType, String modName, Supplier<FrameAttribute> bonusAttribute, float bonusAttributeModifier, Supplier<FrameAttribute> penaltyAttribute, float penaltyAttributeModifier, Properties pProperties) {
        super(modType, modName, 10, ModRarity.RARE, pProperties);
        this.bonusAttribute = bonusAttribute;
        this.penaltyAttribute = penaltyAttribute;
        this.bonusAttributeModifer = bonusAttributeModifier;
        this.penaltyAttributeModifer = penaltyAttributeModifier;
    }

    @Override
    protected void applyModifier(ItemStack itemStack) {
        ListTag listTag = itemStack.getTag().getList(FrameUtil.FRAME_ATTR, 10);
        listTag.add(FrameUtil.storeFrameAttribute(FrameUtil.getFrameAttributeId(getBonusAttribute()), getBonusAttributeModifier()));
        listTag.add(FrameUtil.storeFrameAttribute(FrameUtil.getFrameAttributeId(getPenaltyAttribute()), getPenaltyAttributeModifier()));
        super.applyModifier(itemStack);
    }

    public FrameAttribute getBonusAttribute() {
        return this.bonusAttribute.get();
    }

    public FrameAttribute getPenaltyAttribute() {
        return this.penaltyAttribute.get();
    }

    public float getBonusAttributeModifier() {
        return this.bonusAttributeModifer;
    }

    public float getPenaltyAttributeModifier() {
        return this.penaltyAttributeModifer;
    }
}
