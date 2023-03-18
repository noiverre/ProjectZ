package com.ingeniom22;

import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;

public class BlockZombieCombust implements Listener {
    private final Main plugin;

    public BlockZombieCombust(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityCombust(EntityCombustEvent e) {
        if (e.getEntity() instanceof Zombie && e.getEntity().getFireTicks() != 0) {
            e.setCancelled(true);
            e.getEntity().setFireTicks(0);
        }

    }

}