package com.ingeniom22;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class KamikazeSpawner extends BukkitRunnable {
    private final Main plugin;
    private final int GRID = 25;
    private final int kamikazesPerPlayer = 2;

    public KamikazeSpawner(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        List<Player> players = new ArrayList<>(plugin.getServer().getOnlinePlayers());

        for (Player p : players) {
            World world = p.getWorld();
            Location playerLocation = p.getLocation();

            // get initial count of nearby kamikaze
            int nearbyKamikaze = 0;
            for (Entity e : world.getEntities()) {
                if (e instanceof Zombie
                        && e.getCustomName() != null
                        && e.getCustomName().equals("Kamikaze")) {
                    Location kamikazeLocation = e.getLocation();
                    double distance = playerLocation.distance(kamikazeLocation);
                    if (distance < GRID) {
                        nearbyKamikaze++;
                    }
                }
            }


            // spawn kamikaze if less than threshold
            if (nearbyKamikaze < kamikazesPerPlayer) {
                // Get a random location within 24 blocks of the player's location
                double x = playerLocation.getX() + (Math.random() * 48) - 24;
                double y = playerLocation.getY() + 1;
                double z = playerLocation.getZ() + (Math.random() * 48) - 24;
                Location spawnLocation = new Location(world, x, y, z);

                // Spawn a kamikaze at the random location if block is
                // spawnable and distance with player
                // is greater than 16
                if (Utils.canZombieSpawn(spawnLocation)
                        && playerLocation.distance(spawnLocation) > 16) {
                            
                    Zombie kamikaze = (Zombie) world.spawnEntity(spawnLocation, EntityType.ZOMBIE);
                    kamikaze.setMetadata("type", new FixedMetadataValue(plugin, "kamikaze"));
                    kamikaze.setCustomName("Kamikaze");
                    kamikaze.setCustomNameVisible(true);
                    kamikaze.setHealth(10);
                    kamikaze.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
                    kamikaze.getEquipment().setHelmet((new ItemStack(Material.GOLDEN_HELMET)));
                    System.out.println("Spawning Kamikaze nearby " + p.getName() + "at " + spawnLocation.toString());
                }

            } else {
                System.out.println(
                        "Player " + p.getDisplayName() + " is dealing with " + nearbyKamikaze + " kamikazes!");
            }

        }
    }
}
