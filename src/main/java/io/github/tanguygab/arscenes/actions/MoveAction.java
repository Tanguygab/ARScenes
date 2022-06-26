package io.github.tanguygab.arscenes.actions;

import io.github.tanguygab.arscenes.ARScenes;
import io.github.tanguygab.arscenes.Scene;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public class MoveAction extends Action {

    private final Pattern pattern = Pattern.compile("walk:( )?");

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
        Scene scene = ARScenes.get().inScene.get(p);
        String[] args = match.split(" ");
        String npc = args[0];
        double x = Double.parseDouble(args[1]);
        double y = Double.parseDouble(args[2]);
        double z = Double.parseDouble(args[3]);
        Location loc = new Location(scene.world,x,y,z);
        scene.npcs.get(npc).move(p,loc);
    }
}
