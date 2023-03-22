package com.ingeniom22;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private Spawner spawner;
    private JuggernautManager juggernautManager;
    private KamikazeManager kamikazeManager;
    private GrapplerManager grapplerManager;
    private Despawner despawner;

    @Override
    public void onEnable() {
        getLogger().info("=====[ProjectZ @ingenio]=====");

        getServer().getPluginManager().registerEvents(new BlockNaturalSpawn(this), this);
        getServer().getPluginManager().registerEvents(new BlockZombieCombust(this), this);
        getServer().getPluginManager().registerEvents(new BlockDrowned(this), this);
        getServer().getPluginManager().registerEvents(new JuggernautManager(this), this);
        getServer().getPluginManager().registerEvents(new KamikazeManager(this), this);
        getServer().getPluginManager().registerEvents(new GrapplerManager(this), this);

        spawner = new Spawner(this);
        spawner.runTaskTimer(this, 0, 20);

        juggernautManager = new JuggernautManager(this);
        juggernautManager.runTaskTimer(this, 0, 20);

        kamikazeManager = new KamikazeManager(this);
        kamikazeManager.runTaskTimer(this, 0, 20);

        grapplerManager = new GrapplerManager(this);
        grapplerManager.runTaskTimer(this, 0, 20);

        despawner = new Despawner(this);
        despawner.runTaskTimer(this, 0, 20);
    }

    @Override
    public void onDisable() {
        getLogger().info("=====[Goodbye :D]=====");
    }

}
