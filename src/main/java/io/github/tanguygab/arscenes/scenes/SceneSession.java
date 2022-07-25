package io.github.tanguygab.arscenes.scenes;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class SceneSession {

    private final Player player;
    private final Scene scene;
    public final Thread thread;
    public final Map<String,String> inputs = new HashMap<>();
    public String currentInput = "";
    public boolean canMove = true;
    public boolean canLook = true;

    public SceneSession(Player player, Scene scene, Thread thread) {
        this.player = player;
        this.scene = scene;
        this.thread = thread;
    }

    public Player getPlayer() {
        return player;
    }

    public Scene getScene() {
        return scene;
    }

    public String parseInputs(String str) {
        for (String input : inputs.keySet())
            str = str.replace("%input-"+input+"%",inputs.get(input));
        return str;
    }
}
