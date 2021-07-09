package br.com.main.commands;

import br.com.main.apis.Msg;
import br.com.main.entity.Duel;
import br.com.main.entity.Lobby;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class Spec implements CommandExecutor {

    @Getter
    @Setter
    private static List<UUID> inSpec = new ArrayList<>();

    @Getter
    @Setter
    private static Map<UUID, UUID> specAndBattler = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            Msg.consoleNoPermMessage();
        } else {
            if (command.getName().equalsIgnoreCase("spec")) {
                Player player = (Player) sender;
                if (args.length == 0) {
                    Msg.sendMessage(player, "§cUtilize: /spec <Jogador>");
                }
                if (args.length == 1) {
                    Player target = Bukkit.getServer().getPlayer(args[0]);

                    if (target.getUniqueId().equals(player.getUniqueId())) {
                        Msg.sendMessage(player, "§cVocê não pode espectar a sí mesmo!");
                        return false;
                    } else if (inSpec.contains(player.getUniqueId())) {
                        Msg.sendMessage(player, "§cVocê já está no modo espectador!");
                        return false;
                    }

                    if ((Duel.getInDuel().containsKey(player.getUniqueId()) && Duel.getInDuel().containsValue(target.getUniqueId())) || (Duel.getInDuel().containsKey(target.getUniqueId()) && Duel.getInDuel().containsValue(player.getUniqueId()))) {
                        player.sendMessage("§cVocê está em duelo.");
                        return false;
                    }

                    if (Duel.getInDuel().containsKey(target.getUniqueId()) || Duel.getInDuel().containsValue(target.getUniqueId())) {
                        inSpec.add(player.getUniqueId());
                        specAndBattler.put(player.getUniqueId(), target.getUniqueId());
                        Duel.resetPlayer(player);
                        player.setGameMode(GameMode.SPECTATOR);
                        player.setFlying(true);
                        player.teleport(target.getLocation());
                        for (Map.Entry<UUID, UUID> scan : Duel.getInDuel().entrySet()) {
                            if (target.getUniqueId().equals(scan.getKey())) {
                                Player player2 = Bukkit.getPlayer(scan.getValue());
                                player.showPlayer(player2);
                                player.showPlayer(target);

                            } else if (target.getUniqueId().equals(scan.getValue())) {
                                Player player2 = Bukkit.getPlayer(scan.getKey());
                                player.showPlayer(player2);
                                player.showPlayer(target);
                            }
                        }
                        Msg.sendMessage(target, "§e O jogador §c" + player.getName() + "§e está assistindo sua partida.");
                        Msg.sendMessage(player, "§e Você está assistindo uma partida de §c" + target.getName());
                    } else {
                        Msg.sendMessage(player, "§c Este jogador não está em duelo.");
                    }
                }
            }

            if (command.getName().equalsIgnoreCase("quit")) {
                Lobby lobby = new Lobby();
                Player player = (Player) sender;

                if (args.length == 0) {
                    if (inSpec.contains(player.getUniqueId())) {
                        for (Map.Entry<UUID, UUID> scan : getSpecAndBattler().entrySet()) {
                            if (player.getUniqueId().equals(scan.getKey())) {
                                Player battler = Bukkit.getServer().getPlayer(scan.getValue());
                                player.sendMessage("§e Você parou de assistir §c" + battler.getName());
                                battler.sendMessage("§c" + player.getName() + "§e parou de assistir sua partida.");
                                specAndBattler.remove(scan.getKey(), scan.getValue());
                            }
                        }
                        Duel.resetPlayer(player);

                        player.setGameMode(GameMode.SURVIVAL);
                        lobby.teleportToLobby(player);
                        lobby.setItems(player);
                        inSpec.remove(player.getUniqueId());
                        Msg.sendMessage(player, "§eVocê saiu do modo spectador.");
                    } else {
                        player.sendMessage("§cVocê já está fora do modo spectador!");
                    }
                } else {
                    Msg.sendErrorMessage(player);
                }
            }
        }
        return false;
    }
}
