package com.ombremoon.tennocraft.mixin;

import com.ombremoon.tennocraft.common.modholder.FrameHandler;
import com.ombremoon.tennocraft.util.FrameUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.ArmorSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorSlot.class)
public class ArmorSlotMixin {

    @Shadow @Final private LivingEntity owner;

    @Inject(method = "mayPlace", at = @At("RETURN"), cancellable = true)
    private void mayPlaceMixin(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        FrameHandler handler = FrameUtil.getFrameHandler(this.owner);
        cir.setReturnValue(!handler.isTransfered());
    }
}
