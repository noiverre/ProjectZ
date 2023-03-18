/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ingeniom22;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private Despawner despawner;

    @Override
    public void onEnable() {
        getLogger().info("The projectz plugin has been loaded");

        getServer().getPluginManager().registerEvents(new Spawner(this), this);

        despawner = new Despawner(this);
        despawner.runTaskTimer(this, 1, 40);
    }

    @Override
    public void onDisable() {
        getLogger().info("The projectz plugin has been unloaded");
    }

}
