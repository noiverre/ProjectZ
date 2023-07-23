package com.ingeniom22;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.kingdoms.constants.land.Land;

public class KamikazeManager extends BukkitRunnable implements Listener {
    private final Main plugin;
    private final int GRID = 25;
    private final int KamikazesPerPlayer = 2;

    public KamikazeManager(Main plugin) {
        this.plugin = plugin;
    }

    public void spawnKamikaze(World world, Location spawnLocation, Plugin plugin, Player player) {
        Zombie Kamikaze = (Zombie) world.spawnEntity(spawnLocation, EntityType.ZOMBIE);
        Kamikaze.setMetadata("type", new FixedMetadataValue(plugin, "Kamikaze"));
        Kamikaze.setCustomName("Kamikaze");
        Kamikaze.setCustomNameVisible(true);
        Kamikaze.setHealth(5);
        // Kamikaze.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,
        // Integer.MAX_VALUE, 1));
        Kamikaze.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 1));
        Kamikaze.getEquipment().setHelmet((new ItemStack(Material.GOLDEN_HELMET)));
        // System.out.println("Spawning Kamikaze near " + player.getName() + " at " +
        // spawnLocation.toString());
    }

    public boolean isKamikaze(Entity entity) {
        if (entity.hasMetadata("type")) {
            return entity.getMetadata("type").get(0).asString().equals("Kamikaze");
        }
        return false;
    }

    public int getNearbyKamikazeCount(World world, Player player, double gridDistance) {
        int nearbyKamikazes = 0;
        for (Entity entity : player.getNearbyEntities(gridDistance * 2, gridDistance * 2, gridDistance * 2)) {
            if (isKamikaze(entity)) {
                nearbyKamikazes++;
            }
        }
        return nearbyKamikazes;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (isKamikaze(event.getEntity())) {
            Location loc = event.getEntity().getLocation();
            Land land = Land.getLand(loc);
            loc.getWorld().playSound(loc, Sound.ENTITY_CREEPER_PRIMED, 7, 3);
            if (!(land != null && land.isClaimed())) {
                Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                    @Override
                    public void run() {
                        loc.getWorld().createExplosion(loc, 2);
                    }
                }, 60); // 3 seconds = 60 ticks
            }
        }
    }

    @Override
    public void run() {
        List<Player> players = new ArrayList<>(plugin.getServer().getOnlinePlayers());
        for (Player p : players) {
            World world = p.getWorld();
            Location playerLocation = p.getLocation();

            // get initial count of nearby Kamikaze
            int nearbyKamikaze = getNearbyKamikazeCount(world, p, GRID);
            if (nearbyKamikaze < KamikazesPerPlayer) {
                // Get a random location within 32 blocks of the player's location
                double x = playerLocation.getX() + (Math.random() * 64) - 32;
                double y = playerLocation.getY() + 1;
                double z = playerLocation.getZ() + (Math.random() * 64) - 32;
                Location spawnLocation = new Location(world, x, y, z);

                // Spawn a Kamikaze at the random location if block is
                // spawnable and distance with player
                // is greater than 16
                if (Utils.canZombieSpawn(spawnLocation) && playerLocation.distance(spawnLocation) > 16) {
                    spawnKamikaze(world, spawnLocation, plugin, p);
                }

            }

        }
    }
}
