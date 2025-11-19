package ru.florestdev.florestBanStates;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinEventFL implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        FlorestBanStates plugin = FlorestBanStates.getInstance();

        String ip = p.getAddress().getAddress().getHostAddress();

        plugin.getLogger().info("[FBS] Player join: " + p.getName() + " IP=" + ip);

        // whitelist
        if (plugin.whitelistPlayers.contains(p.getName())) {
            plugin.getLogger().info("[FBS] " + p.getName() + " is whitelisted. Allowed.");
            return;
        }

        // async logic
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            GeoInfo info = plugin.getClient().getInfo(ip);

            if (info == null) {
                plugin.getLogger().warning("[FBS] GeoIP failed for IP " + ip + ". Allowing player.");
                return;
            }

            plugin.getLogger().info("[FBS] GeoIP: country=" + info.countryCode +
                    " region=" + info.region);

            // region check
            if (plugin.unbannedRegions.contains(info.region)) {
                plugin.getLogger().info("[FBS] Region " + info.region + " allowed.");
                return;
            }

            // country ban
            if (plugin.bannedCountries.contains(info.countryCode)) {

                String msg = plugin.bannedMessage.replace("{user}", p.getName());

                plugin.getLogger().warning("[FBS] BLOCKING " + p.getName() +
                        " (" + info.countryCode + ", " + info.region + ")");

                Bukkit.getScheduler().runTask(plugin, () -> {
                    p.kickPlayer(msg);
                });
            } else {
                plugin.getLogger().info("[FBS] ALLOWED: Country " + info.countryCode);
            }

        });
    }
}

