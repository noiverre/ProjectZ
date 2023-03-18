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

public class Despawner implements Listener {
    private Main plugin;
    final private int DESPAWN_DISTANCE = 32;

    public Despawner(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        List<Player> players = new ArrayList<>(plugin.getServer().getOnlinePlayers());

        for (Player p : players) {
            Location playerLocation = p.getLocation();
            World world = p.getWorld();

            for (Entity e : world.getEntities()) {
                if (e instanceof Zombie) {
                    Location zombieLocation = e.getLocation();
                    double distance = playerLocation.distance(zombieLocation);

                    if (distance > DESPAWN_DISTANCE) {
                        e.remove();
                        System.out.println("Removed zombies far away" + distance);
                    }

                }
            }
        }

    }

}
