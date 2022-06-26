package io.github.tanguygab.arscenes.commands;

import io.github.tanguygab.arscenes.ARScenes;
import io.github.tanguygab.arscenes.Chapter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Set;

public class ListCmd {

    public static void onCommand(CommandSender sender, String[] args) {
        boolean isChapter = args.length > 1;
        Set<String> list = ARScenes.get().chapters.keySet();

        if (isChapter) {
            Chapter chapter = ARScenes.get().chapters.get(args[1]);
            if (chapter != null)
                list = ARScenes.get().chapters.get(args[1]).scenes.keySet();
            else isChapter = false;
        }
        TextComponent comp = new TextComponent("List of "+(isChapter ? "scenes in "+args[1] : "chapters")+" ("+list.size()+"):");
        boolean finalIsChapter = isChapter;
        list.forEach(element->{
            TextComponent sceneComp = new TextComponent("\n- "+element);
            sceneComp.setColor(ChatColor.GRAY);
            sceneComp.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, finalIsChapter ? "/arscenes play "+args[1]+" "+element : "/arscenes list "+element));
            comp.addExtra(sceneComp);
        });
        sender.spigot().sendMessage(comp);
    }

    public static List<String> onTabComplete(String[] args) {
        return args.length == 2 ? ARScenes.get().chapters.keySet().stream().toList() : null;
    }
}
