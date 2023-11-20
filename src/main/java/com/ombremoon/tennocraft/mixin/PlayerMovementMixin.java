package com.ombremoon.tennocraft.mixin;

import com.ombremoon.tennocraft.common.network.packet.client.data.TransferenceSyncData;
import com.ombremoon.tennocraft.util.FrameUtil;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class PlayerMovementMixin {
    private int jumpCount = 0;
    private boolean justJumped = false;

    @Inject(at = @At("HEAD"), method = "aiStep", remap = false)
    public void mixinAiStep(CallbackInfo info) {
        LocalPlayer player = (LocalPlayer) (Object) this;
        if (TransferenceSyncData.getHasOnFrame()) {
            if (player.onGround() || player.onClimbable()) {
                jumpCount = 1;
            } else if (!justJumped && jumpCount > 0 && player.getDeltaMovement().y < 0) {
                if (player.input.jumping && !player.getAbilities().flying) {
                    if (canJump(player)) {
                        jumpCount--;
                        player.jumpFromGround();
                    }
                }
            }
            justJumped = player.input.jumping;
        }
    }

    private boolean canJump(LocalPlayer localPlayer) {
        return !localPlayer.isFallFlying() && !localPlayer.isInWater() && !localPlayer.hasEffect(MobEffects.LEVITATION) && (localPlayer.getVehicle() == null);
    }
}
