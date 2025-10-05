package ru.florestdev.florestBanStates;
import org.bukkit.plugin.java.JavaPlugin;

public final class FlorestBanStates extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getLogger().info("Started the plugin!");
        getLogger().info("We are banning the restricted countries by config.yml!");
        getServer().getPluginManager().registerEvents(new PlayerJoinEventFL(this, new Client(this)), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling the ban-hammer for countries on your server.");
    }

}
