package com.pvpclient.mixin;

import com.pvpclient.ClientConfig;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    private void pvpClient$onGetFov(Camera camera, float partialTick, boolean useFOVSetting, CallbackInfoReturnable<Double> cir) {
        if (ClientConfig.get().zoomEnabled) {
            double fov = cir.getReturnValue();
            cir.setReturnValue(fov * ClientConfig.get().currentZoomMultiplier);
        }
    }
}
