package us.nickfraction.oofmod.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;

import java.text.DecimalFormat;

public class GuiSlideControl extends GuiButton {
    public String label;
    public float curValue;
    public float minValue;
    public float maxValue;
    public boolean isSliding;
    public boolean useIntegers;
    private static DecimalFormat numFormat = new DecimalFormat("#.00");

    public GuiSlideControl(int id, int x, int y, int width, int height, String displayString, float minVal, float maxVal, float curVal, boolean useInts) {
        super(id, x, y, width, height, useInts ? (displayString + (int) curVal) : (displayString + GuiSlideControl.numFormat.format(curVal)));
        this.label = displayString;
        this.minValue = minVal;
        this.maxValue = maxVal;
        this.curValue = (curVal - minVal) / (maxVal - minVal);
        this.useIntegers = useInts;
    }

    public float getValueAsFloat() {
        return (this.maxValue - this.minValue) * this.curValue + this.minValue;
    }

    public int getValueAsInt() {
        return (int) ((this.maxValue - this.minValue) * this.curValue + this.minValue);
    }

    public String getLabel() {
        if (this.useIntegers) {
            return this.label + this.getValueAsInt();
        }
        return this.label + GuiSlideControl.numFormat.format(this.getValueAsFloat());
    }

    protected void setLabel() {
        this.displayString = this.getLabel();
    }

    public int getHoverState(boolean isMouseOver) {
        return 0;
    }

    protected void mouseDragged(Minecraft mc, int mousePosX, int mousePosY) {
        if (this.visible) {
            if (this.isSliding) {
                this.curValue = ((float) mousePosX - ((float) this.xPosition + 4)) / ((float) this.width - 8);
                if (this.curValue < 0.0f) {
                    this.curValue = 0.0f;
                }
                if (this.curValue > 1.0f) {
                    this.curValue = 1.0f;
                }
                this.setLabel();
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            drawRect(this.xPosition + 1, this.yPosition + 1, this.xPosition + (int) (this.curValue * (this.width - 8)) + 7, this.yPosition + height - 1, getColorWithAlpha(8168374, 100)); //70
            drawRect(this.xPosition + (int) (this.curValue * (this.width - 8)) + 1, this.yPosition + 1, this.xPosition + (int) (this.curValue * (this.width - 8)) + 7, this.yPosition + height - 1, getColorWithAlpha(8168374, 200)); //200
        }
    }

    public boolean mousePressed(Minecraft mc, int mousePosX, int mousePosY) {
        if (!super.mousePressed(mc, mousePosX, mousePosY)) {
            return false;
        }
        this.curValue = ((float) mousePosX - ((float) this.xPosition + 4)) / ((float) this.width - 8);
        if (this.curValue < 0.0f) {
            this.curValue = 0.0f;
        }
        if (this.curValue > 1.0f) {
            this.curValue = 1.0f;
        }
        this.setLabel();
        if (this.isSliding) {
            return this.isSliding = false;
        }
        return this.isSliding = true;
    }

    public void mouseReleased(int mousePosX, int mousePosY) {
        this.isSliding = false;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            FontRenderer fontrenderer = mc.fontRendererObj;
            mc.getTextureManager().bindTexture(buttonTextures);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            GlStateManager.enableBlend();
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            drawRect(xPosition + 1, yPosition + 1, xPosition + width - 1, yPosition + height - 1, getColorWithAlpha(8168374, 50));

            this.mouseDragged(mc, mouseX, mouseY);
            int l = 14737632;

            if (packedFGColour != 0) {
                l = packedFGColour;
            } else if (!this.enabled) {
                l = 10526880;
            } else if (this.hovered) {
                l = 16777120;
            }

            this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, l);

            GlStateManager.color(1.0F, 1.0F, 1.0F);
        }
    }

    private int getColorWithAlpha(int rgb, int a) {
        int r = rgb >> 16 & 0xFF;
        int g = rgb >> 8 & 0xFF;
        int b = rgb & 0xFF;
        return a << 24 | r << 16 | g << 8 | b;
    }
}
