package com.ingeniom22;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.scheduler.BukkitRunnable;

public class Spawner extends BukkitRunnable {
    private final Main plugin;
    private final int GRID = 25;
    private final int zombiesPerPlayer = 8;

    // int zombiesPerPlayer = MAX_ZOMBIE_GLOBAL /
    // plugin.getServer().getOnlinePlayers().size();

    public Spawner(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        List<Player> players = new ArrayList<>(plugin.getServer().getOnlinePlayers());

        for (Player p : players) {
            World world = p.getWorld();
            Location playerLocation = p.getLocation();

            // get initial count of nearby zombie
            int nearbyZombie = 0;
            for (Entity e : p.getNearbyEntities(GRID * 2, GRID * 2, GRID * 2)) {
                if (e instanceof Zombie) {
                    nearbyZombie++;
                }
            }

            // spawn zombie if less than threshold
            if (nearbyZombie < zombiesPerPlayer) {
                // Get a random location within 24 blocks of the player's location
                double x = playerLocation.getX() + (Math.random() * 48) - 24;
                double y = playerLocation.getY() + 1;
                double z = playerLocation.getZ() + (Math.random() * 48) - 24;
                Location spawnLocation = new Location(world, x, y, z);

                // Spawn a zombie at the random location if block is
                // spawnable and distance with player
                // is greater than 16
                if (Utils.canZombieSpawn(spawnLocation) && playerLocation.distance(spawnLocation) > 16) {
                    world.spawn(spawnLocation, Zombie.class);
                    nearbyZombie++;
                    // System.out.println("Spawning zombie nearby " + p.getName() + "at " + spawnLocation.toString());
                }

            } else {
                // System.out.println("Player " + p.getDisplayName() + " is dealing with " + nearbyZombie + " zombies!");
            }

        }

    }

}
