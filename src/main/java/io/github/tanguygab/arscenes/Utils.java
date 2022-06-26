package io.github.tanguygab.arscenes;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Utils {

    public static void msg(CommandSender sender, String msg) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',msg));
    }

}
