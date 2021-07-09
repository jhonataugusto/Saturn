package br.com.main.commands;

import br.com.main.apis.Msg;
import br.com.main.groups.Permission;
import br.com.main.groups.manager.PermissionManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Tag implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        PermissionManager permissionManager = new PermissionManager();

        if(!(sender instanceof Player)){
            Msg.consoleNoPermMessage();
        } else {
            Player player = (Player) sender;
            if(command.getName().equalsIgnoreCase("tag")){
                switch (permissionManager.getPermission(player)) {
                    case "ADMIN":
                        player.sendMessage("§aTags disponíveis:§r \n §7§lMEMBRO§r \n §a§lSATURN§r \n §b§lYT§r \n §3§lYT+§r \n §2§lHELPER§r \n §d§lTRIAL§r \n §5§lMOD§r \n §c§lADMIN");
                        break;
                    case "MOD":
                        player.sendMessage("§aTags disponíveis:§r \n §7§lMEMBRO§r \n §a§lSATURN§r \n §b§lYT§r \n §3§lYT+§r \n §2§lHELPER§r \n §d§lTRIAL§r \n §5§lMOD");
                        break;
                    case "TRIAL":
                        player.sendMessage("§aTags disponíveis:§r \n §7§lMEMBRO§r \n §a§lSATURN§r \n §b§lYT§r \n §3§lYT+§r \n §2§lHELPER§r \n §d§lTRIAL");
                        break;
                    case "HELPER":
                        player.sendMessage("§aTags disponíveis:§r \n §7§lMEMBRO§r \n §a§lSATURN§r \n §b§lYT§r \n §3§lYT+§r \n §2§lHELPER");
                        break;
                    case "YOUTUBER_PLUS":
                        player.sendMessage("§aTags disponíveis:§r \n §7§lMEMBRO§r \n §a§lSATURN§r \n §b§lYT§r \n §3§lYT+");
                        break;
                    case "YOUTUBER":
                        player.sendMessage("§aTags disponíveis:§r \n §7§lMEMBRO§r \n §a§lSATURN§r \n §b§lYT");
                        break;
                    case "VIP":
                        player.sendMessage("§aTags disponíveis:§r \n §7§lMEMBRO§r \n §a§lSATURN");
                        break;
                    case "MEMBER":
                        player.sendMessage("§aTags disponíveis:§r \n §7§lMEMBRO");
                        break;
                }
            }
        }
        return false;
    }
}
