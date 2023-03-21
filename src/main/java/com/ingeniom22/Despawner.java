package com.ingeniom22;

import java.util.List;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Zombie;
import org.bukkit.scheduler.BukkitRunnable;

public class Despawner extends BukkitRunnable {
    private Main plugin;
    private int maxPlayerDistance = 50;

    public Despawner(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        List<World> world = plugin.getServer().getWorlds();
        for (World w : world) {
            for (Entity e : w.getLivingEntities()) {
                if (e instanceof Zombie) {
                    boolean playerNearby = e.getWorld().getPlayers().stream()
                            .anyMatch(p -> p.getLocation().distance(e.getLocation()) < maxPlayerDistance);

                    if (!playerNearby) {
                        e.remove();
                        System.out.println("Despawning zombie at " + e.getLocation().toString());
                    }
                }
            }
        }

    }
}