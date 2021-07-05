package br.com.main.entity;

import br.com.main.apis.ItemCreator;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Lobby {
    @Getter
    @Setter
    private Player player;

    @Getter
    private final Location lobbyLocation = new Location(Bukkit.getWorld("world"),-5.406,63,-45.463,-90,0);

    public Lobby(Player player) {
        this.player = player;
    }

    public Lobby(){}


    public void setItems(Player player) {
        player.getInventory().clear();
        ItemCreator sword = new ItemCreator(Material.DIAMOND_SWORD).setName("§cUnranked Nodebuff").setDescription("§c Duele seus inimigos na \n §c unranked nodebuff!");

        player.getInventory().setItem(0, sword.getStack());
    }

    public void teleportToLobby(Player player){
        player.teleport(lobbyLocation);
    }

}
