package us.nickfraction.oofmod.settings;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class Settings {

    private File soundsFolder, configFile, selectedSound;

    private String selectedSoundName;
    private boolean enabled;
    private float volume;

    public Settings() throws Exception {
        configFile = new File(Minecraft.getMinecraft().mcDataDir, "config/oofmod/config.cfg");
        soundsFolder = new File("config/oofmod/sounds");

        if (!soundsFolder.exists()){
            soundsFolder.mkdirs();
        }

        selectedSoundName = "oof.wav";
        enabled = true;
        selectedSound = new File(soundsFolder.getPath() + "/" + selectedSoundName + ".wav");
        volume = -10.0f;

        downloadDefaultSound();
    }

    public void setSelectedSoundName(String soundName) {
        selectedSoundName = soundName;
        selectedSound = new File(soundsFolder.getPath() + "/" + selectedSoundName);
    }

    public ArrayList<File> getSounds(){
        ArrayList<File> result = new ArrayList<File>();

        for(File file : soundsFolder.listFiles()){
            if(file.getName().endsWith(".wav")){
                result.add(file);
            }
        }

        return result;
    }

    public File getSelectedSound(){
        return selectedSound;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public void saveConfig() throws Exception {
        Configuration config = new Configuration(this.configFile);
        this.updateConfig(config, false);
        config.save();
    };

    public void loadConfig() throws Exception {
        Configuration config = new Configuration(this.configFile);
        config.load();
        this.updateConfig(config, true);
        config.save();
    }

    public void updateConfig(Configuration config, boolean load) throws Exception {
        Property prop = config.get("global", "enabled", true);
        if (load) {
            enabled = prop.getBoolean();
        }
        else {
            prop.set(enabled);
        }

        prop = config.get("global", "selectedsound", "oof.wav");
        if (load) {
            setSelectedSoundName(prop.getString());

            if(selectedSound.exists()){
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(selectedSound.toURI().toURL());
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
            }
        }
        else {
            prop.set(selectedSoundName);
        }

        prop = config.get("global", "volume", 30f);
        if (load) {
            volume = (float) prop.getDouble();
        }
        else {
            prop.set(volume);
        }
    }

    private void downloadDefaultSound(){
        File defaultSound = new File("config/oofmod/sounds/oof.wav");
        if(defaultSound.exists()){
            return;
        }

        URL url;
        URLConnection con;
        DataInputStream dis;
        FileOutputStream fos;
        byte[] fileData;
        try {
            url = new URL("https://oofmodsound.powns.dev/"); //File Location goes here
            con = url.openConnection(); // open the url connection.
            dis = new DataInputStream(con.getInputStream());
            fileData = new byte[con.getContentLength()];
            for (int q = 0; q < fileData.length; q++) {
                fileData[q] = dis.readByte();
            }
            dis.close(); // close the data input stream
            fos = new FileOutputStream(new File(soundsFolder.getPath() + "/oof.wav")); //FILE Save Location goes here
            fos.write(fileData);  // write out the file we want to save.
            fos.close(); // close the output stream writer
        }
        catch(Exception m) {
            System.out.println(m);
        }
    }
}
