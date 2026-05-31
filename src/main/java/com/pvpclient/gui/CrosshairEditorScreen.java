package com.pvpclient.gui;

import com.pvpclient.ClientConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class CrosshairEditorScreen extends Screen {

    private final Screen parent;
    private final ClientConfig config = ClientConfig.get();
    
    private final int CELL_SIZE = 12;
    private final int GRID_SIZE = 16;
    
    private int gridX;
    private int gridY;

    public CrosshairEditorScreen(Screen parent) {
        super(Component.literal("Crosshair Editor"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        gridX = (this.width - (GRID_SIZE * CELL_SIZE)) / 2;
        gridY = (this.height - (GRID_SIZE * CELL_SIZE)) / 2;

        this.addRenderableWidget(Button.builder(Component.literal("Clear"), btn -> {
            for (int i = 0; i < GRID_SIZE; i++) {
                for (int j = 0; j < GRID_SIZE; j++) {
                    config.crosshairPixels[i][j] = false;
                }
            }
        }).bounds(gridX, gridY + GRID_SIZE * CELL_SIZE + 10, 80, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal("Done"), btn -> {
            this.minecraft.setScreen(parent);
        }).bounds(gridX + GRID_SIZE * CELL_SIZE - 80, gridY + GRID_SIZE * CELL_SIZE + 10, 80, 20).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 15, 0xFFFFFF);
        
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                int x = gridX + j * CELL_SIZE;
                int y = gridY + i * CELL_SIZE;
                
                boolean filled = config.crosshairPixels[i][j];
                
                graphics.fill(x, y, x + CELL_SIZE, y + CELL_SIZE, 0xFF555555);
                int color = filled ? 0xFFFFFFFF : 0xFF222222;
                graphics.fill(x + 1, y + 1, x + CELL_SIZE - 1, y + CELL_SIZE - 1, color);
            }
        }
        
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) return true;
        handleDraw(mouseX, mouseY, button);
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (super.mouseDragged(mouseX, mouseY, button, dragX, dragY)) return true;
        handleDraw(mouseX, mouseY, button);
        return true;
    }

    private void handleDraw(double mouseX, double mouseY, int button) {
        int mx = (int)mouseX;
        int my = (int)mouseY;
        
        if (mx >= gridX && mx < gridX + GRID_SIZE * CELL_SIZE && my >= gridY && my < gridY + GRID_SIZE * CELL_SIZE) {
            int col = (mx - gridX) / CELL_SIZE;
            int row = (my - gridY) / CELL_SIZE;
            
            if (row >= 0 && row < 16 && col >= 0 && col < 16) {
                if (button == 0) config.crosshairPixels[row][col] = true;
                else if (button == 1) config.crosshairPixels[row][col] = false;
            }
        }
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(parent);
    }
}
