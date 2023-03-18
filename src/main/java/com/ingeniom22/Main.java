package com.ingeniom22.projectz;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private Spawner spawner;
    private Despawner despawner;

    @Override
    public void onEnable() {
        getLogger().info("=====[ProjectZ @ingenio]=====");

        getServer().getPluginManager().registerEvents(new BlockNaturalSpawn(this), this);

        spawner = new Spawner(this);
        spawner.runTaskTimer(this, 1, 20);

        despawner = new Despawner(this);
        despawner.runTaskTimer(this, 1, 20);
    }

    @Override
    public void onDisable() {
        getLogger().info("=====[Goodbye :D]=====");
    }

}
