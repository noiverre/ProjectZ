package com.ingeniom22;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class JuggernautManager extends BukkitRunnable implements Listener {
    private final Main plugin;
    private final int GRID = 25;
    private final int juggernautsPerPlayer = 2;

    public JuggernautManager(Main plugin) {
        this.plugin = plugin;
    }

    public void spawnJuggernaut(World world, Location spawnLocation, Plugin plugin, Player player) {
        Zombie Juggernaut = (Zombie) world.spawnEntity(spawnLocation, EntityType.ZOMBIE);
        Juggernaut.setMetadata("type", new FixedMetadataValue(plugin, "Juggernaut"));
        Juggernaut.setCustomName("Juggernaut");
        Juggernaut.setCustomNameVisible(true);
        Juggernaut.setHealth(30);
        Juggernaut.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 2));
        Juggernaut.getEquipment().setHelmet((new ItemStack(Material.IRON_HELMET)));
        System.out.println("Spawning Juggernaut near " + player.getName() + " at " + spawnLocation.toString());
    }

    public boolean isJuggernaut(Entity entity) {
        if (entity.hasMetadata("type")) {
            return entity.getMetadata("type").get(0).asString().equals("Juggernaut");
        }
        return false;
    }

    public int getNearbyJuggernautCount(World world, Player player, double gridDistance) {
        int nearbyJuggernauts = 0;
        for (Entity entity : player.getNearbyEntities(gridDistance * 2, gridDistance*2, gridDistance * 2)) {
            if (isJuggernaut(entity)) {
                nearbyJuggernauts++;
            }
        }
        return nearbyJuggernauts;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (isJuggernaut(event.getDamager())) {
            Zombie Juggernaut = (Zombie) event.getDamager();
            Entity entity = event.getEntity();
            entity.getWorld().playEffect(entity.getLocation(), Effect.STEP_SOUND, Material.IRON_BLOCK);
            Vector knockup = new Vector(0, 1.05, 0);

            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                public void run() {
                    // Set the velocity of the player after the delay
                    entity.setVelocity(Juggernaut.getLocation().getDirection().add(knockup));
                    System.out.println("Knocked player up");
                }
            }, 2);
        }
    }

    @Override
    public void run() {
        List<Player> players = new ArrayList<>(plugin.getServer().getOnlinePlayers());
        for (Player p : players) {
            World world = p.getWorld();
            Location playerLocation = p.getLocation();

            // get initial count of nearby Juggernaut
            int nearbyJuggernaut = getNearbyJuggernautCount(world, p, GRID);
            if (nearbyJuggernaut < juggernautsPerPlayer) {
                // Get a random location within 32 blocks of the player's location
                double x = playerLocation.getX() + (Math.random() * 64) - 32;
                double y = playerLocation.getY() + 1;
                double z = playerLocation.getZ() + (Math.random() * 64) - 32;
                Location spawnLocation = new Location(world, x, y, z);

                // Spawn a Juggernaut at the random location if block is
                // spawnable and distance with player
                // is greater than 16
                if (Utils.canZombieSpawn(spawnLocation) && playerLocation.distance(spawnLocation) > 16) {
                    spawnJuggernaut(world, spawnLocation, plugin, p);
                }

            } else {
                System.out.println(
                        "Player " + p.getDisplayName() + " is dealing with " + nearbyJuggernaut + " Juggernauts!");
            }

        }
    }
}
