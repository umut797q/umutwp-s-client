package com.pvpclient.mixin;

import com.pvpclient.ClientConfig;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.DeltaTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class InGameHudCrosshairMixin {

    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void pvpClient$onRenderCrosshair(GuiGraphics graphics, DeltaTracker tickDelta, CallbackInfo ci) {
        if (ClientConfig.get().customCrosshairEnabled) {
            // Cancel vanilla crosshair because we draw our custom one in HudRenderer
            ci.cancel();
        }
    }
}
