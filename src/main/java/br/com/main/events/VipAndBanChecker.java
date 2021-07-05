package br.com.main.events;

import br.com.main.commands.manager.VipManager;
import br.com.main.groups.manager.PermissionManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class VipAndBanChecker implements Listener {
    VipManager vip = new VipManager();
    PermissionManager permissionManager = new PermissionManager();

    @EventHandler(priority = EventPriority.NORMAL)
    public void playerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Date date = new Date();

        Date finalDateUtil = vip.getFinalDate(player);
        Date finalDate = new java.sql.Date(finalDateUtil.getTime());

        if (vip.hasVIP(player)) {
            if (date.after(finalDate)) {
                vip.removeVIP(player);
                permissionManager.removePermission(player);
                permissionManager.checkPermissionTag(player);
                player.sendMessage("§cSua tag VIP expirou.");
            } else {
                LocalDate localdatefinal = new java.sql.Date(finalDateUtil.getTime()).toLocalDate();
                LocalDate dateActual = new java.sql.Date(System.currentTimeMillis()).toLocalDate();
                long diferencaEmDias = ChronoUnit.DAYS.between(dateActual,localdatefinal);
                player.sendMessage("§cFaltam " + diferencaEmDias + " dias para seu VIP expirar.");
            }
        }
    }
}
