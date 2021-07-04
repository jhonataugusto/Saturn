package br.com.main;

import br.com.main.backend.MYSQL;

import br.com.main.entity.Duel;
import br.com.main.events.FirstJoin;
import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.plugin.java.JavaPlugin;


public class Saturn extends JavaPlugin {

    @Getter
    public static Saturn instance;
    @Getter
    private static MYSQL sql;


    @Override
    public void onEnable() {
        instance = this;
        sql = new MYSQL("root", "", "localhost", 3306, "saturn");
        registerEvents();
        registerCommands();
        createWorlds();
    }

    @Override
    public void onDisable() {
        getSql().closeConnection();
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new Duel(),this);
        Bukkit.getPluginManager().registerEvents(new FirstJoin(),this);
    }

    public void registerCommands() {

    }

    public void createWorlds(){
        if(Bukkit.getServer().getWorld("arena1") == null){
            WorldCreator worldCreator = new WorldCreator("arena1").environment(World.Environment.NORMAL).generateStructures(false).type(WorldType.FLAT);
            worldCreator.createWorld();
        }
    }

}
