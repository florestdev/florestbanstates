package ru.florestdev.florestBanStates;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Client {

    private final Map<String, GeoInfo> cache = new ConcurrentHashMap<>();

    public GeoInfo getInfo(String ip) {

        // cached?
        if (cache.containsKey(ip)) {
            FlorestBanStates.getInstance().getLogger().info("[FBS] Cache hit for " + ip);
            return cache.get(ip);
        }

        FlorestBanStates.getInstance().getLogger().info("[FBS] Cache miss → requesting ipwho.is for " + ip);

        try {

            URL url = new URL("https://ipwho.is/" + ip);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(3000);
            con.setReadTimeout(3000);
            con.setRequestProperty("User-Agent", "FlorestBanStates");

            if (con.getResponseCode() != 200) {
                return null;
            }

            InputStreamReader reader = new InputStreamReader(con.getInputStream());
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();

            boolean success = json.get("success").getAsBoolean();
            if (!success) return null;

            String countryCode = json.get("country_code").getAsString();
            String region = json.get("region").getAsString();

            GeoInfo info = new GeoInfo(countryCode, region);
            cache.put(ip, info);

            return info;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void clearCache() {
        cache.clear();
    }
}
