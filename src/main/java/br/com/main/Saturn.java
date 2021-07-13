package br.com.main;

import br.com.main.backend.MYSQL;

import br.com.main.commands.*;
import br.com.main.entity.Duel;
import br.com.main.entity.MatchResult;
import br.com.main.events.*;
import br.com.main.gamer.listener.GamerListener;
import br.com.main.gamer.manager.GamerManager;
import br.com.main.task.UpdateTask;
import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.plugin.java.JavaPlugin;


public class Saturn extends JavaPlugin {

    @Getter
    private static Saturn instance;
    @Getter
    private static MYSQL sql;

    @Getter
    private GamerManager gamerManager;


    @Override
    public void onEnable() {
        instance = this;
        sql = new MYSQL("root", "123zaq45", "localhost", 3306, "saturn");
        registerEvents();
        registerCommands();
        removeDefaultCommands();
        createWorlds();

        gamerManager = new GamerManager();

        new UpdateTask().initialize(); //Inicializa a schedule
    }

    @Override
    public void onDisable() {
        getSql().closeConnection();
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new ChatEvents(), this);
        Bukkit.getPluginManager().registerEvents(new ChargeAccounts(), this);
        Bukkit.getPluginManager().registerEvents(new FirstJoin(), this);
        Bukkit.getPluginManager().registerEvents(new Duel(), this);
        Bukkit.getPluginManager().registerEvents(new VipAndBanChecker(), this);
        Bukkit.getPluginManager().registerEvents(new TempBanChecker(), this);
        Bukkit.getPluginManager().registerEvents(new SpecEvents(), this);
        Bukkit.getPluginManager().registerEvents(new CommandRemoveEvent(), this);
        Bukkit.getPluginManager().registerEvents(new MotdEvent(), this);
        Bukkit.getPluginManager().registerEvents(new MatchResult(), this);
        Bukkit.getPluginManager().registerEvents(new GamerListener(), this);
    }

    public void registerCommands() {
        this.getCommand("tag").setExecutor(new Tag());
        this.getCommand("setperm").setExecutor(new SetPerm());
        this.getCommand("removeperm").setExecutor(new SetPerm());
        this.getCommand("setvip").setExecutor(new Vip());
        this.getCommand("tempban").setExecutor(new TempBan());
        this.getCommand("spec").setExecutor(new Spec());
        this.getCommand("quit").setExecutor(new Spec());
        this.getCommand("status").setExecutor(new Status());
        this.getCommand("build").setExecutor(new Builder());
        this.getCommand("ping").setExecutor(new Ping());
        this.getCommand("chat").setExecutor(new ChatSaturn());
        this.getCommand("duel").setExecutor(new DuelCommand());
        this.getCommand("aceitar").setExecutor(new DuelCommand());
        this.getCommand("recusar").setExecutor(new DuelCommand());
        this.getCommand("partida").setExecutor(new MatchResult.MatchCommand());
    }

    public void removeDefaultCommands() {
        this.getCommand("tell").setExecutor(new DefaultCommandsBukkit());
        this.getCommand("pl").setExecutor(new DefaultCommandsBukkit());
        this.getCommand("ver").setExecutor(new DefaultCommandsBukkit());
        this.getCommand("about").setExecutor(new DefaultCommandsBukkit());
        this.getCommand("me").setExecutor(new DefaultCommandsBukkit());
        this.getCommand("nametagedit").setExecutor(new DefaultCommandsBukkit());
        this.getCommand("ne").setExecutor(new DefaultCommandsBukkit());
        this.getCommand("nte").setExecutor(new DefaultCommandsBukkit());
        this.getCommand("viaversion").setExecutor(new DefaultCommandsBukkit());
        this.getCommand("gamemode").setExecutor(new Gamemode());
    }

    public void createWorlds() {
        if (Bukkit.getServer().getWorld("arena1") == null) {
            WorldCreator worldCreator = new WorldCreator("arena1").environment(World.Environment.NORMAL).generateStructures(false).type(WorldType.FLAT);
            worldCreator.createWorld();
        }
    }
}
