package com.pvpclient;

/**
 * Central configuration singleton — holds all module states, scales, positions, and custom crosshair data.
 */
public class ClientConfig {

    private static final ClientConfig INSTANCE = new ClientConfig();
    private ClientConfig() {}
    public static ClientConfig get() { return INSTANCE; }

    // ── Module toggles ────────────────────────────────────────────────────────
    public boolean keystrokesEnabled      = true;
    public boolean fpsEnabled             = true;
    public boolean pingEnabled            = true;
    public boolean armorEnabled           = true;
    public boolean zoomEnabled            = true;
    public boolean targetHealthEnabled    = true;
    public boolean customCrosshairEnabled = true;

    // ── Module scales [0.5 .. 1.5] ───────────────────────────────────────────
    public float keystrokesScale = 1.0f;
    public float fpsScale        = 1.0f;
    public float pingScale       = 1.0f;
    public float armorScale      = 1.0f;

    // ── Module positions (X, Y) ───────────────────────────────────────────────
    // These will be percentages (0.0 to 1.0) of the screen width/height to adapt to resizes,
    // or absolute pixel coordinates. Let's use absolute for simplicity, assuming a standard UI size,
    // but percentages are safer. Let's use absolute X and Y.
    // Default positions:
    public int keystrokesX = -1; // -1 means default position (top right)
    public int keystrokesY = -1;
    
    public int fpsX = -1;
    public int fpsY = -1;
    
    public int pingX = -1;
    public int pingY = -1;
    
    public int armorX = -1; // default bottom right
    public int armorY = -1;

    // ── Zoom runtime state ────────────────────────────────────────────────────
    public boolean isZooming             = false;
    public float   targetZoomMultiplier  = 0.25f;  // 25% of normal FOV = ~4× zoom
    public float   currentZoomMultiplier = 1.0f;

    // ── Custom Crosshair Data (16x16) ─────────────────────────────────────────
    // true = pixel is filled, false = empty
    public boolean[][] crosshairPixels = new boolean[16][16];

    // Initialize default crosshair pattern (a plus sign)
    {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                if ((i == 7 || i == 8) && j >= 4 && j <= 11) crosshairPixels[i][j] = true;
                if ((j == 7 || j == 8) && i >= 4 && i <= 11) crosshairPixels[i][j] = true;
                // hollow center
                if ((i == 7 || i == 8) && (j == 7 || j == 8)) crosshairPixels[i][j] = false;
            }
        }
    }

    // ── Scale helpers ─────────────────────────────────────────────────────────
    public static final float SCALE_MIN  = 0.5f;
    public static final float SCALE_MAX  = 1.5f;
    public static final float SCALE_STEP = 0.1f;

    public void stepScale(String module, int direction) {
        switch (module) {
            case "keystrokes" -> keystrokesScale = snap(keystrokesScale + SCALE_STEP * direction);
            case "fps"        -> fpsScale        = snap(fpsScale        + SCALE_STEP * direction);
            case "ping"       -> pingScale       = snap(pingScale       + SCALE_STEP * direction);
            case "armor"      -> armorScale      = snap(armorScale      + SCALE_STEP * direction);
        }
    }

    public float getScale(String module) {
        return switch (module) {
            case "keystrokes" -> keystrokesScale;
            case "fps"        -> fpsScale;
            case "ping"       -> pingScale;
            case "armor"      -> armorScale;
            default           -> 1.0f;
        };
    }

    private float snap(float v) {
        float rounded = Math.round(v * 10) / 10f;
        return Math.max(SCALE_MIN, Math.min(SCALE_MAX, rounded));
    }

    public String scalePercent(String module) {
        return Math.round(getScale(module) * 100) + "%";
    }
}
