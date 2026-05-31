package com.pvpclient.hud;

import com.pvpclient.modules.*;
import com.pvpclient.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public class HudRenderer {

    public static final int COLOR_LOGO_START  = 0xFF8B5CF6;
    public static final int COLOR_LOGO_END    = 0xFF06B6D4;
    public static final int COLOR_ACCENT      = 0xFF6EE7B7;
    public static final int COLOR_PANEL_BG    = 0xAA0D1117;
    public static final int COLOR_TEXT        = 0xFFFFFFFF;
    public static final int COLOR_TEXT_MUTED  = 0xFFADB5BD;
    public static final int COLOR_BORDER      = 0x44FFFFFF;

    private final KeystrokesModule   keystrokesModule;
    private final FpsModule          fpsModule;
    private final PingModule         pingModule;
    private final ArmorStatusModule  armorStatusModule;
    private final TargetHealthModule targetHealthModule;
    private final CrosshairModule    crosshairModule;

    public HudRenderer(KeystrokesModule keystrokesModule,
                       FpsModule fpsModule,
                       PingModule pingModule,
                       ArmorStatusModule armorStatusModule,
                       TargetHealthModule targetHealthModule,
                       CrosshairModule crosshairModule) {
        this.keystrokesModule   = keystrokesModule;
        this.fpsModule          = fpsModule;
        this.pingModule         = pingModule;
        this.armorStatusModule  = armorStatusModule;
        this.targetHealthModule = targetHealthModule;
        this.crosshairModule    = crosshairModule;
    }

    public void render(GuiGraphics graphics) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        Font font = mc.font;
        int sw = mc.getWindow().getGuiScaledWidth();
        int sh = mc.getWindow().getGuiScaledHeight();

        ClientConfig cfg = ClientConfig.get();

        renderLogo(graphics, font);

        if (cfg.keystrokesEnabled) {
            float s = cfg.keystrokesScale;
            int x = cfg.keystrokesX == -1 ? sw - keystrokesModule.getWidth() - 5 : cfg.keystrokesX;
            int y = cfg.keystrokesY == -1 ? 5 : cfg.keystrokesY;
            graphics.pose().pushPose();
            graphics.pose().translate(x, y, 0);
            graphics.pose().scale(s, s, 1.0f);
            keystrokesModule.render(graphics, font, 0, 0);
            graphics.pose().popPose();
        }

        if (cfg.fpsEnabled) {
            float s = cfg.fpsScale;
            int x = cfg.fpsX == -1 ? sw - fpsModule.getWidth() - 5 : cfg.fpsX;
            int defaultY = cfg.keystrokesEnabled ? 5 + (int)(keystrokesModule.getHeight() * cfg.keystrokesScale) + 5 : 5;
            int y = cfg.fpsY == -1 ? defaultY : cfg.fpsY;
            graphics.pose().pushPose();
            graphics.pose().translate(x, y, 0);
            graphics.pose().scale(s, s, 1.0f);
            fpsModule.render(graphics, font, 0, 0);
            graphics.pose().popPose();
        }
        
        if (cfg.pingEnabled) {
            float s = cfg.pingScale;
            int x = cfg.pingX == -1 ? sw - pingModule.getWidth() - 5 : cfg.pingX;
            int defaultYFPS = cfg.keystrokesEnabled ? 5 + (int)(keystrokesModule.getHeight() * cfg.keystrokesScale) + 5 : 5;
            int defaultY = cfg.fpsEnabled ? defaultYFPS + (int)(fpsModule.getHeight() * cfg.fpsScale) + 5 : defaultYFPS;
            int y = cfg.pingY == -1 ? defaultY : cfg.pingY;
            graphics.pose().pushPose();
            graphics.pose().translate(x, y, 0);
            graphics.pose().scale(s, s, 1.0f);
            pingModule.render(graphics, font, 0, 0);
            graphics.pose().popPose();
        }

        if (cfg.armorEnabled) {
            float s = cfg.armorScale;
            int x = cfg.armorX == -1 ? sw - armorStatusModule.getWidth() - 5 : cfg.armorX;
            int y = cfg.armorY == -1 ? sh - (int)(armorStatusModule.getHeight() * s) - 5 : cfg.armorY;
            graphics.pose().pushPose();
            graphics.pose().translate(x, y, 0);
            graphics.pose().scale(s, s, 1.0f);
            armorStatusModule.render(graphics, font, 0, 0);
            graphics.pose().popPose();
        }

        if (cfg.targetHealthEnabled) {
            targetHealthModule.render(graphics, font, sw, sh);
        }

        if (cfg.customCrosshairEnabled) {
            crosshairModule.render(graphics, sw, sh);
        }
    }

    private void renderLogo(GuiGraphics graphics, Font font) {
        String name    = "UmutWP's Client";
        String version = " v1.0";

        int nameW    = font.width(name);
        int versionW = font.width(version);
        int totalW   = nameW + versionW + 10;
        int totalH   = 9 + 10;

        drawRoundRect(graphics, 5, 5, totalW, totalH, COLOR_PANEL_BG);
        drawRoundRectOutline(graphics, 5, 5, totalW, totalH, COLOR_BORDER);

        graphics.drawString(font, name, 10, 10, COLOR_LOGO_START, true);
        graphics.drawString(font, version, 10 + nameW, 10, COLOR_LOGO_END, true);
    }

    public static void drawRoundRect(GuiGraphics ctx, int x, int y, int w, int h, int color) {
        ctx.fill(x + 2, y,     x + w - 2, y + h,     color);
        ctx.fill(x,     y + 2, x + w,     y + h - 2, color);
        ctx.fill(x + 1, y + 1,     x + 2,     y + 2,     color);
        ctx.fill(x + w - 2, y + 1, x + w - 1, y + 2,     color);
        ctx.fill(x + 1, y + h - 2, x + 2,     y + h - 1, color);
        ctx.fill(x + w - 2, y + h - 2, x + w - 1, y + h - 1, color);
    }

    public static void drawRoundRectOutline(GuiGraphics ctx, int x, int y, int w, int h, int color) {
        ctx.fill(x + 2, y,         x + w - 2, y + 1,         color); 
        ctx.fill(x + 2, y + h - 1, x + w - 2, y + h,         color); 
        ctx.fill(x,     y + 2,     x + 1,     y + h - 2,     color); 
        ctx.fill(x + w - 1, y + 2, x + w,     y + h - 2,     color); 
    }

    public int[] getModuleBounds(String name) {
        Minecraft mc = Minecraft.getInstance();
        int sw = mc.getWindow().getGuiScaledWidth();
        int sh = mc.getWindow().getGuiScaledHeight();
        ClientConfig cfg = ClientConfig.get();

        if (name.equals("keystrokes")) {
            float s = cfg.keystrokesScale;
            int x = cfg.keystrokesX == -1 ? sw - keystrokesModule.getWidth() - 5 : cfg.keystrokesX;
            int y = cfg.keystrokesY == -1 ? 5 : cfg.keystrokesY;
            return new int[]{x, y, (int)(keystrokesModule.getWidth() * s), (int)(keystrokesModule.getHeight() * s)};
        }
        if (name.equals("fps")) {
            float s = cfg.fpsScale;
            int x = cfg.fpsX == -1 ? sw - fpsModule.getWidth() - 5 : cfg.fpsX;
            int defaultY = cfg.keystrokesEnabled ? 5 + (int)(keystrokesModule.getHeight() * cfg.keystrokesScale) + 5 : 5;
            int y = cfg.fpsY == -1 ? defaultY : cfg.fpsY;
            return new int[]{x, y, (int)(fpsModule.getWidth() * s), (int)(fpsModule.getHeight() * s)};
        }
        if (name.equals("ping")) {
            float s = cfg.pingScale;
            int x = cfg.pingX == -1 ? sw - pingModule.getWidth() - 5 : cfg.pingX;
            int defaultYFPS = cfg.keystrokesEnabled ? 5 + (int)(keystrokesModule.getHeight() * cfg.keystrokesScale) + 5 : 5;
            int defaultY = cfg.fpsEnabled ? defaultYFPS + (int)(fpsModule.getHeight() * cfg.fpsScale) + 5 : defaultYFPS;
            int y = cfg.pingY == -1 ? defaultY : cfg.pingY;
            return new int[]{x, y, (int)(pingModule.getWidth() * s), (int)(pingModule.getHeight() * s)};
        }
        if (name.equals("armor")) {
            float s = cfg.armorScale;
            int x = cfg.armorX == -1 ? sw - armorStatusModule.getWidth() - 5 : cfg.armorX;
            int y = cfg.armorY == -1 ? sh - (int)(armorStatusModule.getHeight() * s) - 5 : cfg.armorY;
            return new int[]{x, y, (int)(armorStatusModule.getWidth() * s), (int)(armorStatusModule.getHeight() * s)};
        }
        return null;
    }
}
