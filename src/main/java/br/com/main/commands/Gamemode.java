package br.com.main.commands;

import br.com.main.apis.Msg;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Gamemode implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)){
            Msg.consoleNoPermMessage();
        } else{
            if(command.getName().equalsIgnoreCase("gamemode")){
                return false;
            }
        }
        return false;
    }
}
