package br.com.main.events;

import br.com.main.apis.Msg;
import br.com.main.commands.manager.TempBanManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class TempBanChecker implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        TempBanManager tempBanManager = new TempBanManager();

        if(tempBanManager.hasBanned(player)){
            Date finalDate = tempBanManager.getFinalDate(player);
            Date actualDate = new Date();

            LocalDate localFinalDate = new java.sql.Date(finalDate.getTime()).toLocalDate();
            LocalDate localActualDate = new java.sql.Date(System.currentTimeMillis()).toLocalDate();

            long difference = ChronoUnit.DAYS.between(localActualDate, localFinalDate);

            if(!actualDate.after(finalDate)){
                player.kickPlayer("§m---------------------------------------------------------- \n" +
                        "§6SATURN NETWORK \n" +
                        "\n" +
                        "§cVOCÊ FOI BANIDO TEMPORARIAMENTE \n" +
                        "\n" +
                        "§7Data prevista para o encerramento do BAN : \n" +
                        "§r( " + finalDate + " ) = "+difference+" dias\n" +
                        "\n" +
                        "§7Banido por: §f" + tempBanManager.getBanner(player) + "\n" +
                        "\n" +
                        "§7Motivo do ban: §f" + tempBanManager.getReason(player) + "\n" +
                        "§m----------------------------------------------------------");
            } else{
                Msg.sendMessage(player,"§aVocê não está mais banido!");
                tempBanManager.removeBan(player);
            }
        }
    }

}
