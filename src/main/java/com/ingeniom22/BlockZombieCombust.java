package com.ingeniom22;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustByBlockEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityCombustEvent;

public class BlockZombieCombust implements Listener {
    private final Main plugin;

    public BlockZombieCombust(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onZombieBurn(EntityCombustEvent e) {
        if (!(e instanceof EntityCombustByEntityEvent) && !(e instanceof EntityCombustByBlockEvent)) {
            e.setCancelled(true);
        }
    }


}