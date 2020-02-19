package us.nickfraction.oofmod.commands;

import club.sk1er.mods.core.ModCore;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import us.nickfraction.oofmod.OofMod;
import us.nickfraction.oofmod.gui.screens.GuiMainMenu;

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
        ModCore.getInstance().getGuiHandler().open(new GuiMainMenu(mod));
    }

    public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
        return true;
    }

}
