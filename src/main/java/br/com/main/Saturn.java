package br.com.main;

import br.com.main.backend.MYSQL;

import br.com.main.commands.SetPerm;
import br.com.main.commands.Tag;
import br.com.main.commands.Vip;
import br.com.main.entity.Duel;
import br.com.main.events.ChargeAccounts;
import br.com.main.events.FirstJoin;
import br.com.main.events.TagProvider;
import br.com.main.events.VipAndBanChecker;
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
        Bukkit.getPluginManager().registerEvents(new TagProvider(),this);
        Bukkit.getPluginManager().registerEvents(new ChargeAccounts(),this);
        Bukkit.getPluginManager().registerEvents(new FirstJoin(),this);
        Bukkit.getPluginManager().registerEvents(new Duel(),this);
        Bukkit.getPluginManager().registerEvents(new VipAndBanChecker(),this);

    }

    public void registerCommands() {
        this.getCommand("tag").setExecutor(new Tag());
        this.getCommand("setperm").setExecutor(new SetPerm());
        this.getCommand("removeperm").setExecutor(new SetPerm());
        this.getCommand("setvip").setExecutor(new Vip());

    }

    public void createWorlds(){
        if(Bukkit.getServer().getWorld("arena1") == null){
            WorldCreator worldCreator = new WorldCreator("arena1").environment(World.Environment.NORMAL).generateStructures(false).type(WorldType.FLAT);
            worldCreator.createWorld();
        }
    }

}
