package ru.florestdev.florestBanStates;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Client {
    public final Plugin plugin;
    public final HttpClient httpClient;
    public Client(Plugin plugin) {
        this.plugin = plugin;
        this.httpClient = HttpClient.newHttpClient();
    }

    public boolean make_req(String ip, String playerName) throws IOException, InterruptedException {
        String url = "http://ip-api.com/json/" + ip + "?lang=ru";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            plugin.getLogger().severe("We can't send request to ip-api.com!");
            return false;
        } else {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response.body());

            // Проверяем статус ответа API
            if (jsonNode.get("status").asText().equals("success")) {
                String countryCode = jsonNode.get("countryCode").asText();
                plugin.getLogger().info("%s logged in: %s. Code of country: %s".formatted(playerName, ip, countryCode));
                for (String restrictedCode : plugin.getConfig().getStringList("banned_counties")) {
                    if (restrictedCode.equalsIgnoreCase(countryCode)) {
                        if (!plugin.getConfig().getStringList("whitelist_players").contains(playerName)) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
            } else {
                return false;
            }
        }
        return false;
    }

}
