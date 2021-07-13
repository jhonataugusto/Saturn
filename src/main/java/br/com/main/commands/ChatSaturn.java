package br.com.main.commands;

import br.com.main.apis.Msg;
import br.com.main.groups.manager.PermissionManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChatSaturn implements CommandExecutor {
    @Getter
    private static final List<UUID> chatLocked = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            Msg.consoleNoPermMessage();
        } else {
            Player player = (Player) sender;
            PermissionManager permissionManager = new PermissionManager();
            if (permissionManager.getPermission(player).equals("ADMIN") || permissionManager.getPermission(player).equals("MOD")) {
                if (command.getName().equalsIgnoreCase("chat")) {
                    if (args.length == 1) {
                        if (args[0].equalsIgnoreCase("clear")) {
                            for (Player all : Bukkit.getOnlinePlayers()) {
                                all.sendMessage("\n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n ");
                                all.sendMessage("§aO chat foi limpo por : §c" + player.getName());
                            }
                        } else if (args[0].equalsIgnoreCase("lock")) {
                            for (Player all : Bukkit.getOnlinePlayers()) {
                                if (permissionManager.getPermission(all).equals("MEMBER") || permissionManager.getPermission(all).equals("VIP") || permissionManager.getPermission(all).equals("YOUTUBER") || permissionManager.getPermission(all).equals("YOUTUBER_PLUS")) {
                                    chatLocked.add(all.getUniqueId());
                                    all.sendMessage("§O chat foi bloqueado para todos.");
                                }
                            }
                        } else if (args[0].equalsIgnoreCase("removelock")) {
                            chatLocked.clear();
                            for (Player all : Bukkit.getOnlinePlayers()) {
                                all.sendMessage("§eO chat foi liberado!");
                            }
                        }
                    }
                }
            } else {
                Msg.noPermMessage(player);
            }
        }
        return false;
    }
}
