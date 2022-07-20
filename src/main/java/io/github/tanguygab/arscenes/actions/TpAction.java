package io.github.tanguygab.arscenes.actions;

import io.github.tanguygab.arscenes.ARScenes;
import io.github.tanguygab.arscenes.Scene;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public class TpAction extends Action {

    private final Pattern pattern = Pattern.compile("tp:( )?");

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
        double x = Double.parseDouble(args[1]);
        double y = Double.parseDouble(args[2]);
        double z = Double.parseDouble(args[3]);
        Scene scene = getScene(p);
        Location loc = new Location(scene.world,x,y,z);
        scene.npcs.get(npc).teleport(p,loc);
    }
}
