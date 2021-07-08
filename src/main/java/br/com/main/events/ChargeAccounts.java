package br.com.main.events;

import br.com.main.commands.manager.AccountManager;
import br.com.main.groups.manager.PermissionManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ChargeAccounts implements Listener {

    AccountManager accountManager = new AccountManager();
    PermissionManager permissionManager = new PermissionManager();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        if(!accountManager.hasAccount(player)){
            accountManager.createAccount(player);
            permissionManager.checkPermissionTag(player,event);
        } else{
            permissionManager.checkPermissionTag(player,event);
        }
    }
}
