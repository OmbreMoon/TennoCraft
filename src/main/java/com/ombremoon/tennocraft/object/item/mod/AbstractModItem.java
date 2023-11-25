package com.ombremoon.tennocraft.object.item.mod;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

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

/*    @Override
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
    }*/

    public void installMod(Player player, ItemStack modHolder) {
        CompoundTag compoundTag = modHolder.getOrCreateTag();
        compoundTag.putString(this.getName(), this.getName());
        applyModifier(modHolder);
    }

    //Overwrite to add modifiers to mods
    protected void applyModifier(ItemStack itemStack) {

    }

    public boolean isModPresent(ItemStack modHolder) {
        return modHolder.getTag() == null || modHolder.getTag().getString(this.getName()).isEmpty();
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
