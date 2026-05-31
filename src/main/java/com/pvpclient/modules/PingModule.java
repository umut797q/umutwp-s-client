package com.pvpclient.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.PlayerInfo;
import com.pvpclient.hud.HudRenderer;

public class PingModule {

    private static final int PAD = 4;
    private int cachedWidth = 55;

    public void render(GuiGraphics graphics, Font font, int x, int y) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.getConnection() == null) return;
        
        PlayerInfo entry = mc.getConnection().getPlayerInfo(mc.player.getUUID());
        int ping = entry == null ? 0 : entry.getLatency();
        
        String text = "Ping: " + (ping == 0 ? "N/A" : ping + " ms");
        cachedWidth = font.width(text) + PAD * 2;
        
        int color = ping == 0 ? 0xFFAAAAAA : ping < 60 ? 0xFF4ADE80 : ping < 120 ? 0xFFFBBF24 : 0xFFF87171;
        
        HudRenderer.drawRoundRect(graphics, x, y, cachedWidth, 14, HudRenderer.COLOR_PANEL_BG);
        graphics.drawString(font, text, x + PAD, y + 3, color, true);
    }
    
    public int getWidth() {
        return cachedWidth;
    }
    
    public int getHeight() {
        return 14;
    }
}
