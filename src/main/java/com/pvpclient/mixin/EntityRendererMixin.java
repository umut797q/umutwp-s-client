package com.pvpclient.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.pvpclient.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity> {

    @Inject(method = "renderNameTag", at = @At("HEAD"))
    private void pvpClient$renderHealth(T entity, Component displayName, PoseStack poseStack, MultiBufferSource buffer, int packedLight, float partialTick, CallbackInfo ci) {
        if (!ClientConfig.get().targetHealthEnabled) return;

        if (entity instanceof Player player && player != Minecraft.getInstance().player) {
            double d0 = this.entityRenderDispatcher.distanceToSqr(entity);
            if (d0 <= 4096.0D) {
                float health = player.getHealth();
                String healthStr = String.format("%.1f \u2764", health); // 10.0 ❤
                
                int color = health > 15 ? 0xFF4ADE80 : health > 8 ? 0xFFFBBF24 : 0xFFF87171;

                boolean isSneaking = entity.isDiscrete();
                // 1.21 uses attachments, fallback to bbheight for simplicity
                float height = entity.getBbHeight() + 0.5f;
                
                poseStack.pushPose();
                poseStack.translate(0.0D, height + 0.25f, 0.0D); 
                poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
                poseStack.scale(-0.025F, -0.025F, 0.025F);

                Matrix4f matrix4f = poseStack.last().pose();
                Font font = Minecraft.getInstance().font;
                float xOffset = (float)(-font.width(healthStr) / 2);
                
                font.drawInBatch(healthStr, xOffset, 0, 0x20FFFFFF, false, matrix4f, buffer, isSneaking ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.NORMAL, 0x40000000, packedLight);
                font.drawInBatch(healthStr, xOffset, 0, color, false, matrix4f, buffer, isSneaking ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.NORMAL, 0, packedLight);
                
                poseStack.popPose();
            }
        }
    }

    @org.spongepowered.asm.mixin.Shadow
    @org.spongepowered.asm.mixin.Final
    protected net.minecraft.client.renderer.entity.EntityRenderDispatcher entityRenderDispatcher;
}
