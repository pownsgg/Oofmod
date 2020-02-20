package us.nickfraction.oofmod;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import us.nickfraction.oofmod.commands.OpenMenuCommand;
import us.nickfraction.oofmod.gui.screens.GuiMainMenu;
import us.nickfraction.oofmod.listeners.OofModListener;
import us.nickfraction.oofmod.modcore.ModCoreInstaller;
import us.nickfraction.oofmod.settings.Settings;

@Mod(modid = OofMod.MODID, version = OofMod.VERSION, name = OofMod.NAME)
public class OofMod
{
    public static final String MODID = "refractionoof";
    public static final String VERSION = "2.0.1";
    public static final String NAME = "OofMod";

    private Settings settings;

    private boolean openMenu;

    public OofMod() throws Exception {
        settings = new Settings();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) throws Exception {
        ModCoreInstaller.initializeModCore(Minecraft.getMinecraft().mcDataDir);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new OofModListener(this));
        ClientCommandHandler.instance.registerCommand(new OpenMenuCommand(this));
        settings.loadConfig();
    }

    public Settings getSettings() {
        return settings;
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent e){
        if(openMenu){
            Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenu(this));
            openMenu = false;
        }
    }

    public void openMenu(){
        openMenu = true;
    }
}
