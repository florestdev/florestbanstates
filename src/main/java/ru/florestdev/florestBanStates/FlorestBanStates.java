package ru.florestdev.florestBanStates;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.logging.Logger;

public class FlorestBanStates extends JavaPlugin {

    private static FlorestBanStates instance;
    private Logger log;

    public List<String> bannedCountries;
    public List<String> whitelistPlayers;
    public List<String> unbannedRegions;
    public String bannedMessage;

    private Client client;

    public static FlorestBanStates getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        log = getLogger();

        saveDefaultConfig();
        loadConfig();

        client = new Client();

        Bukkit.getPluginManager().registerEvents(new PlayerJoinEventFL(), this);

        log.info("§aFlorestBanStates 2.0 enabled.");
        log.info("§7Banned countries: " + bannedCountries);
        log.info("§7Whitelist players: " + whitelistPlayers);
        log.info("§7Unbanned regions: " + unbannedRegions);
    }

    @Override
    public void onDisable() {
        log.info("§cFlorestBanStates disabled.");
    }

    public void loadConfig() {
        reloadConfig();
        bannedCountries = getConfig().getStringList("banned_counties");
        whitelistPlayers = getConfig().getStringList("whitelist_players");
        unbannedRegions = getConfig().getStringList("unbanned_regions");
        bannedMessage = getConfig().getString("your_country_banned", "Your country is banned.");
    }

    public void reloadAll() {
        loadConfig();
        client.clearCache();
        getLogger().info("§eConfig + cache reloaded.");
    }

    public Client getClient() {
        return client;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!cmd.getName().equalsIgnoreCase("fbs")) return false;

        if (!sender.hasPermission("florestbanstates.admin")) {
            sender.sendMessage("§cYou do not have permissions.");
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            reloadAll();
            sender.sendMessage("§aFlorestBanStates reloaded!");
            return true;
        }

        sender.sendMessage("§eUsage: /fbs reload");
        return true;
    }
}
