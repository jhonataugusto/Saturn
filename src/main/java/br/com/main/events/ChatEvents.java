package br.com.main.events;

import br.com.main.commands.ChatSaturn;
import br.com.main.commands.Tag;
import br.com.main.groups.manager.PermissionManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvents implements Listener {

    PermissionManager permissionManager = new PermissionManager();

    @EventHandler(priority = EventPriority.NORMAL)
    public void chatEvent(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();

        if(ChatSaturn.getChatLocked().contains(player.getUniqueId())){
            event.setCancelled(true);
            player.sendMessage("§cO chat está desativado para todos.");
            return;
        }
        if(Tag.getTagEdited().containsKey(player.getUniqueId())){
            switch (Tag.getTagEdited().get(player.getUniqueId())) {
                case "admin":
                    event.setFormat("§c§lADMIN§r§c " + player.getName() + " §8››§r " + event.getMessage());
                    break;
                case "mod":
                    event.setFormat("§5§lMOD§r§5 " + player.getName() + " §8››§r " + event.getMessage());
                    break;
                case "trial":
                    event.setFormat("§d§lTRIAL§r§d " + player.getName() + " §8››§r " + event.getMessage());
                    break;
                case "helper":
                    event.setFormat("§2§lHELPER§r§2 " + player.getName() + " §8››§r " + event.getMessage());
                    break;
                case "youtuberplus":
                    event.setFormat("§3§lYT+§r§3 " + player.getName() + " §8››§r " + event.getMessage());
                    break;
                case "youtuber":
                    event.setFormat("§b§lYT§r§b " + player.getName() + " §8››§r " + event.getMessage());
                    break;
                case "vip":
                    event.setFormat("§d§lSATURN§r§d " + player.getName() + " §8››§r  " + event.getMessage());
                    break;
            }
        } else{
            permissionManager.checkPermissionChat(player,event);
        }

    }
}
