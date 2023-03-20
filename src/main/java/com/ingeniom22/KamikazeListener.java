package com.ingeniom22;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class KamikazeListener implements Listener {
    private final Main plugin;

    public KamikazeListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntityType() == EntityType.ZOMBIE) {
            if (event.getEntity().getCustomName() != null && event.getEntity().getCustomName().equals("Kamikaze")) {
                Location loc = event.getEntity().getLocation();
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        loc.getWorld().createExplosion(loc, 4);
                        loc.getBlock().setType(Material.ZOMBIE_HEAD);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                loc.getBlock().setType(Material.AIR);
                            }
                        }.runTaskLater(plugin, 60); // 3 seconds = 60 ticks
                    }
                }.runTaskLater(plugin, 60); // 3 seconds = 60 ticks
            }
        }
    }
}