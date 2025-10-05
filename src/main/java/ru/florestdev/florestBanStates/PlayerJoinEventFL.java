package ru.florestdev.florestBanStates;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.io.IOException;

public class PlayerJoinEventFL implements Listener {
    public final Plugin plugin;
    public final Client client;

    public PlayerJoinEventFL(Plugin plugin, Client client) {
        this.plugin = plugin;
        this.client = client;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        try {
            boolean result = client.make_req(event.getPlayer().getAddress().getHostName(), event.getPlayer().getName());
            if (result) {
                event.getPlayer().kickPlayer(plugin.getConfig().getString("your_country_banned").replace("{user}", event.getPlayer().getName()));
            }
        } catch (InterruptedException | IOException e) {
            // ...
        }
    }
}
