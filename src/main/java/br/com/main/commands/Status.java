package br.com.main.commands;

import br.com.main.apis.Msg;
import br.com.main.entity.Duel;
import br.com.main.groups.manager.PermissionManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Status implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)){
            Msg.consoleNoPermMessage();
        } else{
            PermissionManager permissionManager = new PermissionManager();
            Player player = (Player) sender;
            if(command.getName().equalsIgnoreCase("status")){
                if(permissionManager.getPermission(player).equals("ADMIN")){
                    Msg.sendMessage(player,"§m-------------------------------------");
                    Msg.sendMessage(player,"§cPlayers em duelo : ["+ Duel.getInDuel().size()+"]");
                    Msg.sendMessage(player,"§cPlayers spectando : ["+ Spec.getInSpec().size()+"]");
                    Msg.sendMessage(player,"§cPlayers em Queue : ["+Duel.getInQueue().size()+"]");
                    Msg.sendMessage(player,"§cPlayers com tasks : ["+Duel.getListOfScheduler().size()+"]");
                    Msg.sendMessage(player,"§cPlayers em cooldown : ["+Duel.getCooldownTimer().size()+"]");
                    Msg.sendMessage(player,"§m-------------------------------------");
                }
            }
        }

        return false;
    }
}
