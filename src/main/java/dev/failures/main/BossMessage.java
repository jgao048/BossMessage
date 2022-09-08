package dev.failures.main;

import dev.failures.main.Listeners.MobDamageListener;
import dev.failures.main.Listeners.MobDeathListener;
import dev.failures.main.Listeners.MobSpawnListener;
import dev.failures.main.Placeholders.Cooldown;
import org.bukkit.plugin.java.JavaPlugin;

public final class BossMessage extends JavaPlugin {
    public static BossMessage instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new MobDamageListener(this),this);
        getServer().getPluginManager().registerEvents(new MobSpawnListener(this),this);
        getServer().getPluginManager().registerEvents(new MobDeathListener(this),this);
        new Cooldown().register();
    }

    public static BossMessage getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
