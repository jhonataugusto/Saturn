package br.com.main.gamer;

import br.com.main.scoreboard.ScoreboardWrapper;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
@Setter
public class Gamer {

    private UUID uniqueId;
    private String username;

    @Setter
    @Getter
    private int elo;

    private Player player;

    @Getter
    @Setter
    private ScoreboardWrapper wrapper;

    public Gamer(Player player) {
        this.uniqueId = player.getUniqueId();
        this.username = player.getName();
        this.elo = 0;
    }

    public Player getPlayer(String username) {
        return Bukkit.getPlayer(username);
    }

    public Player getPlayerByUUID(UUID uniqueId) {
        return Bukkit.getPlayer(uniqueId);
    }
}
