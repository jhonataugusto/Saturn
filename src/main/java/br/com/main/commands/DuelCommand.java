package br.com.main.commands;

import br.com.main.Saturn;
import br.com.main.apis.Msg;
import br.com.main.entity.Duel;
import com.nametagedit.plugin.NametagEdit;
import lombok.Getter;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class DuelCommand implements CommandExecutor {
    @Getter
    private static Map<String, String> duelInQueue = new HashMap<>(); //cara que foi duelado // cara que mandou o duel

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            Msg.consoleNoPermMessage();
        } else {
            Player player = (Player) sender;
            if (command.getName().equalsIgnoreCase("duel")) {
                if (args.length == 0) {
                    Msg.sendMessage(player, "§cUtilize : /duel <Jogador>");
                } else if (args.length == 1) {
                    Player adversary = Bukkit.getServer().getPlayer(args[0]);
                    if (adversary.getUniqueId().equals(player.getUniqueId())) {
                        player.sendMessage("§cVocê não pode duelar a sí mesmo");
                        return false;
                    }
                    if (Duel.getInDuel().containsKey(adversary.getUniqueId()) || Duel.getInDuel().containsValue(adversary.getUniqueId())) {
                        player.sendMessage("§cEste jogador já está em duelo!");
                        return false;
                    }
                    for (Map.Entry<String, String> scan : duelInQueue.entrySet()) {
                        if (player.getUniqueId().toString().equals(scan.getValue())) {
                            player.sendMessage("§cVocê já mandou duel uma vez, espere um momento.");
                            return false;
                        } else if (player.getUniqueId().toString().equals(scan.getKey())) {
                            player.sendMessage("§cVocê já mandou duel uma vez, espere um momento.");
                            return false;
                        }
                    }
                    duelInQueue.put(adversary.getUniqueId().toString(), player.getUniqueId().toString());
                    TextComponent aceitar = new TextComponent("§a§lACEITAR");
                    TextComponent recusar = new TextComponent("§c§lRECUSAR");

                    BaseComponent[] aceitarBC = new ComponentBuilder("§aClique para aceitar").create();
                    BaseComponent[] recusarBC = new ComponentBuilder("§cClique para recusar").create();

                    HoverEvent hoverAceitar = new HoverEvent(HoverEvent.Action.SHOW_TEXT, aceitarBC);
                    HoverEvent hoverRecusar = new HoverEvent(HoverEvent.Action.SHOW_TEXT, recusarBC);

                    ClickEvent clickAceitar = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/aceitar " + player.getUniqueId().toString());
                    ClickEvent clickRecusar = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/recusar " + player.getUniqueId().toString());

                    aceitar.setClickEvent(clickAceitar);
                    recusar.setClickEvent(clickRecusar);
                    aceitar.setHoverEvent(hoverAceitar);
                    recusar.setHoverEvent(hoverRecusar);

                    player.sendMessage("§eVocê enviou um pedido de batalha para : §c" + adversary.getName());
                    adversary.sendMessage("§c" + player.getName() + "§e enviou um pedido de batalha para você.");
                    adversary.spigot().sendMessage(aceitar);
                    adversary.sendMessage(" ");
                    adversary.spigot().sendMessage(recusar);
                    final int[] contador = new int[1];
                    contador[0] = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Saturn.getInstance(), new BukkitRunnable() {
                        int i = 1;

                        @Override
                        public void run() {
                            i++;

                            if (!getDuelInQueue().containsKey(adversary.getUniqueId().toString())) {
                                duelInQueue.remove(adversary.getUniqueId().toString(), player.getUniqueId().toString());
                                Bukkit.getServer().getScheduler().cancelTask(contador[0]);
                            }

                            if (Duel.getInDuel().containsKey(adversary.getUniqueId()) || Duel.getInDuel().containsValue(adversary.getUniqueId())) {
                                duelInQueue.remove(adversary.getUniqueId().toString(), player.getUniqueId().toString());
                                Bukkit.getServer().getScheduler().cancelTask(contador[0]);
                            }

                            if (i == 20) {
                                duelInQueue.remove(adversary.getUniqueId().toString());
                                player.sendMessage("§cO seu convite de duelo expirou");
                                Bukkit.getServer().getScheduler().cancelTask(contador[0]);
                            }
                        }
                    }, 0, 20);
                }
            }
            if (command.getName().equals("aceitar")) {
                if (args.length == 1) {
                    Player player1 = Bukkit.getServer().getPlayer(UUID.fromString(args[0]));
                    if(!Duel.getInDuel().containsKey(player1.getUniqueId())){
                        for (Map.Entry<String, String> scan : duelInQueue.entrySet()) {
                            if (player1.getUniqueId().toString().equals(scan.getKey())) {
                                Player player2 = Bukkit.getServer().getPlayer(UUID.fromString(scan.getValue()));

                                Location player1Location = new Location(Bukkit.getWorld("arena1"), 436.491, 90, 103.466, 0, 0);
                                Location player2Location = new Location(Bukkit.getWorld("arena1"), 436.475, 90, 198.478, 180, -3);

                                NametagEdit.getApi().setNametag(player1, "§9§o", "");
                                NametagEdit.getApi().setNametag(player2, "§c§o", "");
                                NametagEdit.getApi().reloadNametag(player1);
                                NametagEdit.getApi().reloadNametag(player2);

                                player1.teleport(player1Location);
                                player2.teleport(player2Location);

                                Duel.setInvisible(player1, player2);

                                Duel.resetPlayer(player1);
                                Duel.resetPlayer(player2);

                                Duel.setItens(player1);
                                Duel.setItens(player2);

                                Duel.getInDuel().put(player1.getUniqueId(), player2.getUniqueId());

                                Duel.matchStatus(player1, player2);

                            } else if (player1.getUniqueId().toString().equals(scan.getValue())) {
                                Player player2 = Bukkit.getServer().getPlayer(UUID.fromString(scan.getKey()));

                                Location player1Location = new Location(Bukkit.getWorld("arena1"), 436.491, 90, 103.466, 0, 0);
                                Location player2Location = new Location(Bukkit.getWorld("arena1"), 436.475, 90, 198.478, 180, -3);

                                NametagEdit.getApi().setNametag(player1, "§9§o", "");
                                NametagEdit.getApi().setNametag(player2, "§c§o", "");
                                NametagEdit.getApi().reloadNametag(player1);
                                NametagEdit.getApi().reloadNametag(player2);

                                player1.teleport(player1Location);
                                player2.teleport(player2Location);

                                Duel.setInvisible(player1, player2);

                                Duel.resetPlayer(player1);
                                Duel.resetPlayer(player2);

                                Duel.setItens(player1);
                                Duel.setItens(player2);

                                Duel.getInDuel().put(player1.getUniqueId(), player2.getUniqueId());

                                Duel.matchStatus(player1, player2);
                            }
                        }
                    }else{
                        player.sendMessage("§cEste jogador está em duel.");
                    }
                }
            }
            if (command.getName().equals("recusar")) {
                if (args.length == 1) {
                    if (duelInQueue.containsKey(player.getUniqueId().toString())) {
                        Player playerThatDueled = Bukkit.getServer().getPlayer(UUID.fromString(args[0]));
                        playerThatDueled.sendMessage("§c" + player.getName() + "§e recusou sua solicitação de duelo.");
                        player.sendMessage("§eVocê recusou a solicitação de §c" + playerThatDueled.getName());
                        duelInQueue.remove(player.getUniqueId().toString(), playerThatDueled.getUniqueId().toString());
                    } else {
                        player.sendMessage("§cVocê já recusou a solicitação!");
                    }
                }
            }
        }
        return false;
    }
}
