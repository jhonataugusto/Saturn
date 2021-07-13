package br.com.main.entity;

import br.com.main.Saturn;
import br.com.main.apis.ItemCreator;
import br.com.main.apis.Msg;
import lombok.Getter;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class MatchResult implements Listener {

    @Getter
    private static final Map<Player, Double> performanceList = new HashMap<>();
    @Getter
    private static final Map<Player, Integer> quantityOfPots = new HashMap<>();
    @Getter
    private static final Map<Player, Integer> misseds = new HashMap<>();
    @Getter
    private static final Map<Player, Inventory> inventoryPlayer = new HashMap<>();

    public static void showResults(Player player) {
        inventoryPlayer.put(player, MatchResult.getDuelInventory(player));

        TextComponent nome1 = new TextComponent("§aEstatísticas da partida (clique aqui)");
        BaseComponent[] nome1BC = new ComponentBuilder("§aClique para ver os detalhes").create();

        ClickEvent clickNome1 = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/partida " + player.getUniqueId().toString());
        HoverEvent hoverNome1 = new HoverEvent(HoverEvent.Action.SHOW_TEXT, nome1BC);

        nome1.setClickEvent(clickNome1);
        nome1.setHoverEvent(hoverNome1);

        player.sendMessage(" ");
        player.spigot().sendMessage(nome1);
        player.sendMessage(" ");
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Saturn.getInstance(), new BukkitRunnable() {
            @Override
            public void run() {
                MatchResult.deleteResults(player);
                player.sendMessage("§cInventário deletado com sucesso");
            }
        },20*20);
    }

    public static void deleteResults(Player player) {
        MatchResult.performanceList.remove(player);
        MatchResult.quantityOfPots.remove(player);
        MatchResult.misseds.remove(player);
        Duel.getInDuel().remove(player.getUniqueId());
        MatchResult.inventoryPlayer.remove(player);
    }


    public static Inventory getDuelInventory(Player player) {
        Inventory inventoryPlayer = Bukkit.createInventory(player, player.getInventory().getSize() + 18, player.getName());
        inventoryPlayer.setContents(player.getInventory().getContents());
        //cabeça
        inventoryPlayer.setItem(36, player.getInventory().getHelmet());
        //ombro
        inventoryPlayer.setItem(37, player.getInventory().getChestplate());
        //joelho
        inventoryPlayer.setItem(38, player.getInventory().getLeggings());
        //pé joelho pé joelho pé KKKKKKKKKKKKKKKK q q eu to fazendo da minha vida
        inventoryPlayer.setItem(39, player.getInventory().getBoots());
        //contar quantidade de potions no inventário do player

        inventoryPlayer.setItem(45, new ItemCreator(Material.ARROW).setName("§4Ver inventario do adversario").getStack());
        inventoryPlayer.setItem(47, new ItemCreator(Material.SKULL_ITEM).setName("§cVida : §e" + String.format("%.1f", player.getHealth())).setAmount((int) player.getHealth()).getStack());
        inventoryPlayer.setItem(48, new ItemCreator(Material.COOKED_BEEF).setName("§cFome : §e" + String.format("%.1f", (double) player.getFoodLevel())).setAmount(player.getFoodLevel()).getStack());
        inventoryPlayer.setItem(49, new ItemCreator(Material.BREWING_STAND_ITEM).setName("§cEfeitos :").setAmount(player.getActivePotionEffects().size()).setDescription("" + player.getActivePotionEffects()).getStack()); //arrumar esses efeitos ativos

        if (getPerformanceList().get(player) == null && getMisseds().get(player) == null) {
            //AFK
            inventoryPlayer.setItem(50, new ItemCreator(new ItemStack(Material.POTION, 1, (short) 16421)).setName("§cPOÇOES : §e" + 1).setDescription("§cErros: §e" +
                    0 + "\n§cAcertos : §e" + 0 + "%").getStack());
        } else if (getPerformanceList().get(player) != null && getMisseds().get(player) == null) {
            //0 MISSED
            inventoryPlayer.setItem(50, new ItemCreator(new ItemStack(Material.POTION, 1, (short) 16421)).setName("§cPOÇOES : §e" + 1).setDescription("§cErros: §e" +
                    0 + "\n§cAcertos : §e" + String.format("%.2f", getPerformanceList().get(player) * 100) + "%").getStack());
        } else if (getPerformanceList().get(player) == null && misseds.get(player) != null) {
            //CAN'T GET POTTED
            inventoryPlayer.setItem(50, new ItemCreator(new ItemStack(Material.POTION, 1, (short) 16421)).setName("§cPOÇOES : §e" + 1).setDescription("§cErros: §e" +
                    getMisseds().get(player) + "\n§cAcertos : §e" + 0 + "%").getStack());
        } else {
            inventoryPlayer.setItem(50, new ItemCreator(new ItemStack(Material.POTION, 1, (short) 16421)).setName("§cPOÇOES : §e" + 1).setDescription("§cErros: §e" +
                    getMisseds().get(player) + "\n§cAcertos : §e" + String.format("%.2f", getPerformanceList().get(player) * 100) + "%").getStack());
        }

        inventoryPlayer.setItem(52, new ItemCreator(Material.DIAMOND_SWORD).setName("§cEm breve").getStack());
        inventoryPlayer.setItem(53, new ItemCreator(Material.ARROW).setName("§4Ver inventario do adversario").getStack());
        return inventoryPlayer;
    }

    @EventHandler()
    public void onSplashPotionSplash(PotionSplashEvent event) {
        if (event.getEntity().getShooter() instanceof Player) {
            Player player = (Player) event.getEntity().getShooter();
            if (Duel.getInDuel().containsKey(player.getUniqueId()) || Duel.getInDuel().containsValue(player.getUniqueId())) {

                //calculo de quantidade de pots
                if (!(quantityOfPots.containsKey(player))) {
                    quantityOfPots.put(player, 1);
                } else {
                    quantityOfPots.put(player, quantityOfPots.get(player) + 1);
                }

                //media do calculo de pot
                if (!(performanceList.containsKey(player))) {
                    performanceList.put(player, event.getIntensity(player));
                } else {
                    double sum = performanceList.get(player) + event.getIntensity(player);
                    double media = sum / quantityOfPots.get(player);
                    performanceList.put(player, media);
                }

                //verificar se o player errou a pot
                if (!event.getAffectedEntities().contains(player)) {
                    //errou a pot
                    if (!misseds.containsKey(player)) {
                        misseds.put(player, 1);
                    } else {
                        misseds.put(player, misseds.get(player) + 1);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        ItemStack cursor = event.getCursor();

       // if(!(cursor.getType() == Material.AIR || cursor.getType() == null)) {
            if (event.getCurrentItem().getType().equals(Material.ARROW)) {
                //se o player 1 do duelo clicar na flecha, ele vai pro inventario do player 2
                //se o player 2 do duelo clicar na flecha, ele vai pro inventario do player 1
                for (Map.Entry<UUID, UUID> scan : Duel.getInDuel().entrySet()) {
                    if (player.getUniqueId().equals(scan.getKey())) { //se o jogador for o jogador 1
                        Player player2 = Bukkit.getServer().getPlayer(scan.getValue()); //pegar jogador 2
                        if (event.getInventory().getName().equals(player2.getName())) {
                            player.updateInventory();
                            player.openInventory(MatchResult.getInventoryPlayer().get(player));
                            return;
                        }
                        player.updateInventory();
                        player.openInventory(MatchResult.getInventoryPlayer().get(player2));

                    } else if (player.getUniqueId().equals(scan.getValue())) { // se o jogador for o jogador 2
                        Player player1 = Bukkit.getServer().getPlayer(scan.getKey()); //pegar jogador 1
                        if (event.getInventory().getName().equals(player1.getName())) {
                            player.updateInventory();
                            player.openInventory(MatchResult.getInventoryPlayer().get(player));
                            return;
                        }
                        player.updateInventory();
                        player.openInventory(MatchResult.getInventoryPlayer().get(player1));
                    }
                }
            }
      //  }
    }

    public static class MatchCommand implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
            if (!(sender instanceof Player)) {
                Msg.consoleNoPermMessage();
            } else {
                if (command.getName().equalsIgnoreCase("partida")) {
                    if (args.length == 1) {
                        Player player = (Player) sender;
                        Player target = Bukkit.getServer().getPlayer(UUID.fromString(args[0]));
                        if (!MatchResult.getInventoryPlayer().containsKey(target)) {
                            player.sendMessage("§cO inventário que você está querendo acessar não existe mais!");
                        } else {
                            player.openInventory(MatchResult.getInventoryPlayer().get(player));
                        }
                    }
                }
            }
            return false;
        }
    }
}
