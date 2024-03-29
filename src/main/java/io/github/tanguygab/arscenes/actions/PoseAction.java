package io.github.tanguygab.arscenes.actions;

import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public class PoseAction extends Action {

    private final Pattern pattern = Pattern.compile("pose:( )?");

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
        int pose = Integer.parseInt(args[1]);
        getSession(p).getScene().npcs.get(npc).setPose(p,pose);
    }
}
