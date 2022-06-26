package io.github.tanguygab.arscenes;

import io.github.tanguygab.arscenes.config.ConfigurationFile;
import io.github.tanguygab.arscenes.actions.Action;
import io.github.tanguygab.arscenes.npcs.PacketNPC;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scene {

    private final String name;
    public final World world;
    public final Map<String, PacketNPC> npcs = new HashMap<>();
    private final List<String> actions;

    public Scene(String name, ConfigurationFile config) {
        this.name = name;
        world = Bukkit.getServer().getWorld(config.getString("world"));
        Map<String,Map<String,Object>> map = config.getConfigurationSection("npcs");
        map.forEach((npcName,npcConfig)->npcs.put(npcName,new PacketNPC(npcName,npcConfig,world)));

        actions = config.getStringList("actions");
    }

    public void start(Player p) {
        Utils.msg(p,"&8&oStarting scene...");
        npcs.values().forEach(npc->npc.spawn(p));
        actions.forEach(event-> Action.findAndExecute(event,p));
    }
    public void end(Player p) {
        npcs.values().forEach(npc->npc.hide(p));
        Utils.msg(p,"&8&oScene ended");
    }
}
