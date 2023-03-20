package com.ingeniom22;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;


public class JuggernautListener implements Listener {
    private final Main plugin;

    public JuggernautListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager().getType() == EntityType.ZOMBIE) {
            Zombie zombie = (Zombie) event.getDamager();
            if (zombie.getCustomName() != null && zombie.getCustomName().equals("Juggernaut")) {
                if (event.getEntity() instanceof Player) {
                    Player player = (Player) event.getEntity();
                    player.getWorld().playEffect(player.getLocation(), Effect.STEP_SOUND, Material.IRON_BLOCK);
                    Vector knockup = new Vector(0, 2.5, 0);
                    
                    Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                        public void run() {
                            // Set the velocity of the player after the delay
                            player.setVelocity(zombie.getLocation().getDirection().add(knockup));
                            System.out.println("Knocked player up");
                        }
                    }, 2);


                }
            }
        }
    }

}