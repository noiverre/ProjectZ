package com.ingeniom22;

import org.bukkit.Location;
import org.bukkit.World;

public class Utils {
    public static boolean canZombieSpawn(Location l) {
        World w = l.getWorld();
        int x = l.getBlockX();
        int y = l.getBlockY();
        int z = l.getBlockZ();

        return (w.getBlockAt(x, y, z).isEmpty()
                && w.getBlockAt(x, y + 1, z).isEmpty()
                && w.getBlockAt(x, y + 2, z).isEmpty()
                && !w.getBlockAt(x, y -1, z).isEmpty()
                );

    }
}