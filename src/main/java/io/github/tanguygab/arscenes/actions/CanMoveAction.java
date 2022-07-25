package io.github.tanguygab.arscenes.actions;

import io.github.tanguygab.arscenes.scenes.SceneSession;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public class CanMoveAction extends Action {

    private final Pattern pattern = Pattern.compile("can-move:( )?");

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
        boolean canMove = Boolean.parseBoolean(args[0]);
        boolean canLook = args.length > 1 && Boolean.parseBoolean(args[1]);
        getSession(p).canMove = canMove;
        getSession(p).canLook = canLook;
    }
}
