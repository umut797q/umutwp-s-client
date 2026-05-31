package com.pvpclient.modules;

import net.minecraft.client.gui.GuiGraphics;
import com.pvpclient.ClientConfig;

public class CrosshairModule {

    private static final int COLOR_MAIN   = 0xFFFFFFFF; // white
    private static final int COLOR_SHADOW = 0x66000000; // semi-transparent black

    public void render(GuiGraphics graphics, int sw, int sh) {
        ClientConfig config = ClientConfig.get();
        boolean[][] pixels = config.crosshairPixels;

        // 16x16 grid, centered on screen
        int startX = sw / 2 - 8;
        int startY = sh / 2 - 8;

        // Render shadow first
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                if (pixels[i][j]) {
                    // x, y, width, height (draw 1x1 rect offset by 1px)
                    graphics.fill(startX + j + 1, startY + i + 1, startX + j + 2, startY + i + 2, COLOR_SHADOW);
                }
            }
        }

        // Render main crosshair
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                if (pixels[i][j]) {
                    graphics.fill(startX + j, startY + i, startX + j + 1, startY + i + 1, COLOR_MAIN);
                }
            }
        }
    }
}
