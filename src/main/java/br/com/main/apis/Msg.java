package br.com.main.apis;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Msg {

    public static void sendMessage(Player player, String message) {
        player.sendMessage(message);
    }

    public static void sendConsoleMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(message);

    }

    public static void sendErrorMessage(Player player) {
        player.sendMessage(ChatColor.RED + "Algo deu errado! Por favor, tente novamente.");
    }

    public static void noPermMessage(Player player) {
        player.sendMessage(ChatColor.RED + "Você não tem permissão para executar este comando.");
    }

    public static void consoleNoPermMessage() {
        Bukkit.getConsoleSender().sendMessage("Você precisa ser um jogador para executar este comando");
    }

}
