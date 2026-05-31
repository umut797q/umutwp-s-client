package com.pvpclient.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import com.pvpclient.hud.HudRenderer;

public class FpsModule {

    private static final int PAD = 4;
    private int cachedWidth = 50;

    public void render(GuiGraphics graphics, Font font, int x, int y) {
        Minecraft mc = Minecraft.getInstance();
        int fps = mc.getFps();
        
        String text = "FPS: " + fps;
        cachedWidth = font.width(text) + PAD * 2;
        
        HudRenderer.drawRoundRect(graphics, x, y, cachedWidth, 14, HudRenderer.COLOR_PANEL_BG);
        graphics.drawString(font, text, x + PAD, y + 3, HudRenderer.COLOR_TEXT, true);
    }
    
    public int getWidth() {
        return cachedWidth;
    }
    
    public int getHeight() {
        return 14;
    }
}
