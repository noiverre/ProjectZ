package com.ingeniom22;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.MetadataValueAdapter;
import org.bukkit.util.Vector;

public class JuggernautListener implements Listener {
    private final Main plugin;

    public JuggernautListener(Main plugin) {
        this.plugin = plugin;
    }

    public boolean isJuggernaut(Entity entity) {
        if (entity.hasMetadata("type")) {
            return entity.getMetadata("type").get(0).asString().equals("Juggernaut");
        }
        return false;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (isJuggernaut(event.getDamager())) {
            Zombie juggernaut = (Zombie) event.getDamager();
            Entity entity = event.getEntity();
            entity.getWorld().playEffect(entity.getLocation(), Effect.STEP_SOUND, Material.IRON_BLOCK);
            Vector knockup = new Vector(0, 1.2, 0);

            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                public void run() {
                    // Set the velocity of the player after the delay
                    entity.setVelocity(juggernaut.getLocation().getDirection().add(knockup));
                    System.out.println("Knocked player up");
                }
            }, 2);

        }
    }
}
