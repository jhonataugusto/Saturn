package br.com.main.entity;

import br.com.main.Saturn;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Duel implements Listener {


    @Getter
    @Setter
    private Map<UUID, Integer> listOfScheduler = new HashMap<>();
    @Getter
    @Setter
    private List<UUID> inQueue = new ArrayList<>();
    @Getter
    @Setter
    private Map<UUID, UUID> inDuel = new HashMap<>();
    @Getter
    @Setter
    private Lobby lobby = new Lobby();

    public Duel() {

    }

    @EventHandler
    public void clickSwordDiamond(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        if (action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR) {
            if (player.getInventory().getItemInHand().getType().equals(Material.DIAMOND_SWORD)) {
                if (Bukkit.getWorld("world").equals(player.getWorld())) {
                    if (!inQueue.contains(player.getUniqueId())) {
                        player.sendMessage("§aVocê entrou na queue.");
                        inQueue.add(player.getUniqueId());
                        player.sendMessage("" + inQueue.size());
                        queue(player);
                    } else if (inQueue.contains(player.getUniqueId())) {
                        player.sendMessage("§cVocê saiu da queue.");
                        inQueue.remove(player.getUniqueId());
                        player.sendMessage("" + inQueue.size());
                        cancelQueue(player);
                    }
                }
            }
        }
    }

    public void queue(Player player) {
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                player.sendMessage("§3Aguarde, você está na fila...");
                if (inQueue.size() == 2) {


                    Player player1 = Bukkit.getServer().getPlayer(inQueue.get(0));
                    Player player2 = Bukkit.getServer().getPlayer(inQueue.get(1));

                    player1.getInventory().clear();
                    player2.getInventory().clear();

                    matchStatus(player1, player2);
                    inDuel.put(player1.getUniqueId(), player2.getUniqueId());

                    player1.teleport(Bukkit.getServer().getWorld("arena1").getSpawnLocation());
                    player2.teleport(Bukkit.getServer().getWorld("arena1").getSpawnLocation());

                    player1.sendMessage("player 1: " + player1.getName() + " " + inDuel.keySet() + inQueue.size());
                    player2.sendMessage("player 2: " + player2.getName() + " " + inDuel.keySet() + inQueue.size());


                    if (!inQueue.isEmpty()) {
                        inQueue.clear();
                    }

                    setInvisible(player1, player2);
                    cancelQueue(player1);
                    cancelQueue(player2);
                    listOfScheduler.clear();

                }

            }
        };
        runnable.runTaskTimer(Saturn.getInstance(), 0, 20);
        listOfScheduler.put(player.getUniqueId(), runnable.getTaskId());
    }

    public void matchStatus(Player player1, Player player2) {
        player1.sendMessage("§c==============================");
        player1.sendMessage("       MATCH ENCONTRADA     ");
        player1.sendMessage("§3 Você vai lutar contra : §l§b" + player2.getName());
        player1.sendMessage("§c==============================");

        player2.sendMessage("§c==============================");
        player2.sendMessage("       MATCH ENCONTRADA     ");
        player2.sendMessage("§3 Você vai lutar contra : §l§b" + player1.getName());
        player2.sendMessage("§c==============================");
    }

    public void cancelQueue(Player player) {
        for (Map.Entry<UUID, Integer> task : listOfScheduler.entrySet()) {
            if (player.getUniqueId().equals(task.getKey())) {
                Bukkit.getServer().getScheduler().cancelTask(task.getValue());
            }
        }
    }

    public void setInvisible(Player player1, Player player2) {

        for (Map.Entry<UUID, UUID> inDuel : inDuel.entrySet()) {
            if (player1.getUniqueId().equals(inDuel.getKey()) || player1.getUniqueId().equals(inDuel.getValue())) {

                player1 = Bukkit.getPlayer(inDuel.getKey());
                player2 = Bukkit.getPlayer(inDuel.getValue());

                for (Player all : Bukkit.getOnlinePlayers()) {
                    if (player1.canSee(all)) {
                        player1.hidePlayer(all);
                    }
                    if (player2.canSee(all)) {
                        player2.hidePlayer(all);
                    }
                }
                player1.showPlayer(player2);
                player2.showPlayer(player1);
            }
        }


    }


    @EventHandler
    public void hitArenaEvent(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player player1 = (Player) event.getEntity();
            Player player2 = (Player) event.getDamager();

            if (player1.getWorld().getName().equals("arena1") && player2.getWorld().getName().equals("arena1")) {
                if (player1.getHealth() <= event.getFinalDamage()) {
                    player1.setHealth(20);

                    lobby.teleportToLobby(player1);
                    lobby.setItems(player1);

                    lobby.teleportToLobby(player2);
                    lobby.setItems(player2);

                    for (Player all : Bukkit.getOnlinePlayers()) {
                        if (!player1.canSee(all)) {
                            player1.showPlayer(all);
                        }
                        if (!player2.canSee(all)) {
                            player2.showPlayer(all);
                        }
                    }

                    for (Map.Entry<UUID, UUID> duel : inDuel.entrySet()) {
                        if (duel.getValue().equals(player2.getUniqueId()) || duel.getKey().equals(player2.getUniqueId())) {
                            inDuel.remove(duel.getKey());
                        }
                    }

                    player1.sendMessage("§c" + player2.getName() + "§3 matou você com : §c" + player2.getHealth() + " §3corações.");
                    player2.sendMessage("§3 Você matou §c" + player1.getName() + "§3 com : §c" + player2.getHealth() + " §3corações");

                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        for (Map.Entry<UUID, UUID> duel : inDuel.entrySet()) {
            if (player.getUniqueId().equals(duel.getValue())) {
                Player player1 = Bukkit.getPlayer(duel.getKey());
                lobby.teleportToLobby(player1);
            }
        }
        inDuel.remove(player.getUniqueId());
    }
}
