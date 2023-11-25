package com.ombremoon.tennocraft.player.attribute;

import com.google.common.collect.Maps;
import com.ombremoon.tennocraft.common.init.custom.FrameAttributes;
import com.ombremoon.tennocraft.util.FrameUtil;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosCapability;

import java.util.Map;

public class FrameAttribute {
    private final ResourceLocation frameAttribute;
    private String descriptionId;
    private final float defaultValue;

    public FrameAttribute(@Nullable ResourceLocation frameAttribute, String descriptionId, float defaultValue) {
        this.frameAttribute = frameAttribute;
        this.descriptionId = descriptionId;
        this.defaultValue = defaultValue;
    }

    public  Map<Integer, ItemStack> getTransferenceSlotItems(Player player) {
        Map<Integer, ItemStack> map = Maps.newHashMap();
        for (int i = 0; i < FrameUtil.TRANSFERENCE_SLOTS; i++) {
            ItemStack itemStack = player.getCapability(CuriosCapability.INVENTORY).orElseThrow(NullPointerException::new).getStacksHandler(FrameUtil.CURIO_SLOT).get().getStacks().getStackInSlot(i);
            if (!itemStack.isEmpty()) {
                map.put(i, itemStack);
            }
        }
        return map;
    }

    protected String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("attribute", FrameAttributes.FRAME_ATTRIBUTES.getRegistryName());
        }
        return this.descriptionId;
    }

    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    public void doPostAttack(LivingEntity attacker, Entity target, float modifier) {

    }

    public void doPostHurt(LivingEntity attacker, Entity target, float modifier) {

    }

    public ResourceLocation getResourceLocation() {
        return this.frameAttribute;
    }

    public float getDefaultValue() {
        return this.defaultValue;
    }
}
