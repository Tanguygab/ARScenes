package io.github.tanguygab.arscenes;

import io.github.tanguygab.arscenes.commands.ListCmd;
import io.github.tanguygab.arscenes.commands.PlayCmd;
import io.github.tanguygab.arscenes.config.ConfigurationFile;
import io.github.tanguygab.arscenes.config.YamlConfigurationFile;
import io.github.tanguygab.arscenes.actions.Action;
import io.github.tanguygab.arscenes.npcs.skins.SkinManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ARScenes extends JavaPlugin implements CommandExecutor {

    private static ARScenes instance;
    private ConfigurationFile config;
    public final Map<String,Chapter> chapters = new HashMap<>();
    public final Map<Player,Scene> inScene = new HashMap<>();
    public SkinManager skins;

    @Override
    public void onEnable() {
        instance = this;
        Action.registerAll();
        try {
            config = new YamlConfigurationFile(getResource("config.yml"), new File(getDataFolder(), "config.yml"));
            skins = new SkinManager();
            Path path = Path.of(getDataFolder().getAbsolutePath()+"/chapters");
            File folder = path.toFile();
            if (!folder.exists()) {
                new File(getDataFolder().getAbsolutePath()+"/chapters/ExampleChapter/").mkdirs();
                File exampleFile = new File(getDataFolder().getAbsolutePath()+"/chapters/ExampleChapter/ExampleScene.yml");
                Files.copy(getResource("chapters/ExampleChapter/ExampleScene.yml"),exampleFile.toPath());
            }
            File[] files = folder.listFiles();
            for (File file : files) {
                if (!file.isDirectory()) continue;
                chapters.put(file.getName(),new Chapter(file));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ARScenes get() {
        return instance;
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        chapters.clear();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String arg = args.length > 0 ? args[0] : "";
        switch (arg) {
            case "play" -> PlayCmd.onCommand(sender,args);
            case "list" -> ListCmd.onCommand(sender,args);
            case "reload" -> {
                onDisable();
                onEnable();
                Utils.msg(sender,"&aPlugin Reloaded!");
            }
            default -> Utils.msg(sender,"&m                                        \n"
                    + "&a[ARScenes] &7" + getDescription().getVersion() + "\n"
                    + " - &3/arscenes [help]\n"
                    + "   &8| &aDefault help page\n"
                    + " - &3/arscenes play <chapter> <scene> [player]\n"
                    + "   &8| &aPlays the specified scene\n"
                    + " - &3/arscenes list [chapter]\n"
                    + "   &8| &aList of all loaded chapters or scenes in the specified chapter\n"
                    + " - &3/arscenes reload\n"
                    + "   &8| &aReloads the configuration file\n"
                    + "&m                                        ");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) return List.of("help","play","list","reload");

        return switch (args[0]) {
            case "play" -> PlayCmd.onTabComplete(args);
            case "list" -> ListCmd.onTabComplete(args);
            default -> null;
        };
    }
}
