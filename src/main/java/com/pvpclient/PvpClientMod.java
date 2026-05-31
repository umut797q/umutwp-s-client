package com.pvpclient;

import com.pvpclient.hud.HudRenderer;
import com.pvpclient.modules.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.Screen;
import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class PvpClientMod implements ClientModInitializer {

    public static final String MOD_ID = "pvpclient";
    public static final String MOD_NAME = "UmutWP's Client";
    public static final String MOD_VERSION = "1.0";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static PvpClientMod instance;

    private HudRenderer hudRenderer;
    private KeystrokesModule keystrokesModule;
    private FpsModule fpsModule;
    private PingModule pingModule;
    private ArmorStatusModule armorStatusModule;
    private TargetHealthModule targetHealthModule;
    private CrosshairModule crosshairModule;

    public static KeyMapping guiKeyBinding;
    public static KeyMapping zoomKeyBinding;

    @Override
    public void onInitializeClient() {
        instance = this;
        LOGGER.info("[{}] Initializing PvP Client v{} (1.21.1 Mojang Mappings)", MOD_NAME, MOD_VERSION);

        keystrokesModule = new KeystrokesModule();
        fpsModule = new FpsModule();
        pingModule = new PingModule();
        armorStatusModule = new ArmorStatusModule();
        targetHealthModule = new TargetHealthModule();
        crosshairModule = new CrosshairModule();

        hudRenderer = new HudRenderer(keystrokesModule, fpsModule, pingModule, armorStatusModule, targetHealthModule, crosshairModule);

        registerKeyBindings();

        LOGGER.info("[{}] All modules loaded successfully!", MOD_NAME);
    }

    private void registerKeyBindings() {
        guiKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.pvpclient.gui",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_ALT,
                "category.pvpclient.keys"
        ));

        zoomKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.pvpclient.zoom",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_C,
                "category.pvpclient.keys"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ClientConfig cfg = ClientConfig.get();
            if (zoomKeyBinding != null && cfg.zoomEnabled) {
                cfg.isZooming = zoomKeyBinding.isDown();
            } else {
                cfg.isZooming = false;
            }

            // Smooth zoom interpolation
            float target = cfg.isZooming ? cfg.targetZoomMultiplier : 1.0f;
            cfg.currentZoomMultiplier += (target - cfg.currentZoomMultiplier) * 0.25f;

            while (guiKeyBinding != null && guiKeyBinding.consumeClick()) {
                if (Screen.hasControlDown()) {
                    client.setScreen(new com.pvpclient.gui.ClientSettingsScreen(client.screen));
                }
            }
        });
    }

    public static PvpClientMod getInstance() { return instance; }
    public HudRenderer getHudRenderer() { return hudRenderer; }
}
