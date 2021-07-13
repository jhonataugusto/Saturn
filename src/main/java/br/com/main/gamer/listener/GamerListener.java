package br.com.main.gamer.listener;

import br.com.main.Saturn;
import br.com.main.gamer.Gamer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GamerListener implements Listener {

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        if (player == null) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "§cNão foi possível carregar sua conta, tente entrar novamente.");
            return;
        }
        Saturn.getInstance().getGamerManager().loadGamer(new Gamer(player.getUniqueId()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Saturn.getInstance().getGamerManager().unloadGamer(event.getPlayer().getUniqueId());
    }
}
