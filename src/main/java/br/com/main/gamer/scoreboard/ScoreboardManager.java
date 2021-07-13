package br.com.main.gamer.scoreboard;

import br.com.main.Saturn;
import br.com.main.gamer.Gamer;
import br.com.main.scoreboard.sidebar.Sidebar;
import br.com.main.task.event.UpdateEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ScoreboardManager implements Listener {

    public void updateScoreboard(Player player) {
        if (player == null) {
            return;
        }
        Gamer gamer = Saturn.getInstance().getGamerManager().getGamer(player.getUniqueId());
        Sidebar sidebar = gamer.getWrapper().getSidebar();

        if (sidebar.isHided()) {
            return;
        }
        sidebar.setTitle("§6§lSATURN");
        sidebar.updateRows(rows -> {
            rows.add(" ");
            rows.add("§7Jogadores: §3" + Bukkit.getOnlinePlayers().size());
            rows.add(" ");
            if (gamer.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
                rows.add("§cMODO CRIATIVO");
                rows.add(" ");
            } else {
                rows.add(" ");
            }
            rows.add("§e§owww.saturn.com.br");
        });
    }

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        if (event.getType() != UpdateEvent.UpdateType.SECOND) {
            return;
        }
        for (Player o : Bukkit.getOnlinePlayers()) {
            updateScoreboard(o);
        }
    }
}
