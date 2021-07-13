package br.com.main.commands;

import br.com.main.apis.Msg;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Ping implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)){
            Msg.consoleNoPermMessage();
        } else{
            if(command.getName().equalsIgnoreCase("ping")){
                Player player =  (Player) sender;
                if(args.length == 0){
                    int ping = ((CraftPlayer) player).getHandle().ping;
                    Msg.sendMessage(player,"§aO seu ping é de: "+ping);
                } else if(args.length == 1){
                    Player target = Bukkit.getServer().getPlayer(args[0]);
                    if(target == null){
                        player.sendMessage("§cEste jogador não está online");
                        return false;
                    }
                    int ping = ((CraftPlayer) target).getHandle().ping;
                    Msg.sendMessage(player,"§aO ping de "+target.getName()+" é de: "+ping);
                } else{
                    Msg.sendErrorMessage(player);
                }
            }

        }

        return false;
    }
}
