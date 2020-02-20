package us.nickfraction.oofmod.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import us.nickfraction.oofmod.OofMod;

public class OpenMenuCommand extends CommandBase {

    private OofMod mod;

    public OpenMenuCommand(OofMod mod) {
        this.mod = mod;
    }

    @Override
    public String getCommandName() {
        return "oofmod";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "/" + getCommandName();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if(mod.getSettings().getSounds().size() == 0){
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_RED + "------[OOFMOD ERROR]------"));
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + ".minecraft/config/oofmod/sounds is empty!"));
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "Add some files before using this command."));

            IChatComponent download = new ChatComponentText(EnumChatFormatting.GOLD + "" + EnumChatFormatting.UNDERLINE + "CLICK HERE to download oof.wav.");
            download.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.mediafire.com/file/l7tv8u9dsleq7ul/oof.wav/file"));
            Minecraft.getMinecraft().thePlayer.addChatMessage(download);
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_RED + "-------------------------"));
            return;
        }
        mod.openMenu();
    }

    public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_)
    {
        return true;
    }

}
