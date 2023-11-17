package com.ombremoon.tennocraft.object.item.mod;

import com.ombremoon.tennocraft.player.attribute.FrameAttribute;
import com.ombremoon.tennocraft.util.FrameUtil;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class SimpleModItem extends AbstractModItem {
    private final Supplier<FrameAttribute> frameAttribute;
    private final float initialModifier;

    public SimpleModItem(ModType modType, String modName, int maxRank, ModRarity modRarity, Supplier<FrameAttribute> frameAttribute, float initialModifier, Properties pProperties) {
        super(modType, modName, maxRank, modRarity, pProperties);
        this.frameAttribute = frameAttribute;
        this.initialModifier = initialModifier;
    }

    @Override
    protected void applyModifier(ItemStack itemStack) {
        ListTag listTag = itemStack.getTag().getList(FrameUtil.FRAME_ATTR, 10);
        listTag.add(FrameUtil.storeFrameAttribute(FrameUtil.getFrameAttributeId(getFrameAttribute()), getInitialModifier()));
        super.applyModifier(itemStack);
    }

    public FrameAttribute getFrameAttribute() {
        return this.frameAttribute.get();
    }

    public float getInitialModifier() {
        return this.initialModifier;
    }
}
