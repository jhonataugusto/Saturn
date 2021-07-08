package br.com.main.commands;

import br.com.main.apis.Msg;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DefaultCommandsBukkit implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if(!(sender instanceof Player)){
        } else{
            if(command.getName().equalsIgnoreCase("tell")){
                return false;
            }
            if(command.getName().equalsIgnoreCase("pl")){
                return false;
            }
            if(command.getName().equalsIgnoreCase("ver") || command.getName().equalsIgnoreCase("about")){
                return false;
            }
            if(command.getName().equalsIgnoreCase("me")){
                return false;
            }
            if(command.getName().equalsIgnoreCase("nametagedit") || command.getName().equalsIgnoreCase("ne") || command.getName().equalsIgnoreCase("nte")){
                return false;
            }
            if(command.getName().equalsIgnoreCase("viaversion")){
                return false;
            }
        }
        return false;
    }
}
