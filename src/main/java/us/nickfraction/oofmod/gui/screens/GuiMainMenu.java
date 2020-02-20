package us.nickfraction.oofmod.gui.screens;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import us.nickfraction.oofmod.OofMod;
import us.nickfraction.oofmod.gui.GuiSlideControl;
import us.nickfraction.oofmod.gui.GuiTransButton;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;
import java.util.ArrayList;

public class GuiMainMenu extends GuiScreen {

    private OofMod mod;
    private GuiTransButton next, prev, enabled, preview, save;
    private GuiSlideControl volume;

    private int currentIndex;

    public GuiMainMenu(OofMod mod) {
        this.mod = mod;
        this.currentIndex = mod.getSettings().getSounds().indexOf(mod.getSettings().getSelectedSound());

        if(!mod.getSettings().getSelectedSound().exists()){
            currentIndex = 0;
            mod.getSettings().setSelectedSoundName(mod.getSettings().getSounds().get(currentIndex).getName());
        }
    }

    public void initGui(){
        buttonList.add(prev = new GuiTransButton(0, getCenter() - 46, getRowPos(2) + 10, 16, 16, "<"));
        buttonList.add(next = new GuiTransButton(1, getCenter() + 30, getRowPos(2) + 10, 16, 16, ">"));
        buttonList.add(preview = new GuiTransButton(2, getCenter() - 23, getRowPos(1) + 8, 46, 55, ""));
        buttonList.add(enabled = new GuiTransButton(3, getCenter() - 45, getRowPos(4) + 10, 90, 18, mod.getSettings().isEnabled() ? EnumChatFormatting.GREEN + "Enabled" : EnumChatFormatting.RED + "Disabled"));
        buttonList.add(volume = new GuiSlideControl(4, getCenter() - 45, getRowPos(5) + 10, 90, 18, "Volume: ", 0f, 30f, mod.getSettings().getVolume(), true));
        buttonList.add(save = new GuiTransButton(5, getCenter() - 45, height - 20, 90, 20, "Save"));
    }

    public int getRowPos(final int rowNumber) {
        return this.height / 4 + (24 * rowNumber - 24) -16;
    }

    public int getCenter() {
        return this.width / 2;
    }

    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        int yPosTitle = 10;

        super.drawDefaultBackground();
        GL11.glPushMatrix();
        GL11.glTranslatef((2 - 1.0f) * -(this.getCenter()), (2 - 1.0f) * -yPosTitle, 0.0f);
        GL11.glScaled(2, 2, 2);
        this.drawCenteredString(mc.fontRendererObj, "OofMod V2", this.getCenter(), yPosTitle, 815000);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef((0.7f - 1.0f) * -(this.getCenter()), (0.7f - 1.0f) * -yPosTitle + 12, 0.0f);
        GL11.glScaled(0.7, 0.7, 0.7);
        this.drawCenteredString(mc.fontRendererObj, "By Refraction & powns", this.getCenter(), yPosTitle + 12, 815000);
        GL11.glPopMatrix();

        super.drawScreen(mouseX, mouseY, partialTicks);

        renderUI();
        renderWavIcon();
    }

    protected void actionPerformed(GuiButton button){
        switch (button.id){
            case 0: {
                ArrayList<File> allSounds = mod.getSettings().getSounds();

                if(allSounds.size() <= 1){
                    return;
                }

                if(currentIndex - 1 < 0){
                    currentIndex = allSounds.size() -1;
                } else {
                    currentIndex--;
                }
                break;
            }
            case 1: {
                ArrayList<File> allSounds = mod.getSettings().getSounds();

                if(allSounds.size() <= 1){
                    return;
                }

                if(currentIndex + 1 > allSounds.size() - 1){
                    currentIndex = 0;
                } else {
                    currentIndex++;
                }
                break;
            }
            case 2: {
                try { previewSound(mod.getSettings().getSounds().get(currentIndex).getName(), volume.GetValueAsFloat() - 30f); } catch (Exception e) {}
                break;
            }
            case 3: {
                mod.getSettings().setEnabled(!mod.getSettings().isEnabled());
                button.displayString = mod.getSettings().isEnabled() ? EnumChatFormatting.GREEN + "Enabled" : EnumChatFormatting.RED + "Disabled";
                break;
            }
            case 5: {
                mod.getSettings().setVolume(volume.GetValueAsFloat());
                mod.getSettings().setSelectedSoundName(mod.getSettings().getSounds().get(currentIndex).getName());
                mc.displayGuiScreen(null);
                break;
            }
        }
    }

    protected void renderUI(){
        float f2 = (float)(8168374 >> 16 & 255) / 255.0F;
        float f3 = (float)(8168374 >> 8 & 255) / 255.0F;
        float f4 = (float)(8168374 & 255) / 255.0F;

        if(this.width >= 640 && this.height >= 350){
            GL11.glPushMatrix();
            GL11.glScaled(1, 1, 0);
            this.drawString(mc.fontRendererObj, "Version: 1.8.9 - " + OofMod.VERSION, 2, 2, 8168374);
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(f2, f3, f4, 0.3f);

            Tessellator tes = Tessellator.getInstance();
            WorldRenderer worldrenderer = tes.getWorldRenderer();
            worldrenderer.begin(4, DefaultVertexFormats.POSITION);
            worldrenderer.pos(0, 0, 0).endVertex();
            worldrenderer.pos(0, 140, 0).endVertex();
            worldrenderer.pos(140, 0, 0).endVertex();
            tes.draw();

            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(f2, f3, f4, 0.3f);

            Tessellator tes2 = Tessellator.getInstance();
            WorldRenderer worldrenderer2 = tes2.getWorldRenderer();
            worldrenderer2.begin(4, DefaultVertexFormats.POSITION);
            worldrenderer2.pos(this.width, this.height, 0).endVertex();
            worldrenderer2.pos(this.width, this.height - 140, 0).endVertex();
            worldrenderer2.pos(this.width - 140, this.height, 0).endVertex();
            tes.draw();

            GL11.glColor4f(1,1,1,1);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        }

    }

    private void renderWavIcon(){
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.color(1,1,1);

        mc.renderEngine.bindTexture(new ResourceLocation("wav-icon.png"));
        Gui.drawModalRectWithCustomSizedTexture(this.getCenter() - 20, getRowPos(1) + 10, 0, 0, 40, 51, 40, 51);

        GlStateManager.popMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef((0.8f - 1.0f) * -(this.getCenter()), (0.8f - 1.0f) * -(getRowPos(4) - 6), 0.0f);
        GL11.glScaled(0.8, 0.8, 0.8);
        this.drawCenteredString(mc.fontRendererObj, mod.getSettings().getSounds().get(currentIndex).getName(), this.getCenter(), getRowPos(4) - 6, -1);
        GL11.glPopMatrix();
    }

    private void previewSound(String fileName, float volume) throws Exception {
        File soundFile = new File("config/oofmod/sounds/" + fileName);

        AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile.toURI().toURL());
        Clip clip = AudioSystem.getClip();
        clip.open(audioIn);
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(volume);
        clip.start();
    }

    private int getColorWithAlpha(int rgb, int a) {
        int r = rgb >> 16 & 0xFF;
        int g = rgb >> 8 & 0xFF;
        int b = rgb & 0xFF;
        return a << 24 | r << 16 | g << 8 | b;
    }

    public void onGuiClosed()
    {
        try { mod.getSettings().saveConfig(); } catch (Exception e) {}
    }
}
