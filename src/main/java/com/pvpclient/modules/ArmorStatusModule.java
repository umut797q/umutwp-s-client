package com.pvpclient.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import com.pvpclient.hud.HudRenderer;

public class ArmorStatusModule {
    
    private static final int ICON_SIZE = 16;
    private static final int PAD = 4;
    private static final int ROW_H = 20;

    public void render(GuiGraphics graphics, Font font, int x, int y) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        int width = 90;
        int height = ROW_H * 5 + PAD;
        HudRenderer.drawRoundRect(graphics, x, y, width, height, HudRenderer.COLOR_PANEL_BG);

        EquipmentSlot[] slots = {
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.MAINHAND
        };

        int drawY = y + PAD;
        for (EquipmentSlot slot : slots) {
            ItemStack item = mc.player.getItemBySlot(slot);
            if (!item.isEmpty()) {
                graphics.renderItem(item, x + PAD, drawY);

                if (item.isDamageableItem()) {
                    int max = item.getMaxDamage();
                    int dmg = item.getDamageValue();
                    int dur = max - dmg;
                    float frac = Math.max(0f, Math.min(1f, (float)dur / max));
                    int pct = Math.round(frac * 100);

                    int color = frac > 0.6f ? 0xFF4ADE80 : frac > 0.3f ? 0xFFFBBF24 : 0xFFF87171;
                    
                    int barX = x + PAD + ICON_SIZE + PAD;
                    int barY = drawY + 8;
                    int barW = width - (PAD * 3 + ICON_SIZE) - 20;
                    graphics.fill(barX, barY, barX + barW, barY + 4, 0x44FFFFFF);
                    
                    int fillW = Math.round(barW * frac);
                    if (fillW > 0) graphics.fill(barX, barY, barX + fillW, barY + 4, color);

                    graphics.drawString(font, pct + "%", barX + barW + 2, drawY + 4, color, true);
                } else {
                    graphics.drawString(font, "∞", x + PAD + ICON_SIZE + PAD, drawY + 4, 0xFFAAAAAA, true);
                }
            }
            drawY += ROW_H;
        }
    }
    
    public int getWidth() {
        return 90;
    }
    
    public int getHeight() {
        return ROW_H * 5 + PAD;
    }
}
