package com.ingeniom22;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class Spawner implements Listener {
    private final Main plugin;
    private final int GRID = 16;
    private final int MAX_ZOMBIE = 16;

    public Spawner(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        List<Player> players = new ArrayList<>(plugin.getServer().getOnlinePlayers());

        for (Player p : players) {
            World world = p.getWorld();
            Location playerLocation = p.getLocation();

            // get initial count of nearby zombie
            int nearbyZombie = 0;
            for (Entity e : world.getEntities()) {
                if (e instanceof Zombie) {
                    Location zombieLocation = e.getLocation();
                    double distance = playerLocation.distance(zombieLocation);
                    if (distance < GRID) {
                        nearbyZombie++;
                    }
                }
            }

            // spawn zombie if less than threshold
            if (nearbyZombie < MAX_ZOMBIE) {
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
                    System.out.println("Spawning zombie nearby " + p.getName() + "at" + x + y + z);
                } else {
                    continue;
                }

            }

        }
    }
}
