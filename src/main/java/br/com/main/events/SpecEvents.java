package br.com.main.events;

import br.com.main.commands.Spec;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEnderPearl;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class SpecEvents implements Listener {

    @EventHandler (priority = EventPriority.NORMAL)
    public void playerHitEvent(EntityDamageByEntityEvent event){
        if(!(event.getEntity() instanceof CraftEnderPearl)){
            if(Spec.getInSpec().contains(event.getDamager().getUniqueId())){
                event.setCancelled(true);
            }
        }
    }
}
