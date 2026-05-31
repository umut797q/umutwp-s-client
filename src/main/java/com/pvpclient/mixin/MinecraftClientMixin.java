package com.pvpclient.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MinecraftClientMixin {

    @Inject(method = "createTitle", at = @At("RETURN"), cancellable = true)
    private void pvpClient$modifyWindowTitle(CallbackInfoReturnable<String> cir) {
        String original = cir.getReturnValue();
        cir.setReturnValue("UmutWP's Client - " + original);
    }
}
