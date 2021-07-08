package br.com.main.events;

import br.com.main.commands.Builder;
import br.com.main.entity.Duel;
import br.com.main.entity.Lobby;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player player = event.getPlayer();
        Duel.resetPlayer(player);
        player.setGameMode(GameMode.SURVIVAL);
        lobby.teleportToLobby(player);
        lobby.setItems(player);
        player.setFoodLevel(20);
        player.setHealth(20);
        player.setExp(0);
        player.setPlayerWeather(WeatherType.CLEAR);
        player.setPlayerTime(0, true);
        player.setFallDistance(500);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        double locationBlockY = player.getLocation().getBlockY();
        if (locationBlockY < -100) {
            lobby.teleportToLobby(player);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onHitPlayer(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player entity = (Player) event.getEntity();
            if (entity.getWorld().getName().equals("world")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
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

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerCatchItemDropped(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        boolean hasSameworld = player.getWorld().equals(Bukkit.getWorld("world"));

        if (hasSameworld && !Builder.getInBuild().contains(player.getUniqueId())) {
            event.setCancelled(false);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void breakBlockEvent(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if(!Builder.getInBuild().contains(player.getUniqueId())){
            if (player.getWorld().getName().equals("world")) {
                event.setCancelled(true);
                return;
            }
            if(player.getWorld().getName().equals("arena1")){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void placeBlockEvent(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if(!Builder.getInBuild().contains(player.getUniqueId())){
            if (player.getWorld().getName().equals("world")) {
                event.setCancelled(true);
                return;
            }
            if(player.getWorld().getName().equals("arena1")){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void inventoryMoveEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (player.getWorld().getName().equals("world") && !Builder.getInBuild().contains(player.getUniqueId())) {
            event.setCancelled(true);
        }

    }

    @EventHandler(priority = EventPriority.LOW)
    public void onFoodChange(FoodLevelChangeEvent event) {
        Player player = (Player) event.getEntity();

        if (player.getWorld().getName().equals("world")) {
            if (event.getFoodLevel() < 20) {
                event.setFoodLevel(20);
            }
        }


    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onWeatherChange(WeatherChangeEvent event) {
        event.setCancelled(true);
    }
}
