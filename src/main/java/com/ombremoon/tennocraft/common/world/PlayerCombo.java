package com.ombremoon.tennocraft.common.world;

import com.ombremoon.tennocraft.common.api.weapon.schema.data.ComboType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PlayerCombo {
    private ComboType comboType;
    private int comboCount;
    private int comboIndex;
    private int hitIndex;
    private int comboDuration;
    private boolean tickCombo;

    public void tick(Player player, ItemStack stack) {
        if (this.tickCombo && this.comboDuration-- <= 0) {
            this.resetCombo(player, stack);
        }
    }

    public void incrementComboCount(int duration, float multiplier) {
        if (this.comboCount == 0)
            this.tickCombo = true;

        int comboGain = Mth.ceil(multiplier / 100);
        this.comboCount += comboGain;
        this.comboDuration = duration;
    }

    public void resetCombo(Player player, ItemStack stack) {
        this.comboCount = 0;
        this.comboIndex = 0;
        this.hitIndex = 0;
        this.tickCombo = false;
    }

    public void setComboType(ComboType comboType) {
        this.comboType = comboType;
    }

    public ComboType getComboType() {
        if (this.comboType == null) {
            this.comboType = ComboType.NEUTRAL;
        }

        return this.comboType;
    }

    public void setComboCount(int comboCount) {
        this.comboCount = comboCount;
    }

    public int getComboCount() {
        return this.comboCount;
    }

    public void setComboIndex(int comboIndex) {
        this.comboIndex = comboIndex;
    }

    public int getComboIndex() {
        return this.comboIndex;
    }

    public void setHitIndex(int hitIndex) {
        this.hitIndex = hitIndex;
    }

    public int getHitIndex() {
        return this.hitIndex;
    }
}
