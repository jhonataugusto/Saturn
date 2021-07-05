package br.com.main.events;

import br.com.main.entity.Lobby;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;

public class FirstJoin implements Listener {

    Lobby lobby = new Lobby();


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        lobby.teleportToLobby(player);
        lobby.setItems(player);
        player.setFoodLevel(20);
        player.setHealth(20);
        player.setExp(0);
        player.setPlayerWeather(WeatherType.CLEAR);
        player.setPlayerTime(0,true);
        player.setFallDistance(500);

    }

    @EventHandler
    public void onHitPlayer(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player entity = (Player) event.getEntity();
            if (entity.getWorld().getName().equals("world")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (player.getLocation().equals(lobby.getLobbyLocation())) {
            lobby.setItems(player);
        }
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        ItemStack itemDropped = event.getItemDrop().getItemStack();
        boolean hasSameWorld = player.getWorld().equals(Bukkit.getWorld("world"));
        if (hasSameWorld) {
            if (itemDropped.getType().equals(Material.DIAMOND_SWORD)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerCatchItemDropped(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        boolean hasSameworld = player.getWorld().equals(Bukkit.getWorld("world"));

        if (hasSameworld) {
            event.setCancelled(false);
        }
    }

    @EventHandler
    public void breakBlockEvent(BlockBreakEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void placeBlockEvent(BlockPlaceEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void teleportPlayer(PlayerTeleportEvent event) {
        Player player = event.getPlayer();

    }

    @EventHandler
    public void playerMoveEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        int locationBlockY = player.getLocation().getBlockY();
        if (locationBlockY <= 0) {
            player.teleport(lobby.getLobbyLocation());
        }
    }

    @EventHandler
    public void inventoryMoveEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (player.getWorld().getName().equals("world")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        if(event.getFoodLevel() < 20){
            event.setFoodLevel(20);
        }

    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event){
        event.setCancelled(true);
    }
}
