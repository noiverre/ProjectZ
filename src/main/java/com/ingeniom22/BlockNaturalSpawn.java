package com.ingeniom22;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class BlockNaturalSpawn implements Listener {
    private final Main plugin;

    public BlockNaturalSpawn(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntitySpawn(CreatureSpawnEvent event) {
        if (event.getEntityType() == EntityType.ZOMBIE && event.getSpawnReason() == SpawnReason.NATURAL) {
            // Cancel the spawn event if it's a natural zombie spawn
            event.setCancelled(true);
            // System.out.println("Blocked natural spawn of " + event.toString());
        }
    }

}