package com.ingeniom22;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Drowned;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
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

public class GrapplerManager extends BukkitRunnable implements Listener {
    private final Main plugin;
    private final int GRID = 25;
    private final int GrapplersPerPlayer = 1;

    public GrapplerManager(Main plugin) {
        this.plugin = plugin;
    }

    public void spawnGrappler(World world, Location spawnLocation, Plugin plugin, Player player) {
        Drowned Grappler = (Drowned) world.spawnEntity(spawnLocation, EntityType.DROWNED);
        // for mechanics
        Grappler.setMetadata("type", new FixedMetadataValue(plugin, "Grappler"));
        Grappler.setCustomName("Grappler");
        Grappler.setCustomNameVisible(true);
        Grappler.getEquipment().setItemInMainHand(new ItemStack(Material.TRIDENT));
        // modifiables
        Grappler.setHealth(20);
        Grappler.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 2));
        Grappler.getEquipment().setHelmet((new ItemStack(Material.JACK_O_LANTERN)));
        System.out.println("Spawning Grappler near " + player.getName() + " at " + spawnLocation.toString());
    }

    public boolean isGrappler(Entity entity) {
        if (entity.hasMetadata("type")) {
            return entity.getMetadata("type").get(0).asString().equals("Grappler");
        }
        return false;
    }

    public int getNearbyGrapplerCount(World world, Player player, double gridDistance) {
        int nearbyGrapplers = 0;
        for (Entity entity : player.getNearbyEntities(gridDistance * 2, gridDistance * 2, gridDistance * 2)) {
            if (isGrappler(entity)) {
                nearbyGrapplers++;
            }
        }
        return nearbyGrapplers;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityTargetEvent(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity entity = event.getEntity();

        // Check if the damager is a trident
        if (damager.getType() == EntityType.TRIDENT) {
            Trident trident = (Trident) damager;
            // Check if the trident is held by a drowned
            if (trident.getShooter() instanceof Drowned) {
                Drowned drowned = (Drowned) trident.getShooter();
                // check if the drowned is a Grappler
                if (isGrappler(drowned)) {
                    Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                        public void run() {
                            // Set the velocity of the entity after the delay
                            Vector pull = drowned.getLocation().getDirection().multiply(-1.25)
                                    .add(new Vector(0, 0.5, 0));
                            entity.setVelocity(pull);
                            System.out.println("Grappler used pulled " + pull.toString());
                        }
                    }, 2);
                }
            }
        }
    }

    @Override
    public void run() {
        List<Player> players = new ArrayList<>(plugin.getServer().getOnlinePlayers());
        for (Player p : players) {
            World world = p.getWorld();
            Location playerLocation = p.getLocation();

            // get initial count of nearby Grappler
            int nearbyGrappler = getNearbyGrapplerCount(world, p, GRID);
            if (nearbyGrappler < GrapplersPerPlayer) {
                // Get a random location within 32 blocks of the player's location
                double x = playerLocation.getX() + (Math.random() * 64) - 32;
                double y = playerLocation.getY() + 1;
                double z = playerLocation.getZ() + (Math.random() * 64) - 32;
                Location spawnLocation = new Location(world, x, y, z);

                // Spawn a Grappler at the random location if block is
                // spawnable and distance with player
                // is greater than 16
                if (Utils.canZombieSpawn(spawnLocation) && playerLocation.distance(spawnLocation) > 16) {
                    spawnGrappler(world, spawnLocation, plugin, p);
                }

            } else {
                System.out.println(
                        "Player " + p.getDisplayName() + " is dealing with " + nearbyGrappler + " Grapplers!");
            }

        }
    }
}
