package com.ombremoon.tennocraft.object.item.mod;

import com.ombremoon.tennocraft.common.AttributeHandler;
import com.ombremoon.tennocraft.object.item.IModHolder;
import com.ombremoon.tennocraft.player.FrameAttribute;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class AbstractModItem extends Item implements IModAttribute {
    private final ModType modType;
    private final String modName;
    private final int maxRank;
    private final ModRarity modRarity;

    public AbstractModItem(ModType modType, String modName, int maxRank, ModRarity modRarity, Properties pProperties) {
        super(pProperties);
        this.modType = modType;
        this.modName = modName;
        this.maxRank = maxRank;
        this.modRarity = modRarity;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack modItem = pPlayer.getItemInHand(InteractionHand.MAIN_HAND);
        if (!pLevel.isClientSide()) {
            if (pPlayer.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof IModHolder modHolder) {
                ItemStack modHolderStack = pPlayer.getItemInHand(InteractionHand.OFF_HAND);
                ModType itemModType = modHolder.getModType();
                if (itemModType.equals(getModType())) {
                    if (isModPresent(modHolderStack)) {
                        installMod(pPlayer, modHolderStack);
                        return InteractionResultHolder.sidedSuccess(modItem, pLevel.isClientSide);
                    }
                }
            }
        }
        return InteractionResultHolder.pass(modItem);
    }

    public void installMod(Player player, ItemStack modHolder) {
        ListTag listTag = AttributeHandler.getFrameAttributeTags(modHolder);
        CompoundTag compoundTag = modHolder.getOrCreateTag();
        if (!compoundTag.contains("Frame Attributes", 9))
            compoundTag.put("Frame Attributes", listTag);

        compoundTag.putString(this.getName(), Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(this)).toString());
        applyModifier(player, modHolder);
    }

    //Overwrite to add modifiers to mods
    protected void applyModifier(Player player, ItemStack itemStack) {

    }

    public boolean isModPresent(ItemStack modHolder) {
        return modHolder.getTag() == null || modHolder.getTag().getString(this.getName()).isEmpty();
    }

    public void increaseModifier(FrameAttribute frameAttribute, ItemStack itemStack, float amount) {
        ListTag listTag = AttributeHandler.getFrameAttributeTags(itemStack);
        if (!listTag.isEmpty() && AttributeHandler.hasTagAttributeModifier(frameAttribute, itemStack)) {
            AttributeHandler.setTagAttributeModifier(frameAttribute, itemStack, amount + AttributeHandler.getTagAttributeModifier(frameAttribute, itemStack));
        } else {
            listTag.add(AttributeHandler.storeFrameAttribute(AttributeHandler.getFrameAttributeId(frameAttribute), amount));
        }
    }

    public ModType getModType() {
        return this.modType;
    }

    @Override
    public String getName() {
        return this.modName;
    }

    @Override
    public int getMaxRank() {
        return this.maxRank;
    }

    @Override
    public ModRarity getRarity() {
        return this.modRarity;
    }

    public enum ModRarity {
        COMMON,
        UNCOMMON,
        RARE,
        LEGENDARY,
        RIVEN,
        ARCHON,
        PECULIAR,
        AMALGAM,
        GALVANIZED,
        REQUIEM
    }
}
