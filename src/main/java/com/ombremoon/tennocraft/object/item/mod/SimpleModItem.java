package com.ombremoon.tennocraft.object.item.mod;

import com.ombremoon.tennocraft.player.FrameAttribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class SimpleModItem extends AbstractModItem {
    private final Supplier<FrameAttribute> frameAttribute;
    private final Supplier<FrameAttribute> frameAttribute1;
    private final float initialModifier;
    private final float initialModifier1;

    public SimpleModItem(ModType modType, String modName, int maxRank, ModRarity modRarity, Supplier<FrameAttribute> frameAttribute, float initialModifier, Properties pProperties) {
        this(modType, modName, maxRank, modRarity, frameAttribute, initialModifier, null, 0, pProperties);
    }

    public SimpleModItem(ModType modType, String modName, int maxRank, ModRarity modRarity, Supplier<FrameAttribute> frameAttribute, float initialModifier, @Nullable Supplier<FrameAttribute> frameAttribute1, float initialModifier1, Properties pProperties) {
        super(modType, modName, maxRank, modRarity, pProperties);
        this.frameAttribute = frameAttribute;
        this.frameAttribute1 = frameAttribute1;
        this.initialModifier = initialModifier;
        this.initialModifier1 = initialModifier1;
    }

    @Override
    protected void applyModifier(Player player, ItemStack itemStack) {
        increaseModifier(getFrameAttribute(), itemStack, getInitialModifier());
        if (getSecondFrameAttribute() != null) {
            increaseModifier(getSecondFrameAttribute().get(), itemStack, getSecondInitialModifier());
        }
        super.applyModifier(player, itemStack);
    }

    public FrameAttribute getFrameAttribute() {
        return this.frameAttribute.get();
    }

    public Supplier<FrameAttribute> getSecondFrameAttribute() {
        return this.frameAttribute1;
    }

    public float getInitialModifier() {
        return this.initialModifier;
    }

    public float getSecondInitialModifier() {
        return this.initialModifier1;
    }
}
