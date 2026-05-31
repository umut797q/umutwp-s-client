package com.pvpclient.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.KeyMapping;
import com.pvpclient.hud.HudRenderer;
import java.util.LinkedList;
import java.util.Queue;

public class KeystrokesModule {

    private static final int PAD = 2;
    private static final int BOX_SIZE = 20;
    
    private boolean lastLmb = false;
    private boolean lastRmb = false;
    
    private final Queue<Long> lmbClicks = new LinkedList<>();
    private final Queue<Long> rmbClicks = new LinkedList<>();

    public void render(GuiGraphics graphics, Font font, int x, int y) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options == null) return;

        long now = System.currentTimeMillis();
        
        // Update CPS
        boolean lmbDown = mc.options.keyAttack.isDown();
        boolean rmbDown = mc.options.keyUse.isDown();
        
        if (lmbDown && !lastLmb) lmbClicks.add(now);
        if (rmbDown && !lastRmb) rmbClicks.add(now);
        
        lastLmb = lmbDown;
        lastRmb = rmbDown;
        
        while (!lmbClicks.isEmpty() && now - lmbClicks.peek() > 1000) lmbClicks.poll();
        while (!rmbClicks.isEmpty() && now - rmbClicks.peek() > 1000) rmbClicks.poll();

        int cpsL = lmbClicks.size();
        int cpsR = rmbClicks.size();

        // Coordinates logic inside HudRenderer, we just draw at (x, y)
        // W A S D layout
        // W is top middle
        drawKey(graphics, font, mc.options.keyUp, x + BOX_SIZE + PAD, y);
        // A S D
        drawKey(graphics, font, mc.options.keyLeft, x, y + BOX_SIZE + PAD);
        drawKey(graphics, font, mc.options.keyDown, x + BOX_SIZE + PAD, y + BOX_SIZE + PAD);
        drawKey(graphics, font, mc.options.keyRight, x + (BOX_SIZE + PAD) * 2, y + BOX_SIZE + PAD);
        
        // LMB and RMB with CPS
        int row3Y = y + (BOX_SIZE + PAD) * 2;
        int mouseWidth = (BOX_SIZE * 3 + PAD * 2 - PAD) / 2;
        
        drawMouseKey(graphics, font, "LMB", lmbDown, cpsL, x, row3Y, mouseWidth);
        drawMouseKey(graphics, font, "RMB", rmbDown, cpsR, x + mouseWidth + PAD, row3Y, mouseWidth);
    }

    private void drawKey(GuiGraphics graphics, Font font, KeyMapping key, int x, int y) {
        boolean down = key.isDown();
        String name = key.getTranslatedKeyMessage().getString().toUpperCase();
        
        // Background
        int bgColor = down ? HudRenderer.COLOR_ACCENT : HudRenderer.COLOR_PANEL_BG;
        int textColor = down ? 0xFF000000 : HudRenderer.COLOR_TEXT;
        
        HudRenderer.drawRoundRect(graphics, x, y, BOX_SIZE, BOX_SIZE, bgColor);
        
        // Text
        int tw = font.width(name);
        graphics.drawString(font, name, x + (BOX_SIZE - tw) / 2, y + (BOX_SIZE - 9) / 2 + 1, textColor, !down);
    }

    private void drawMouseKey(GuiGraphics graphics, Font font, String name, boolean down, int cps, int x, int y, int width) {
        int height = BOX_SIZE;
        int bgColor = down ? HudRenderer.COLOR_ACCENT : HudRenderer.COLOR_PANEL_BG;
        int textColor = down ? 0xFF000000 : HudRenderer.COLOR_TEXT;
        
        HudRenderer.drawRoundRect(graphics, x, y, width, height, bgColor);
        
        // Name
        int tw = font.width(name);
        graphics.drawString(font, name, x + (width - tw) / 2, y + 3, textColor, !down);
        
        // CPS
        String cpsStr = cps + " CPS";
        int cw = font.width(cpsStr);
        // Draw smaller or just below
        graphics.drawString(font, cpsStr, x + (width - cw) / 2, y + 12, down ? 0xFF333333 : HudRenderer.COLOR_TEXT_MUTED, !down);
    }
    
    public int getWidth() {
        return BOX_SIZE * 3 + PAD * 2;
    }
    
    public int getHeight() {
        return BOX_SIZE * 3 + PAD * 2;
    }
}
