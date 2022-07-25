package io.github.tanguygab.arscenes.actions;

import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public class RotateAction extends Action {

    private final Pattern pattern = Pattern.compile("rotate:( )?");

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public boolean replaceMatch() {
        return true;
    }

    @Override
    public void execute(String match, Player p) {
        String[] args = match.split(" ");
        String npc = args[0];
        float yaw = Float.parseFloat(args[1]);
        float pitch = Float.parseFloat(args[2]);
        getSession(p).getScene().npcs.get(npc).rotate(p,yaw,pitch);
    }
}
