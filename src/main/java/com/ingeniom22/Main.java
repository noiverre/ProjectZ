package com.ingeniom22;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private Spawner spawner;
    private JuggernautSpawner juggernautSpawner;
    private KamikazeSpawner kamikazeSpawner;
    private Despawner despawner;

    @Override
    public void onEnable() {
        getLogger().info("=====[ProjectZ @ingenio]=====");

        getServer().getPluginManager().registerEvents(new BlockNaturalSpawn(this), this);
        getServer().getPluginManager().registerEvents(new BlockZombieCombust(this), this);
        getServer().getPluginManager().registerEvents(new JuggernautListener(this), this);
        getServer().getPluginManager().registerEvents(new KamikazeListener(this), this);

        spawner = new Spawner(this);
        spawner.runTaskTimer(this, 1, 20);

        juggernautSpawner = new JuggernautSpawner(this);
        juggernautSpawner.runTaskTimer(this, 1, 20);

        kamikazeSpawner = new KamikazeSpawner(this);
        kamikazeSpawner.runTaskTimer(this, 1, 20);

        despawner = new Despawner(this);
        despawner.runTaskTimer(this, 1, 20);
    }

    @Override
    public void onDisable() {
        getLogger().info("=====[Goodbye :D]=====");
    }

}
