package com.pvpclient.gui;

import com.pvpclient.ClientConfig;
import com.pvpclient.PvpClientMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class ClientSettingsScreen extends Screen {

    private final Screen parent;
    private final ClientConfig config = ClientConfig.get();
    
    private String dragging = null;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;

    public ClientSettingsScreen(Screen parent) {
        super(Component.literal("UmutWP's Client - Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int startY = 40;
        int rowHeight = 24;
        
        int leftColX = centerX - 160;
        int rightColX = centerX + 10;
        int btnWidth = 150;

        this.addRenderableWidget(Button.builder(getToggleText("Keystrokes", config.keystrokesEnabled), btn -> {
            config.keystrokesEnabled = !config.keystrokesEnabled;
            btn.setMessage(getToggleText("Keystrokes", config.keystrokesEnabled));
        }).bounds(leftColX, startY, btnWidth, 20).build());
        addScaleControls("keystrokes", rightColX, startY);

        startY += rowHeight;
        this.addRenderableWidget(Button.builder(getToggleText("FPS", config.fpsEnabled), btn -> {
            config.fpsEnabled = !config.fpsEnabled;
            btn.setMessage(getToggleText("FPS", config.fpsEnabled));
        }).bounds(leftColX, startY, btnWidth, 20).build());
        addScaleControls("fps", rightColX, startY);
        
        startY += rowHeight;
        this.addRenderableWidget(Button.builder(getToggleText("Ping", config.pingEnabled), btn -> {
            config.pingEnabled = !config.pingEnabled;
            btn.setMessage(getToggleText("Ping", config.pingEnabled));
        }).bounds(leftColX, startY, btnWidth, 20).build());
        addScaleControls("ping", rightColX, startY);

        startY += rowHeight;
        this.addRenderableWidget(Button.builder(getToggleText("Armor Status", config.armorEnabled), btn -> {
            config.armorEnabled = !config.armorEnabled;
            btn.setMessage(getToggleText("Armor Status", config.armorEnabled));
        }).bounds(leftColX, startY, btnWidth, 20).build());
        addScaleControls("armor", rightColX, startY);

        startY += rowHeight;
        this.addRenderableWidget(Button.builder(getToggleText("Target Health", config.targetHealthEnabled), btn -> {
            config.targetHealthEnabled = !config.targetHealthEnabled;
            btn.setMessage(getToggleText("Target Health", config.targetHealthEnabled));
        }).bounds(leftColX, startY, btnWidth, 20).build());

        startY += rowHeight;
        this.addRenderableWidget(Button.builder(getToggleText("Custom Crosshair", config.customCrosshairEnabled), btn -> {
            config.customCrosshairEnabled = !config.customCrosshairEnabled;
            btn.setMessage(getToggleText("Custom Crosshair", config.customCrosshairEnabled));
        }).bounds(leftColX, startY, btnWidth, 20).build());
        
        this.addRenderableWidget(Button.builder(Component.literal("Edit Crosshair..."), btn -> {
            this.minecraft.setScreen(new CrosshairEditorScreen(this));
        }).bounds(rightColX, startY, btnWidth, 20).build());

        startY += rowHeight;
        this.addRenderableWidget(Button.builder(getToggleText("Smooth Zoom (C)", config.zoomEnabled), btn -> {
            config.zoomEnabled = !config.zoomEnabled;
            btn.setMessage(getToggleText("Smooth Zoom (C)", config.zoomEnabled));
        }).bounds(leftColX, startY, btnWidth, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal("Done"), btn -> {
            this.minecraft.setScreen(parent);
        }).bounds(centerX - 100, this.height - 30, 200, 20).build());
    }

    private Component getToggleText(String name, boolean enabled) {
        return Component.literal(name + ": " + (enabled ? "§aON" : "§cOFF"));
    }

    private void addScaleControls(String moduleId, int x, int y) {
        Button display = Button.builder(Component.literal(config.scalePercent(moduleId)), btn -> {})
                .bounds(x + 24, y, 102, 20).build();
        display.active = false;
        
        this.addRenderableWidget(Button.builder(Component.literal("-"), btn -> {
            config.stepScale(moduleId, -1);
            display.setMessage(Component.literal(config.scalePercent(moduleId)));
        }).bounds(x, y, 20, 20).build());

        this.addRenderableWidget(display);

        this.addRenderableWidget(Button.builder(Component.literal("+"), btn -> {
            config.stepScale(moduleId, 1);
            display.setMessage(Component.literal(config.scalePercent(moduleId)));
        }).bounds(x + 130, y, 20, 20).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 15, 0xFFFFFF);
        graphics.drawCenteredString(this.font, "HUD modüllerini fareyle sürükleyip taşıyabilirsiniz.", this.width / 2, 25, 0xAAAAAA);
        super.render(graphics, mouseX, mouseY, partialTick);
        
        drawHighlightBoxes(graphics, mouseX, mouseY);
    }
    
    private void drawHighlightBoxes(GuiGraphics graphics, int mx, int my) {
        String[] mods = {"keystrokes", "fps", "ping", "armor"};
        for (String mod : mods) {
            int[] b = PvpClientMod.getInstance().getHudRenderer().getModuleBounds(mod);
            if (b != null) {
                boolean hovered = mx >= b[0] && mx <= b[0] + b[2] && my >= b[1] && my <= b[1] + b[3];
                int color = hovered ? 0x88FFFFFF : 0x33FFFFFF;
                if (mod.equals(dragging)) color = 0xAA6EE7B7;
                graphics.fill(b[0], b[1], b[0] + b[2], b[1] + b[3], color);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) return true;
        
        if (button == 0) {
            String[] mods = {"keystrokes", "fps", "ping", "armor"};
            for (String mod : mods) {
                int[] b = PvpClientMod.getInstance().getHudRenderer().getModuleBounds(mod);
                if (b != null && mouseX >= b[0] && mouseX <= b[0] + b[2] && mouseY >= b[1] && mouseY <= b[1] + b[3]) {
                    dragging = mod;
                    dragOffsetX = (int)mouseX - b[0];
                    dragOffsetY = (int)mouseY - b[1];
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (dragging != null) {
            int nx = (int)mouseX - dragOffsetX;
            int ny = (int)mouseY - dragOffsetY;
            switch(dragging) {
                case "keystrokes" -> { config.keystrokesX = nx; config.keystrokesY = ny; }
                case "fps"        -> { config.fpsX = nx; config.fpsY = ny; }
                case "ping"       -> { config.pingX = nx; config.pingY = ny; }
                case "armor"      -> { config.armorX = nx; config.armorY = ny; }
            }
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }
    
    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (dragging != null && button == 0) {
            dragging = null;
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(parent);
    }
}
