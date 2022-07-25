package io.github.tanguygab.arscenes.scenes;

import io.github.tanguygab.arscenes.config.ConfigurationFile;
import io.github.tanguygab.arscenes.config.YamlConfigurationFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Chapter {

    public final Map<String,Scene> scenes = new HashMap<>();

    public Chapter(File folder) throws IOException {
        for (File file : folder.listFiles()) {
            ConfigurationFile cfg = new YamlConfigurationFile(null, file);
            String name = file.getName().replace(".yml", "");
            scenes.put(name,new Scene(name,cfg));
        }
    }
}
