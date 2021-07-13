package br.com.main.commands;

import br.com.main.apis.Msg;
import br.com.main.entity.Duel;
import br.com.main.groups.manager.PermissionManager;
import com.nametagedit.plugin.NametagEdit;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class Tag implements CommandExecutor{

    @Getter
    private static final Map<UUID,String> tagEdited = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        PermissionManager permissionManager = new PermissionManager();


        if(!(sender instanceof Player)){
            Msg.consoleNoPermMessage();
        } else {
            Player player = (Player) sender;
            if(command.getName().equalsIgnoreCase("tag")){
                if(args.length == 0){
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
                } if(args.length == 1){
                    if(!(Duel.getInDuel().containsKey(player.getUniqueId()) || Duel.getInDuel().containsValue(player.getUniqueId()))){
                        String tag = args[0];
                        switch(tag){
                            case "admin":
                                if(permissionManager.getPermission(player).equals("ADMIN")){
                                    tagEdited.put(player.getUniqueId(),"admin");
                                    NametagEdit.getApi().setNametag(player, "§c§lADMIN§r§c ", "");
                                    NametagEdit.getApi().reloadNametag(player);
                                    Msg.sendMessage(player,"§aSua tag foi alterada para §c§lADMIN");
                                }else{
                                    Msg.noPermMessage(player);
                                }
                                break;
                            case "mod":
                                if(permissionManager.getPermission(player).equals("MOD") || permissionManager.getPermission(player).equals("ADMIN")){
                                    tagEdited.put(player.getUniqueId(),"mod");
                                    NametagEdit.getApi().setNametag(player, "§5§lMOD§r§5 ", "");
                                    NametagEdit.getApi().reloadNametag(player);
                                    Msg.sendMessage(player,"§aSua tag foi alterada para §5§lMOD");
                                }else{
                                    Msg.noPermMessage(player);
                                }
                                break;
                            case "trial":
                                if(permissionManager.getPermission(player).equals("TRIAL") || permissionManager.getPermission(player).equals("MOD") || permissionManager.getPermission(player).equals("ADMIN")){
                                    tagEdited.put(player.getUniqueId(),"trial");
                                    NametagEdit.getApi().setNametag(player, "§d§lTRIAL§r§d ", "");
                                    NametagEdit.getApi().reloadNametag(player);
                                    Msg.sendMessage(player,"§aSua tag foi alterada para §d§lTRIAL");
                                }else{
                                    Msg.noPermMessage(player);
                                }
                                break;
                            case "helper":
                                if(permissionManager.getPermission(player).equals("HELPER") || permissionManager.getPermission(player).equals("TRIAL") || permissionManager.getPermission(player).equals("MOD") ||
                                        permissionManager.getPermission(player).equals("ADMIN")){
                                    tagEdited.put(player.getUniqueId(),"helper");
                                    NametagEdit.getApi().setNametag(player, "§2§lHELPER§r§2 ", "");
                                    NametagEdit.getApi().reloadNametag(player);
                                    Msg.sendMessage(player,"§aSua tag foi alterada para §2§lHELPER");
                                }else{
                                    Msg.noPermMessage(player);
                                }
                                break;
                            case "youtuber_plus":
                                if(permissionManager.getPermission(player).equals("YOUTUBER_PLUS") || permissionManager.getPermission(player).equals("HELPER")
                                        || permissionManager.getPermission(player).equals("TRIAL") || permissionManager.getPermission(player).equals("MOD") || permissionManager.getPermission(player).equals("ADMIN")){
                                    tagEdited.put(player.getUniqueId(),"youtuberplus");
                                    NametagEdit.getApi().setNametag(player, "§3§lYT+§r§3 ", "");
                                    NametagEdit.getApi().reloadNametag(player);
                                    Msg.sendMessage(player,"§aSua tag foi alterada para §3§lYT+");
                                }else{
                                    Msg.noPermMessage(player);
                                }

                                break;
                            case "youtuber":
                                if(permissionManager.getPermission(player).equals("YOUTUBER") || permissionManager.getPermission(player).equals("YOUTUBER_PLUS") || permissionManager.getPermission(player).equals("HELPER")
                                        || permissionManager.getPermission(player).equals("TRIAL") || permissionManager.getPermission(player).equals("MOD") || permissionManager.getPermission(player).equals("ADMIN")){
                                    tagEdited.put(player.getUniqueId(),"youtuber");
                                    NametagEdit.getApi().setNametag(player, "§b§lYT§r§b ", "");
                                    NametagEdit.getApi().reloadNametag(player);
                                    Msg.sendMessage(player,"§aSua tag foi alterada para §b§lYT");
                                }else{
                                    Msg.noPermMessage(player);
                                }
                                break;
                            case "vip":
                                if(permissionManager.getPermission(player).equals("VIP") || permissionManager.getPermission(player).equals("YOUTUBER") || permissionManager.getPermission(player).equals("YOUTUBER_PLUS") || permissionManager.getPermission(player).equals("HELPER")
                                        || permissionManager.getPermission(player).equals("TRIAL") || permissionManager.getPermission(player).equals("MOD") || permissionManager.getPermission(player).equals("ADMIN")){
                                    tagEdited.put(player.getUniqueId(),"vip");
                                    NametagEdit.getApi().setNametag(player, "§d§lSATURN§r§d ", "");
                                    NametagEdit.getApi().reloadNametag(player);
                                    Msg.sendMessage(player,"§aSua tag foi alterada para §d§lSATURN");
                                }else{
                                    Msg.noPermMessage(player);
                                }
                                break;
                            case "reset":
                                tagEdited.remove(player.getUniqueId());
                                NametagEdit.getApi().setNametag(player, "§7§lMEMBRO§r§7 ", "");
                                NametagEdit.getApi().reloadNametag(player);
                                Msg.sendMessage(player,"§aSua tag foi alterada para §7§lMEMBRO");
                                break;
                        }
                    } else{
                        Msg.sendMessage(player,"§cVocê não pode trocar de tag enquanto estiver em duelo.");
                    }
                }
            }
        }
        return false;
    }
}
