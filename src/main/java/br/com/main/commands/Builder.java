package br.com.main.commands;

import br.com.main.apis.Msg;
import br.com.main.entity.Duel;
import br.com.main.entity.MatchResult;
import br.com.main.groups.manager.PermissionManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Builder implements CommandExecutor {

    @Getter
    private static final List<UUID> inBuild = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            Msg.consoleNoPermMessage();
        } else {
            if(command.getName().equalsIgnoreCase("build")){
                PermissionManager permissionManager = new PermissionManager();
                Player player = (Player) sender;
                if (permissionManager.getPermission(player).equals("ADMIN") || permissionManager.getPermission(player).equals("MOD")) {
                    if (inBuild.contains(player.getUniqueId())) {
                        inBuild.remove(player.getUniqueId());
                        player.setGameMode(GameMode.SURVIVAL);
                        Msg.sendMessage(player, "§cVocê saiu do modo de construção");
                    } else{
                        if (Duel.getInDuel().containsKey(player.getUniqueId()) || Duel.getInDuel().containsValue(player.getUniqueId())) {
                            for(Map.Entry<UUID,UUID> scan : Duel.getInDuel().entrySet()) {
                                if (player.getUniqueId().equals(scan.getKey())) {
                                    Msg.sendMessage(player, "§cVocê está em duel");
                                } else if(player.getUniqueId().equals(scan.getValue())){
                                    Msg.sendMessage(player, "§cVocê está em duel");
                                }
                            }
                            return false;
                        } else {
                            inBuild.add(player.getUniqueId());
                            player.setGameMode(GameMode.CREATIVE);
                            Msg.sendMessage(player, "§aVocê entrou no modo de construção.");
                        }
                    }

                }
            }
        }
        return false;
    }
}
