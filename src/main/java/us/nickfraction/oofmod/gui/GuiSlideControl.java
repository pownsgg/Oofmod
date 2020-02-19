package us.nickfraction.oofmod.gui;

import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;

import java.text.*;
import net.minecraft.client.*;
import org.lwjgl.opengl.*;

public class GuiSlideControl extends GuiButton
{
    public String label;
    public float curValue;
    public float minValue;
    public float maxValue;
    public boolean isSliding;
    public boolean useIntegers;
    private static DecimalFormat numFormat;

    public GuiSlideControl(int id, int x, int y, int width, int height, String displayString, float minVal, float maxVal, float curVal, boolean useInts) {
        super(id, x, y, width, height, useInts ? (displayString + (int)curVal) : (displayString + GuiSlideControl.numFormat.format(curVal)));
        this.label = displayString;
        this.minValue = minVal;
        this.maxValue = maxVal;
        this.curValue = (curVal - minVal) / (maxVal - minVal);
        this.useIntegers = useInts;
    }

    public float GetValueAsFloat() {
        return (this.maxValue - this.minValue) * this.curValue + this.minValue;
    }

    public int GetValueAsInt() {
        return (int)((this.maxValue - this.minValue) * this.curValue + this.minValue);
    }

    protected float roundValue(float value) {
        value = 0.01f * Math.round(value / 0.01f);
        return value;
    }

    public String GetLabel() {
        if (this.useIntegers) {
            return this.label + this.GetValueAsInt();
        }
        return this.label + GuiSlideControl.numFormat.format(this.GetValueAsFloat());
    }

    protected void SetLabel() {
        this.displayString = this.GetLabel();
    }

    public int getHoverState(boolean isMouseOver) {
        return 0;
    }

    protected void mouseDragged(Minecraft mc, int mousePosX, int mousePosY) {
        if (this.visible) {
            if (this.isSliding) {
                this.curValue = ((float)mousePosX - ((float)this.xPosition + 4)) / ((float)this.width - 8);
                if (this.curValue < 0.0f) {
                    this.curValue = 0.0f;
                }
                if (this.curValue > 1.0f) {
                    this.curValue = 1.0f;
                }
                this.SetLabel();
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
//            this.drawTexturedModalRect(xPosition + (int)(curValue * (float)(width - 8)), yPosition, 0, 66, 4, 20);
//            this.drawTexturedModalRect(xPosition + (int)(curValue * (float)(width - 8)) + 4, yPosition, 196, 66, 4, 20);
            this.drawRect(this.xPosition + 1, this.yPosition + 1, this.xPosition + (int)(this.curValue * (this.width - 8)) + 7, this.yPosition + height - 1, getColorWithAlpha(8168374, 100)); //70
            this.drawRect(this.xPosition + (int)(this.curValue * (this.width - 8)) + 1, this.yPosition + 1, this.xPosition + (int)(this.curValue * (this.width - 8)) + 7, this.yPosition + height - 1, getColorWithAlpha(8168374, 200)); //200
        }
    }

    public boolean mousePressed(Minecraft mc, int mousePosX, int mousePosY) {
        if (!super.mousePressed(mc, mousePosX, mousePosY)) {
            return false;
        }
        this.curValue = ((float)mousePosX - ((float)this.xPosition + 4)) / ((float)this.width - 8);
        if (this.curValue < 0.0f) {
            this.curValue = 0.0f;
        }
        if (this.curValue > 1.0f) {
            this.curValue = 1.0f;
        }
        this.SetLabel();
        if (this.isSliding) {
            return this.isSliding = false;
        }
        return this.isSliding = true;
    }

    public void mouseReleased(int mousePosX, int mousePosY) {
        this.isSliding = false;
    }

    @Override
    public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_)
    {
        if (this.visible)
        {
            FontRenderer fontrenderer = p_146112_1_.fontRendererObj;
            p_146112_1_.getTextureManager().bindTexture(buttonTextures);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = p_146112_2_ >= this.xPosition && p_146112_3_ >= this.yPosition && p_146112_2_ < this.xPosition + this.width && p_146112_3_ < this.yPosition + this.height; //!
            int k = this.getHoverState(this.visible);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            this.drawRect(xPosition + 1, yPosition + 1, xPosition + width - 1, yPosition + height - 1, getColorWithAlpha(8168374, 50));

            this.mouseDragged(p_146112_1_, p_146112_2_, p_146112_3_);
            int l = 14737632;

            if (packedFGColour != 0)
            {
                l = packedFGColour;
            }
            else if (!this.enabled)
            {
                l = 10526880;
            }
            else if (this.hovered)
            {
                l = 16777120;
            }

            this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, l);

            GL11.glColor3f(1.0F, 1.0F, 1.0F);
        }
    }

    private int getColorWithAlpha(int rgb, int a) {
        int r = rgb >> 16 & 0xFF;
        int g = rgb >> 8 & 0xFF;
        int b = rgb & 0xFF;
        return a << 24 | r << 16 | g << 8 | b;
    }

    static {
        GuiSlideControl.numFormat = new DecimalFormat("#.00");
    }
}
