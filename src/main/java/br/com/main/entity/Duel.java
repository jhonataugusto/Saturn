package br.com.main.entity;

import br.com.main.Saturn;
import br.com.main.apis.ItemCreator;
import br.com.main.apis.Msg;
import br.com.main.commands.Spec;
import br.com.main.groups.manager.PermissionManager;
import com.nametagedit.plugin.NametagEdit;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEnderPearl;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Duel implements Listener {


    @Getter
    @Setter
    private static Map<UUID, Integer> listOfScheduler = new HashMap<>();
    @Getter
    @Setter
    private static List<UUID> inQueue = new ArrayList<>();
    @Getter
    @Setter
    private static Map<UUID, UUID> inDuel = new HashMap<>();
    @Getter
    @Setter
    private Lobby lobby = new Lobby();
    @Getter
    @Setter
    private PermissionManager permissionManager = new PermissionManager();
    @Getter
    @Setter
    private static List<UUID> cooldownTimer = new ArrayList<>();
    public Duel() {

    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void clickSwordDiamond(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        if (action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR) {

            if (player.getInventory().getItemInHand().getType().equals(Material.DIAMOND_SWORD)) {

                if (Bukkit.getWorld("world").equals(player.getWorld())) {

                    if (!inQueue.contains(player.getUniqueId())) {
                        player.sendMessage("§aVocê entrou na queue.");
                        inQueue.add(player.getUniqueId());
                        queue(player);

                    } else if (inQueue.contains(player.getUniqueId())) {
                        player.sendMessage("§cVocê saiu da queue.");
                        inQueue.remove(player.getUniqueId());
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

                    Location player1Location = new Location(Bukkit.getWorld("arena1"), 436.491, 90, 103.466, 0, 0);
                    Location player2Location = new Location(Bukkit.getWorld("arena1"), 436.475, 90, 198.478, 180, -3);


                    Player player1 = Bukkit.getServer().getPlayer(inQueue.get(0));
                    Player player2 = Bukkit.getServer().getPlayer(inQueue.get(1));

                    cancelQueue(player1);
                    cancelQueue(player2);

                    NametagEdit.getApi().setNametag(player1, "§9§o", "");
                    NametagEdit.getApi().setNametag(player2, "§c§o", "");
                    NametagEdit.getApi().reloadNametag(player1);
                    NametagEdit.getApi().reloadNametag(player2);

                    player1.teleport(player1Location);
                    player2.teleport(player2Location);

                    setInvisible(player1, player2);

                    resetPlayer(player1);
                    resetPlayer(player2);

                    setItens(player1);
                    setItens(player2);

                    inDuel.put(player1.getUniqueId(), player2.getUniqueId());

                    matchStatus(player1, player2);

                    if (!inQueue.isEmpty()) {
                        inQueue.clear();
                    }
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
        if(player1.getWorld().getName().equals("arena1") && player2.getWorld().getName().equals("arena1")){
            for(Player all : Bukkit.getOnlinePlayers()){
                player1.hidePlayer(all);
                all.hidePlayer(player1);

                player2.hidePlayer(all);
                all.hidePlayer(player2);
            }
            player1.showPlayer(player2);
            player2.showPlayer(player1);
        }
    }

    public void setItens(Player player) {
        //armor
        player.getInventory().setHelmet(new ItemCreator(Material.DIAMOND_HELMET).setEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).setEnchant(Enchantment.DURABILITY, 3).getStack());
        player.getInventory().setChestplate(new ItemCreator(Material.DIAMOND_CHESTPLATE).setEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).setEnchant(Enchantment.DURABILITY, 3).getStack());
        player.getInventory().setLeggings(new ItemCreator(Material.DIAMOND_LEGGINGS).setEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).setEnchant(Enchantment.DURABILITY, 3).getStack());
        player.getInventory().setBoots(new ItemCreator(Material.DIAMOND_BOOTS).setEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).setEnchant(Enchantment.PROTECTION_FALL, 3).setEnchant(Enchantment.DURABILITY, 3).getStack());
        player.getInventory().setItem(0, new ItemCreator(Material.DIAMOND_SWORD).setEnchant(Enchantment.DAMAGE_ALL, 2).setEnchant(Enchantment.DURABILITY, 3).setEnchant(Enchantment.FIRE_ASPECT, 2).getStack());

        player.getInventory().setItem(1, new ItemCreator(Material.ENDER_PEARL).setAmount(16).getStack());
        for (int i = 2; i <= 35; i++) {

            if (i == 6) {
                player.getInventory().setItem(i, new ItemStack(Material.POTION, 1, (short) 8259));
            } else if (i == 7) {
                player.getInventory().setItem(i, new ItemStack(Material.POTION, 1, (short) 8226));
            } else if (i == 17) {
                player.getInventory().setItem(i, new ItemStack(Material.POTION, 1, (short) 8226));
            } else if (i == 26) {
                player.getInventory().setItem(i, new ItemStack(Material.POTION, 1, (short) 8226));
            } else if (i == 35) {
                player.getInventory().setItem(i, new ItemStack(Material.POTION, 1, (short) 8226));
            } else {
                player.getInventory().setItem(i, new ItemStack(Material.POTION, 1, (short) 16421));
            }
        }
        player.getInventory().setItem(8, new ItemCreator(Material.COOKED_BEEF).setAmount(64).getStack());
    }

    public static void resetPlayer(Player player) {
        if(player == null){
            return;
        }
        player.getInventory().setArmorContents(null);
        player.getInventory().clear();
        player.setFoodLevel(20);
        player.setHealth(20);
        player.setSaturation(12);
        player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
        player.removePotionEffect(PotionEffectType.SPEED);
        player.setFireTicks(0);
        player.setFlying(false);
        player.setLevel(0);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void finalDuel(EntityDamageByEntityEvent event) {

        if(!(event.getEntity() instanceof CraftEnderPearl)){
                if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) && event.getEntity() instanceof Player) {
                    Player deather = (Player) event.getEntity();
                    Player damager = (Player) event.getDamager();
                    if(event.getFinalDamage() >= deather.getHealth()){
                        event.setCancelled(true);
                        deather.setGameMode(GameMode.SPECTATOR);
                        deather.setFlying(true);
                        deather.getWorld().strikeLightningEffect(deather.getLocation());
                        resetPlayer(deather);
                        resetPlayer(damager);

                        Msg.sendMessage(deather,"§c"+deather.getName()+"§e foi morto por §c"+damager.getName());
                        Msg.sendMessage(damager,"§c"+deather.getName()+"§e foi morto por §c"+damager.getName());

                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Saturn.getInstance(), new BukkitRunnable() {
                            @Override
                            public void run() {
                                deather.setGameMode(GameMode.SURVIVAL);
                                damager.setGameMode(GameMode.SURVIVAL);

                                inDuel.remove(deather.getUniqueId());
                                inDuel.remove(damager.getUniqueId());

                                lobby.setItems(deather);
                                lobby.setItems(damager);

                                cancelQueue(deather);
                                cooldownTimer.remove(deather.getUniqueId());
                                deather.setLevel(0);

                                cancelQueue(damager);
                                cooldownTimer.remove(damager.getUniqueId());
                                damager.setLevel(0);

                                for(Player all : Bukkit.getOnlinePlayers()){
                                    deather.showPlayer(all);
                                    all.showPlayer(deather);

                                    damager.showPlayer(all);
                                    all.showPlayer(damager);
                                }

                                permissionManager.checkPermissionTag(damager);
                                permissionManager.checkPermissionTag(deather);

                                //tirar os spectadores da sala e fazer eles verem todos
                                for(Map.Entry<UUID,UUID> scan : Spec.getSpecAndBattler().entrySet()){
                                    if(damager.getUniqueId().equals(scan.getValue())){
                                        Player spectator = Bukkit.getServer().getPlayer(scan.getKey());
                                        resetPlayer(spectator);
                                        lobby.teleportToLobby(spectator);
                                        lobby.setItems(spectator);
                                        spectator.setGameMode(GameMode.SURVIVAL);
                                        Spec.getSpecAndBattler().remove(damager.getUniqueId(),spectator.getUniqueId());
                                        Spec.getInSpec().remove(spectator.getUniqueId());

                                        for(Player all : Bukkit.getOnlinePlayers()){
                                            spectator.showPlayer(all);
                                            all.showPlayer(spectator);
                                        }

                                    }
                                    if(deather.getUniqueId().equals(scan.getValue())){
                                        Player spectator = Bukkit.getServer().getPlayer(scan.getKey());
                                        resetPlayer(spectator);
                                        lobby.teleportToLobby(spectator);
                                        lobby.setItems(spectator);
                                        spectator.setGameMode(GameMode.SURVIVAL);
                                        Spec.getSpecAndBattler().remove(deather.getUniqueId(),spectator.getUniqueId());
                                        Spec.getInSpec().remove(spectator.getUniqueId());

                                        for(Player all : Bukkit.getOnlinePlayers()){
                                            spectator.showPlayer(all);
                                            all.showPlayer(spectator);
                                        }
                                    }
                                }
                                lobby.teleportToLobby(damager);
                                lobby.teleportToLobby(deather);
                            }
                        },20*4);
                    }
                }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void FallDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player){
            Player deather = (Player) event.getEntity();
            if(event.getCause().equals(EntityDamageEvent.DamageCause.FALL) || event.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)){
                for(Map.Entry<UUID,UUID> scan : inDuel.entrySet()){
                    if(deather.getUniqueId().equals(scan.getKey())){
                        Player adversary = Bukkit.getServer().getPlayer(scan.getValue());

                        if(event.getFinalDamage() >= deather.getHealth()){
                            event.setCancelled(true);
                            deather.setGameMode(GameMode.SPECTATOR);
                            deather.setFlying(true);
                            deather.getWorld().strikeLightningEffect(deather.getLocation());
                            resetPlayer(deather);
                            resetPlayer(adversary);

                            Msg.sendMessage(deather,"§c"+deather.getName()+"§e foi morto por §c"+adversary.getName());
                            Msg.sendMessage(adversary,"§c"+deather.getName()+"§e foi morto por §c"+adversary.getName());

                            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Saturn.getInstance(), new BukkitRunnable() {
                                @Override
                                public void run() {
                                    deather.setGameMode(GameMode.SURVIVAL);
                                    adversary.setGameMode(GameMode.SURVIVAL);

                                    inDuel.remove(deather.getUniqueId());
                                    inDuel.remove(adversary.getUniqueId());

                                    lobby.setItems(deather);
                                    lobby.setItems(adversary);

                                    cancelQueue(deather);
                                    cooldownTimer.remove(deather.getUniqueId());
                                    deather.setLevel(0);

                                    cancelQueue(adversary);
                                    cooldownTimer.remove(adversary.getUniqueId());
                                    adversary.setLevel(0);

                                    for(Player all : Bukkit.getOnlinePlayers()){
                                        deather.showPlayer(all);
                                        all.showPlayer(deather);

                                        adversary.showPlayer(all);
                                        all.showPlayer(adversary);
                                    }

                                    permissionManager.checkPermissionTag(adversary);
                                    permissionManager.checkPermissionTag(deather);

                                    //tirar os spectadores da sala e fazer eles verem todos
                                    for(Map.Entry<UUID,UUID> scan : Spec.getSpecAndBattler().entrySet()){
                                        if(adversary.getUniqueId().equals(scan.getValue())){
                                            Player spectator = Bukkit.getServer().getPlayer(scan.getKey());
                                            resetPlayer(spectator);
                                            lobby.teleportToLobby(spectator);
                                            lobby.setItems(spectator);
                                            spectator.setGameMode(GameMode.SURVIVAL);
                                            Spec.getSpecAndBattler().remove(adversary.getUniqueId(),spectator.getUniqueId());
                                            Spec.getInSpec().remove(spectator.getUniqueId());

                                            for(Player all : Bukkit.getOnlinePlayers()){
                                                spectator.showPlayer(all);
                                                all.showPlayer(spectator);
                                            }

                                        }
                                        if(deather.getUniqueId().equals(scan.getValue())){
                                            Player spectator = Bukkit.getServer().getPlayer(scan.getKey());
                                            resetPlayer(spectator);
                                            lobby.teleportToLobby(spectator);
                                            lobby.setItems(spectator);
                                            spectator.setGameMode(GameMode.SURVIVAL);
                                            Spec.getSpecAndBattler().remove(deather.getUniqueId(),spectator.getUniqueId());
                                            Spec.getInSpec().remove(spectator.getUniqueId());

                                            for(Player all : Bukkit.getOnlinePlayers()){
                                                spectator.showPlayer(all);
                                                all.showPlayer(spectator);
                                            }
                                        }
                                    }
                                    lobby.teleportToLobby(adversary);
                                    lobby.teleportToLobby(deather);
                                }
                            },20*4);
                        }

                    } else if(deather.getUniqueId().equals(scan.getValue())){
                        Player adversary = Bukkit.getServer().getPlayer(scan.getKey());

                        if(event.getFinalDamage() >= deather.getHealth()){
                            event.setCancelled(true);
                            deather.setGameMode(GameMode.SPECTATOR);
                            deather.setFlying(true);
                            deather.getWorld().strikeLightningEffect(deather.getLocation());
                            resetPlayer(deather);
                            resetPlayer(adversary);

                            Msg.sendMessage(deather,"§c"+deather.getName()+"§e foi morto por §c"+adversary.getName());
                            Msg.sendMessage(adversary,"§c"+deather.getName()+"§e foi morto por §c"+adversary.getName());

                            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Saturn.getInstance(), new BukkitRunnable() {
                                @Override
                                public void run() {
                                    deather.setGameMode(GameMode.SURVIVAL);
                                    adversary.setGameMode(GameMode.SURVIVAL);

                                    inDuel.remove(deather.getUniqueId());
                                    inDuel.remove(adversary.getUniqueId());

                                    lobby.setItems(deather);
                                    lobby.setItems(adversary);

                                    cancelQueue(deather);
                                    cooldownTimer.remove(deather.getUniqueId());
                                    deather.setLevel(0);

                                    cancelQueue(adversary);
                                    cooldownTimer.remove(adversary.getUniqueId());
                                    adversary.setLevel(0);

                                    for(Player all : Bukkit.getOnlinePlayers()){
                                        deather.showPlayer(all);
                                        all.showPlayer(deather);

                                        adversary.showPlayer(all);
                                        all.showPlayer(adversary);
                                    }

                                    permissionManager.checkPermissionTag(adversary);
                                    permissionManager.checkPermissionTag(deather);

                                    //tirar os spectadores da sala e fazer eles verem todos
                                    for(Map.Entry<UUID,UUID> scan : Spec.getSpecAndBattler().entrySet()){
                                        if(adversary.getUniqueId().equals(scan.getValue())){
                                            Player spectator = Bukkit.getServer().getPlayer(scan.getKey());
                                            resetPlayer(spectator);
                                            lobby.teleportToLobby(spectator);
                                            lobby.setItems(spectator);
                                            spectator.setGameMode(GameMode.SURVIVAL);
                                            Spec.getSpecAndBattler().remove(adversary.getUniqueId(),spectator.getUniqueId());
                                            Spec.getInSpec().remove(spectator.getUniqueId());

                                            for(Player all : Bukkit.getOnlinePlayers()){
                                                spectator.showPlayer(all);
                                                all.showPlayer(spectator);
                                            }

                                        }
                                        if(deather.getUniqueId().equals(scan.getValue())){
                                            Player spectator = Bukkit.getServer().getPlayer(scan.getKey());
                                            resetPlayer(spectator);
                                            lobby.teleportToLobby(spectator);
                                            lobby.setItems(spectator);
                                            spectator.setGameMode(GameMode.SURVIVAL);
                                            Spec.getSpecAndBattler().remove(deather.getUniqueId(),spectator.getUniqueId());
                                            Spec.getInSpec().remove(spectator.getUniqueId());

                                            for(Player all : Bukkit.getOnlinePlayers()){
                                                spectator.showPlayer(all);
                                                all.showPlayer(spectator);
                                            }
                                        }
                                    }
                                    lobby.teleportToLobby(adversary);
                                    lobby.teleportToLobby(deather);
                                }
                            },20*4);
                        }
                    }
                }
            } else if(event.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)){
                for(Map.Entry<UUID,UUID> scan : inDuel.entrySet()){
                    if(deather.getUniqueId().equals(scan.getKey())){
                        Player adversary = Bukkit.getServer().getPlayer(scan.getValue());

                        if(event.getFinalDamage() >= deather.getHealth()){
                            event.setCancelled(true);
                            deather.setGameMode(GameMode.SPECTATOR);
                            deather.setFlying(true);
                            deather.getWorld().strikeLightningEffect(deather.getLocation());
                            resetPlayer(deather);
                            resetPlayer(adversary);

                            Msg.sendMessage(deather,"§c"+deather.getName()+"§e foi morto por §c"+adversary.getName());
                            Msg.sendMessage(adversary,"§c"+deather.getName()+"§e foi morto por §c"+adversary.getName());

                            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Saturn.getInstance(), new BukkitRunnable() {
                                @Override
                                public void run() {
                                    deather.setGameMode(GameMode.SURVIVAL);
                                    adversary.setGameMode(GameMode.SURVIVAL);

                                    inDuel.remove(deather.getUniqueId());
                                    inDuel.remove(adversary.getUniqueId());

                                    lobby.setItems(deather);
                                    lobby.setItems(adversary);

                                    cancelQueue(deather);
                                    cooldownTimer.remove(deather.getUniqueId());
                                    deather.setLevel(0);

                                    cancelQueue(adversary);
                                    cooldownTimer.remove(adversary.getUniqueId());
                                    adversary.setLevel(0);

                                    for(Player all : Bukkit.getOnlinePlayers()){
                                        deather.showPlayer(all);
                                        all.showPlayer(deather);

                                        adversary.showPlayer(all);
                                        all.showPlayer(adversary);
                                    }

                                    permissionManager.checkPermissionTag(adversary);
                                    permissionManager.checkPermissionTag(deather);

                                    //tirar os spectadores da sala e fazer eles verem todos
                                    for(Map.Entry<UUID,UUID> scan : Spec.getSpecAndBattler().entrySet()){
                                        if(adversary.getUniqueId().equals(scan.getValue())){
                                            Player spectator = Bukkit.getServer().getPlayer(scan.getKey());
                                            resetPlayer(spectator);
                                            lobby.teleportToLobby(spectator);
                                            lobby.setItems(spectator);
                                            spectator.setGameMode(GameMode.SURVIVAL);
                                            Spec.getSpecAndBattler().remove(adversary.getUniqueId(),spectator.getUniqueId());
                                            Spec.getInSpec().remove(spectator.getUniqueId());

                                            for(Player all : Bukkit.getOnlinePlayers()){
                                                spectator.showPlayer(all);
                                                all.showPlayer(spectator);
                                            }

                                        }
                                        if(deather.getUniqueId().equals(scan.getValue())){
                                            Player spectator = Bukkit.getServer().getPlayer(scan.getKey());
                                            resetPlayer(spectator);
                                            lobby.teleportToLobby(spectator);
                                            lobby.setItems(spectator);
                                            spectator.setGameMode(GameMode.SURVIVAL);
                                            Spec.getSpecAndBattler().remove(deather.getUniqueId(),spectator.getUniqueId());
                                            Spec.getInSpec().remove(spectator.getUniqueId());

                                            for(Player all : Bukkit.getOnlinePlayers()){
                                                spectator.showPlayer(all);
                                                all.showPlayer(spectator);
                                            }
                                        }
                                    }
                                    lobby.teleportToLobby(adversary);
                                    lobby.teleportToLobby(deather);
                                }
                            },20*4);
                        }

                    } else if(deather.getUniqueId().equals(scan.getValue())){
                        Player adversary = Bukkit.getServer().getPlayer(scan.getKey());

                        if(event.getFinalDamage() >= deather.getHealth()){
                            event.setCancelled(true);
                            deather.setGameMode(GameMode.SPECTATOR);
                            deather.setFlying(true);
                            deather.getWorld().strikeLightningEffect(deather.getLocation());
                            resetPlayer(deather);
                            resetPlayer(adversary);

                            Msg.sendMessage(deather,"§c"+deather.getName()+"§e foi morto por §c"+adversary.getName());
                            Msg.sendMessage(adversary,"§c"+deather.getName()+"§e foi morto por §c"+adversary.getName());

                            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Saturn.getInstance(), new BukkitRunnable() {
                                @Override
                                public void run() {
                                    deather.setGameMode(GameMode.SURVIVAL);
                                    adversary.setGameMode(GameMode.SURVIVAL);

                                    inDuel.remove(deather.getUniqueId());
                                    inDuel.remove(adversary.getUniqueId());

                                    lobby.setItems(deather);
                                    lobby.setItems(adversary);

                                    cancelQueue(deather);
                                    cooldownTimer.remove(deather.getUniqueId());
                                    deather.setLevel(0);

                                    cancelQueue(adversary);
                                    cooldownTimer.remove(adversary.getUniqueId());
                                    adversary.setLevel(0);

                                    for(Player all : Bukkit.getOnlinePlayers()){
                                        deather.showPlayer(all);
                                        all.showPlayer(deather);

                                        adversary.showPlayer(all);
                                        all.showPlayer(adversary);
                                    }

                                    permissionManager.checkPermissionTag(adversary);
                                    permissionManager.checkPermissionTag(deather);

                                    //tirar os spectadores da sala e fazer eles verem todos
                                    for(Map.Entry<UUID,UUID> scan : Spec.getSpecAndBattler().entrySet()){
                                        if(adversary.getUniqueId().equals(scan.getValue())){
                                            Player spectator = Bukkit.getServer().getPlayer(scan.getKey());
                                            resetPlayer(spectator);
                                            lobby.teleportToLobby(spectator);
                                            lobby.setItems(spectator);
                                            spectator.setGameMode(GameMode.SURVIVAL);
                                            Spec.getSpecAndBattler().remove(adversary.getUniqueId(),spectator.getUniqueId());
                                            Spec.getInSpec().remove(spectator.getUniqueId());

                                            for(Player all : Bukkit.getOnlinePlayers()){
                                                spectator.showPlayer(all);
                                                all.showPlayer(spectator);
                                            }

                                        }
                                        if(deather.getUniqueId().equals(scan.getValue())){
                                            Player spectator = Bukkit.getServer().getPlayer(scan.getKey());
                                            resetPlayer(spectator);
                                            lobby.teleportToLobby(spectator);
                                            lobby.setItems(spectator);
                                            spectator.setGameMode(GameMode.SURVIVAL);
                                            Spec.getSpecAndBattler().remove(deather.getUniqueId(),spectator.getUniqueId());
                                            Spec.getInSpec().remove(spectator.getUniqueId());

                                            for(Player all : Bukkit.getOnlinePlayers()){
                                                spectator.showPlayer(all);
                                                all.showPlayer(spectator);
                                            }
                                        }
                                    }
                                    lobby.teleportToLobby(adversary);
                                    lobby.teleportToLobby(deather);
                                }
                            },20*4);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void setDeathMessage(PlayerDeathEvent event) {
        event.setDeathMessage(null);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player quitter = event.getPlayer();
        for(Map.Entry<UUID,UUID> scan : inDuel.entrySet()){
            if(quitter.getUniqueId().equals(scan.getKey())){
                Player rest = Bukkit.getServer().getPlayer(scan.getValue()); // tentar mudar isso aqui, o REST
                quitter.getWorld().strikeLightningEffect(quitter.getLocation());
                resetPlayer(quitter);
                resetPlayer(rest);

                Msg.sendMessage(rest,"§c"+quitter.getName()+"§e deslogou da partida");

                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Saturn.getInstance(), new BukkitRunnable() {
                    @Override
                    public void run() {
                        rest.setGameMode(GameMode.SURVIVAL);

                        inDuel.remove(quitter.getUniqueId());
                        inDuel.remove(rest.getUniqueId());

                        lobby.setItems(rest);

                        cancelQueue(rest);
                        cooldownTimer.remove(rest.getUniqueId());
                        rest.setLevel(0);

                        for(Player all : Bukkit.getOnlinePlayers()){
                            quitter.showPlayer(all);
                            all.showPlayer(quitter);

                            rest.showPlayer(all);
                            all.showPlayer(rest);
                        }

                        permissionManager.checkPermissionTag(rest);
                        permissionManager.checkPermissionTag(quitter);

                        //tirar os spectadores da sala e fazer eles verem todos
                        for(Map.Entry<UUID,UUID> scan : Spec.getSpecAndBattler().entrySet()){
                            if(rest.getUniqueId().equals(scan.getValue())){
                                Player spectator = Bukkit.getServer().getPlayer(scan.getKey());
                                resetPlayer(spectator);
                                lobby.teleportToLobby(spectator);
                                lobby.setItems(spectator);
                                spectator.setGameMode(GameMode.SURVIVAL);
                                Spec.getSpecAndBattler().remove(rest.getUniqueId(),spectator.getUniqueId());
                                Spec.getInSpec().remove(spectator.getUniqueId());

                                for(Player all : Bukkit.getOnlinePlayers()){
                                    spectator.showPlayer(all);
                                    all.showPlayer(spectator);
                                }

                            }
                            if(quitter.getUniqueId().equals(scan.getValue())){
                                Player spectator = Bukkit.getServer().getPlayer(scan.getKey());
                                resetPlayer(spectator);
                                lobby.teleportToLobby(spectator);
                                lobby.setItems(spectator);
                                spectator.setGameMode(GameMode.SURVIVAL);
                                Spec.getSpecAndBattler().remove(quitter.getUniqueId(),spectator.getUniqueId());
                                Spec.getInSpec().remove(spectator.getUniqueId());

                                for(Player all : Bukkit.getOnlinePlayers()){
                                    spectator.showPlayer(all);
                                    all.showPlayer(spectator);
                                }
                            }
                        }
                        lobby.teleportToLobby(rest);
                    }
                },20*4);

            } else if(quitter.getUniqueId().equals(scan.getValue())){
                Player rest = Bukkit.getServer().getPlayer(scan.getKey());
                quitter.getWorld().strikeLightningEffect(quitter.getLocation());
                resetPlayer(quitter);
                resetPlayer(rest);

                Msg.sendMessage(rest,"§c"+quitter.getName()+"§e deslogou da partida");

                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Saturn.getInstance(), new BukkitRunnable() {
                    @Override
                    public void run() {
                        rest.setGameMode(GameMode.SURVIVAL);

                        inDuel.remove(quitter.getUniqueId());
                        inDuel.remove(rest.getUniqueId());

                        lobby.setItems(rest);

                        cancelQueue(rest);
                        cooldownTimer.remove(rest.getUniqueId());
                        rest.setLevel(0);

                        for(Player all : Bukkit.getOnlinePlayers()){
                            quitter.showPlayer(all);
                            all.showPlayer(quitter);

                            rest.showPlayer(all);
                            all.showPlayer(rest);
                        }

                        permissionManager.checkPermissionTag(rest);
                        permissionManager.checkPermissionTag(quitter);

                        //tirar os spectadores da sala e fazer eles verem todos
                        for(Map.Entry<UUID,UUID> scan : Spec.getSpecAndBattler().entrySet()){
                            if(rest.getUniqueId().equals(scan.getValue())){
                                Player spectator = Bukkit.getServer().getPlayer(scan.getKey());
                                resetPlayer(spectator);
                                lobby.teleportToLobby(spectator);
                                lobby.setItems(spectator);
                                spectator.setGameMode(GameMode.SURVIVAL);
                                Spec.getSpecAndBattler().remove(rest.getUniqueId(),spectator.getUniqueId());
                                Spec.getInSpec().remove(spectator.getUniqueId());

                                for(Player all : Bukkit.getOnlinePlayers()){
                                    spectator.showPlayer(all);
                                    all.showPlayer(spectator);
                                }

                            }
                            if(quitter.getUniqueId().equals(scan.getValue())){
                                Player spectator = Bukkit.getServer().getPlayer(scan.getKey());
                                resetPlayer(spectator);
                                lobby.teleportToLobby(spectator);
                                lobby.setItems(spectator);
                                spectator.setGameMode(GameMode.SURVIVAL);
                                Spec.getSpecAndBattler().remove(quitter.getUniqueId(),spectator.getUniqueId());
                                Spec.getInSpec().remove(spectator.getUniqueId());

                                for(Player all : Bukkit.getOnlinePlayers()){
                                    spectator.showPlayer(all);
                                    all.showPlayer(spectator);
                                }
                            }
                        }
                        lobby.teleportToLobby(rest);
                    }
                },20*4);
            }
        }
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        ItemStack itemDropped = event.getItemDrop().getItemStack();
        boolean hasSameWorld = player.getWorld().equals(Bukkit.getWorld("arena1"));
        if (hasSameWorld) {
            if (itemDropped.getType().equals(Material.DIAMOND_SWORD) || itemDropped.getType().equals(Material.ENDER_PEARL)) {
                event.setCancelled(true);
            } else {
                ItemStack potionOfSpeed = new ItemStack(Material.POTION, 1, (short) 8226);
                ItemStack potionOfFireResistance = new ItemStack(Material.POTION, 1, (short) 8259);
                ItemStack potionInstaHealth = new ItemStack(Material.POTION, 1, (short) 16421);

                if (itemDropped.equals(potionOfSpeed) || itemDropped.equals(potionOfFireResistance) || itemDropped.equals(potionInstaHealth)) {
                    event.setCancelled(true);
                } else{
                    event.getItemDrop().remove();
                    player.getWorld().spigot().playEffect(player.getLocation(), Effect.PARTICLE_SMOKE);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void cooldownEnderPearl(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        if(action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)){
            if (player.getInventory().getItemInHand().getType().equals(Material.ENDER_PEARL)) {
                if(!cooldownTimer.contains(player.getUniqueId())){
                    cooldownTimer.add(player.getUniqueId());
                    event.setCancelled(false);
                    int id = Bukkit.getServer().getScheduler().scheduleAsyncRepeatingTask(Saturn.getInstance(), new BukkitRunnable() {
                        int i = 17;
                        @Override
                        public void run() {
                            i--;
                            player.setLevel(i);
                            if(i == 0){
                                player.sendMessage("§aSua enderpearl está pronta para ser usada!");
                                player.playSound(player.getLocation(),Sound.ENDERMAN_TELEPORT,5,5);
                                cancelQueue(player);
                                cooldownTimer.remove(player.getUniqueId());
                                listOfScheduler.remove(player.getUniqueId());
                            }
                        }
                    },0,20);
                    listOfScheduler.put(player.getUniqueId(),id);
                } else if(cooldownTimer.contains(player.getUniqueId())){
                    event.setCancelled(true);
                    player.sendMessage("§e Enderpearl em cooldown");
                }
            }
        }
    }
}