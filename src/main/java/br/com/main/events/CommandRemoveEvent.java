package br.com.main.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandRemoveEvent implements Listener {
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event){
        Player player = event.getPlayer();
        if(event.getMessage().toLowerCase().startsWith("/plugins")){
            event.setMessage("§cPlugin desenvolvido por : Jhonata Augusto (KinoxPvP)");
        }
        if(event.getMessage().toLowerCase().startsWith("/version")){
            event.setMessage("§cPlugin desenvolvido por : Jhonata Augusto (KinoxPvP)");
        }
        if(event.getMessage().toLowerCase().startsWith("/minecraft:tell")){
            event.setMessage("§cPlugin desenvolvido por : Jhonata Augusto (KinoxPvP)");
        }
    }

}
