package us.nickfraction.oofmod.listeners;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import us.nickfraction.oofmod.OofMod;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OofModListener {

    private Minecraft mc = Minecraft.getMinecraft();
    private OofMod mod;
    private String nameToCheck = "";

    public OofModListener(OofMod mod) {
        this.mod = mod;
    }

    @SubscribeEvent
    public void onDeathMessage(ClientChatReceivedEvent event) throws Exception {
        if (nameToCheck.isEmpty()) {
            nameToCheck = mc.thePlayer.getName();
        }

        String line = event.message.getUnformattedText();

        if (!mod.getSettings().isEnabled() || line.split(" ").length == 0) {
            return;
        }

        //TODO: Nicked players?
        String killMessageRegex = "(\\w{1,16}).+ (by|of|to|for|with) (" + nameToCheck + ")";
        String usernamePatternRegex = "^[a-zA-Z0-9_-]{3,16}$";

        Pattern killMessagePattern = Pattern.compile(killMessageRegex);
        Pattern usernamePattern = Pattern.compile(usernamePatternRegex);

        Matcher killMessageMatcher = killMessagePattern.matcher(line);
        Matcher usernameMatcher = usernamePattern.matcher(line.split(" ")[0]);

        if (usernameMatcher.matches() && killMessageMatcher.find()) {
            String killed = killMessageMatcher.group(1);
            if (!killed.equals(nameToCheck)) {
                playOofSound();
            }
        }
    }

    @SubscribeEvent
    public void profileCheck(TickEvent.ClientTickEvent event) {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        if (player == null) return;

        NetHandlerPlayClient sendQueue = player.sendQueue;
        if (sendQueue == null) return;

        for (NetworkPlayerInfo networkPlayerInfo : sendQueue.getPlayerInfoMap()) {
            GameProfile gameProfile = networkPlayerInfo.getGameProfile();

            if (gameProfile.getId() != null && gameProfile.getId().equals(Minecraft.getMinecraft().getSession().getProfile().getId())) {
                nameToCheck = gameProfile.getName();
                break;
            }
        }
    }

    private void playOofSound() throws Exception {
        File soundFile = mod.getSettings().getSelectedSound();

        AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile.toURI().toURL());
        Clip clip = AudioSystem.getClip();
        clip.open(audioIn);
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(mod.getSettings().getVolume() - 30f);
        clip.start();
    }

}
