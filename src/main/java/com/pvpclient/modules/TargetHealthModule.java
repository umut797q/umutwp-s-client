package com.pvpclient.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class TargetHealthModule {

    private static final int PAD    = 6;
    private static final int BAR_H  = 3;

    private static final int COLOR_BG     = 0xCC0D1117;
    private static final int COLOR_BORDER = 0x88FFFFFF;
    private static final int COLOR_NAME   = 0xFFFFFFFF;
    private static final int COLOR_BAR_BG = 0x44FFFFFF;
    private static final int COLOR_HIGH   = 0xFF4ADE80;
    private static final int COLOR_MED    = 0xFFFBBF24;
    private static final int COLOR_LOW    = 0xFFF87171;

    public void render(GuiGraphics graphics, Font font, int sw, int sh) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.hitResult == null) return;

        HitResult hit = mc.hitResult;
        if (hit.getType() != HitResult.Type.ENTITY) return;

        if (!(((EntityHitResult) hit).getEntity() instanceof LivingEntity target)) return;
        if (target == mc.player) return;

        float health    = target.getHealth();
        float maxHealth = target.getMaxHealth();
        float frac      = Math.max(0f, Math.min(1f, health / maxHealth));
        int   pct       = Math.round(frac * 100);

        int healthColor = frac > 0.60f ? COLOR_HIGH
                        : frac > 0.30f ? COLOR_MED
                        :                COLOR_LOW;

        String name      = target.getName().getString();
        String healthStr = String.format("%.1f / %.1f  (%d%%)", health, maxHealth, pct);

        int innerW = Math.max(font.width(name), font.width(healthStr));
        int panelW = innerW + PAD * 2;
        int panelH = PAD + 9 + 4 + BAR_H + 4 + 9 + PAD; 
        int panelX = (sw - panelW) / 2;
        int panelY = sh / 2 - panelH - 12;

        graphics.fill(panelX - 1, panelY - 1, panelX + panelW + 1, panelY + panelH + 1, COLOR_BORDER);
        graphics.fill(panelX,     panelY,     panelX + panelW,     panelY + panelH,     COLOR_BG);

        graphics.drawString(font, name, panelX + (panelW - font.width(name)) / 2, panelY + PAD, COLOR_NAME, true);

        int barX = panelX + PAD;
        int barW = panelW - PAD * 2;
        int barY = panelY + PAD + 9 + 4;
        graphics.fill(barX, barY, barX + barW, barY + BAR_H, COLOR_BAR_BG);
        int fillW = Math.round(barW * frac);
        if (fillW > 0) graphics.fill(barX, barY, barX + fillW, barY + BAR_H, healthColor);

        graphics.drawString(font, healthStr, panelX + (panelW - font.width(healthStr)) / 2, barY + BAR_H + 4, healthColor, true);
    }
}
