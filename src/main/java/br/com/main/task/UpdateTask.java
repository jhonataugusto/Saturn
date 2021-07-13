package br.com.main.task;

import br.com.main.Saturn;
import br.com.main.task.event.UpdateEvent;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class UpdateTask extends BukkitRunnable {

    private int tick;

    @Override
    public void run() {
        Bukkit.getPluginManager().callEvent(new UpdateEvent(++tick));
    }

    public void initialize() {
        runTaskTimer(Saturn.getInstance(), 0, 1);
    }
}
