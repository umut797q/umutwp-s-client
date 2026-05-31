package com.pvpclient.mixin;

import com.pvpclient.PvpClientMod;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.DeltaTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class InGameHudMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void pvpClient$onRenderTail(GuiGraphics graphics, DeltaTracker tickDelta, CallbackInfo ci) {
        PvpClientMod mod = PvpClientMod.getInstance();
        if (mod != null && mod.getHudRenderer() != null) {
            mod.getHudRenderer().render(graphics);
        }
    }
}
