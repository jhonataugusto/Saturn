package br.com.main.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class MotdEvent implements Listener {
    @EventHandler
    public void serverPing(ServerListPingEvent event){
        event.setMotd("     §e§lSATURN NETWORK §c  ۞ §r   1.7/1.8\n" +
                "§c§l     SERVIDOR BRASILEIRO DE POT PVP");
        event.setMaxPlayers(250);

    }
}
