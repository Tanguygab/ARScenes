package io.github.tanguygab.arscenes.npcs.skins;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import io.github.tanguygab.arscenes.ARScenes;
import io.github.tanguygab.arscenes.config.ConfigurationFile;
import io.github.tanguygab.arscenes.config.YamlConfigurationFile;

import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class SkinManager {
    private final List<String> invalidSkins = new ArrayList<>();
    private ConfigurationFile cache;

    public SkinManager() {
        try {
            File f = new File(ARScenes.get().getDataFolder(), "skincache.yml");
            if (f.exists() || f.createNewFile())
                cache = new YamlConfigurationFile(null, f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> download(String texture) {
        try {
            URL url = new URL("https://api.mineskin.org/generate/url/");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("User-Agent", "ExampleApp/v1.0");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            String jsonInputString = "{\"variant\":\"classic\",\"name\":\"string\",\"visibility\":1,\"url\":\"" + texture + "\"}";
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            InputStreamReader reader = new InputStreamReader(con.getInputStream());
            JsonObject json = (JsonObject) JsonParser.parseReader(reader);
            JsonObject data = (JsonObject) json.get("data");
            JsonObject texture2 = (JsonObject) data.get("texture");
            String value = texture2.get("value").getAsString();
            String signature = texture2.get("signature").getAsString();
            return Arrays.asList(value, signature);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public PropertyMap getSkin(String skin) {
        if (invalidSkins.contains(skin)) return null;
        if (cache.hasConfigOption(skin))
            return map(cache.getStringList(skin));

        List<String> properties = download(skin);
        if (!properties.isEmpty()) {
            cache.set(skin, properties);
            return map(properties);
        }
        return null;
    }

    private PropertyMap map(List<String> props) {
        PropertyMap map = new PropertyMap();
        map.put("textures",new Property("textures",props.get(0),props.get(1)));
        return map;
    }



}
