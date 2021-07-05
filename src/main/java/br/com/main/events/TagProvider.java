package br.com.main.events;

import br.com.main.groups.manager.PermissionManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class TagProvider implements Listener {

    PermissionManager permissionManager = new PermissionManager();

    @EventHandler(priority = EventPriority.NORMAL)
    public void chatEvent(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        permissionManager.checkPermissionChat(player,event);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void chatEvent(PlayerJoinEvent event){
        Player player = event.getPlayer();
        permissionManager.checkPermissionTag(player,event);
    }
}
