package com.ombremoon.tennocraft.object.item.mineframe.helmet;

import com.ombremoon.tennocraft.common.init.custom.FrameAbilities;
import com.ombremoon.tennocraft.object.item.mineframe.VoltFrameItem;
import com.ombremoon.tennocraft.object.item.mineframe.helmet.FrameHelmetItem;
import com.ombremoon.tennocraft.player.ability.AbilityType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class VoltHelmetItem extends FrameHelmetItem<VoltFrameItem> {
    private final Supplier<List<AbilityType<?>>> voltAbilities = () -> new ArrayList<>() {{
        add(FrameAbilities.SHOCK_ABILITY.get());
        add(FrameAbilities.SPEED_ABILITY.get());
    }};

    public VoltHelmetItem(Properties pProperties) {
        super(pProperties);
        this.frameEnergy = 1000;
    }

    @Override
    public FrameType getFrameType() {
        return FrameType.VOLT;
    }

    @Override
    public Supplier<List<AbilityType<?>>> getAbilityList() {
        return voltAbilities;
    }
}
