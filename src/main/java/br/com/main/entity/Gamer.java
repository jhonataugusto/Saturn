package br.com.main.entity;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

public class Gamer {
    @Getter @Setter
    private Player player;
    @Getter @Setter
    private String elo;

    public Gamer(Player player){
        this.player = player;
    }
    public Gamer(){

    }
}
