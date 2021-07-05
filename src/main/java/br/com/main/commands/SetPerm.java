package br.com.main.commands;

import br.com.main.apis.Msg;
import br.com.main.groups.Permission;
import br.com.main.groups.manager.PermissionManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetPerm implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            Msg.consoleNoPermMessage();
        } else {
            Player player = (Player) sender;
            PermissionManager permissionManager = new PermissionManager();
            if (command.getName().equals("setperm")) {
                    if (!permissionManager.getPermission(player).equals("ADMIN")) {
                        Msg.noPermMessage(player);
                    } else {
                        if (args.length == 0) {
                            player.sendMessage("§cUtilize: /setperm <Jogador> <Cargo>");
                        }
                        else if (args.length == 1) {
                            player.sendMessage("§cUtilize: /setperm <Jogador> <Cargo>");
                        } else if (args.length == 2) {
                            Player target = Bukkit.getServer().getPlayer(args[0]);
                            String permission = args[1];
                            switch (permission) {
                                case "youtuber":
                                    permissionManager.setPermission(target, Permission.YOUTUBER);
                                    permissionManager.checkPermissionTag(target);
                                    player.sendMessage("§aVocê alterou a permissão de "+target.getName()+" para YOUTUBER");
                                    target.sendMessage("§aSua permissão foi alterada para YOUTUBER");
                                    break;
                                case "youtuberplus":
                                    permissionManager.setPermission(target, Permission.YOUTUBER_PLUS);
                                    permissionManager.checkPermissionTag(target);
                                    player.sendMessage("§aVocê alterou a permissão de "+target.getName()+" para YOUTUBER+");
                                    target.sendMessage("§aSua permissão foi alterada para YOUTUBER+");
                                    break;
                                case "helper":
                                    permissionManager.setPermission(target, Permission.HELPER);
                                    permissionManager.checkPermissionTag(target);
                                    player.sendMessage("§aVocê alterou a permissão de "+target.getName()+" para HELPER");
                                    target.sendMessage("§aSua permissão foi alterada para HELPER");
                                    break;
                                case "trial":
                                    permissionManager.setPermission(target, Permission.TRIAL);
                                    permissionManager.checkPermissionTag(target);
                                    player.sendMessage("§aVocê alterou a permissão de "+target.getName()+" para TRIAL MOD");
                                    target.sendMessage("§aSua permissão foi alterada para TRIAL MOD");
                                    break;
                                case "mod":
                                    permissionManager.setPermission(target, Permission.MOD);
                                    permissionManager.checkPermissionTag(target);
                                    player.sendMessage("§aVocê alterou a permissão de "+target.getName()+" para MOD");
                                    target.sendMessage("§aSua permissão foi alterada para MOD");
                                    break;
                                case "admin":
                                    permissionManager.setPermission(target, Permission.ADMIN);
                                    permissionManager.checkPermissionTag(target);
                                    player.sendMessage("§aVocê alterou a permissão de "+target.getName()+" para ADMIN");
                                    target.sendMessage("§aSua permissão foi alterada para ADMIN");
                                    break;
                            }
                        } else {
                            Msg.sendErrorMessage(player);
                        }
                    }

            }
            if (command.getName().equals("removeperm")) {
                if(!permissionManager.getPermission(player).equals("ADMIN")){
                    Msg.noPermMessage(player);
                } else{
                    if (args.length == 0) {
                        if (!permissionManager.getPermission(player).equals("ADMIN")) {
                            Msg.noPermMessage(player);
                        } else {
                            player.sendMessage("§cUtilize: /removeperm <Jogador>");
                        }
                    } else if (args.length == 1) {
                        Player target = Bukkit.getServer().getPlayer(args[0]);
                        permissionManager.removePermission(target);
                        permissionManager.checkPermissionTag(target);
                        player.sendMessage("§aVocê removeu todas as permissões de  "+target.getName());
                        target.sendMessage("§cTodas as suas permissões foram removidas");
                    } else {
                        Msg.sendErrorMessage(player);
                    }
                }
            }
        }
        return false;
    }
}