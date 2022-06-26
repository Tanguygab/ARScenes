package io.github.tanguygab.arscenes.commands;

import io.github.tanguygab.arscenes.ARScenes;
import io.github.tanguygab.arscenes.Chapter;
import io.github.tanguygab.arscenes.Scene;
import io.github.tanguygab.arscenes.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class PlayCmd {

    public static void onCommand(CommandSender sender, String[] args) {
        if (args.length < 2) {
            Utils.msg(sender,"&cNo chapter specified!");
            return;
        }
        Chapter chapter = ARScenes.get().chapters.get(args[1]);
        if (chapter == null) {
            Utils.msg(sender,"&cChapter not found!");
            return;
        }
        if (args.length < 3) {
            Utils.msg(sender,"&cNo scene specified!");
            return;
        }
        Scene scene = chapter.scenes.get(args[2]);
        if (scene == null) {
            Utils.msg(sender,"&cScene not found!");
            return;
        }
        if (args.length < 4 && !(sender instanceof Player)) {
            Utils.msg(sender,"&cNo player specified!");
            return;
        }
        Player p = sender instanceof Player player ? player : Bukkit.getServer().getPlayer(args[3]);
        if (p == null) {
            Utils.msg(sender,"&cPlayer not found!");
            return;
        }
        if (
        ARScenes.get().inScene.containsKey(p)) {
            Utils.msg(sender,"&cPlayer already in scene!");
            return;
        }

        ARScenes.get().inScene.put(p,scene);
        new Thread(()->{
            scene.start(p);
            try {Thread.sleep(5000);}
            catch (InterruptedException e) {throw new RuntimeException(e);}
            scene.end(p);
            ARScenes.get().inScene.remove(p);
        }).start();
    }

    public static List<String> onTabComplete(String[] args) {
        return switch (args.length) {
            case 2 -> ARScenes.get().chapters.keySet().stream().toList();
            case 3 -> ARScenes.get().chapters.get(args[1]).scenes.keySet().stream().toList();
            default -> null;
        };
    }
}
