package br.com.main.entity;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

public class Gamer {
    @Getter @Setter
    private String uuid;
    @Getter @Setter
    private Player player;

    public Gamer(String uuid, Player player) {
        this.uuid = player.getUniqueId().toString();
        this.player = player;
    }

    public Gamer(){
        
    }
}
