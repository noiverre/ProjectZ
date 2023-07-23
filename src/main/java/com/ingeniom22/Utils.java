package com.ingeniom22;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.kingdoms.constants.land.Land;

public class Utils {

    public static boolean canZombieSpawn(Location l) {
        World w = l.getWorld();
        int x = l.getBlockX();
        int y = l.getBlockY();
        int z = l.getBlockZ();
        Land land = Land.getLand(l);

        return (w.getBlockAt(x, y, z).isEmpty()
                && w.getBlockAt(x, y + 1, z).isEmpty()
                && w.getBlockAt(x, y + 2, z).isEmpty()
                && !w.getBlockAt(x, y - 1, z).isEmpty());

    }

    public static int gerNearbyEliteZombie(Location l, int grid) {
        int nearbyEliteZombie = 0;
        for (Entity e : l.getWorld().getNearbyEntities(l, grid * 2, grid * 2, grid * 2)) {
            if (e.hasMetadata("type")) {
                nearbyEliteZombie++;
            }
        }
        return nearbyEliteZombie;
    }
}