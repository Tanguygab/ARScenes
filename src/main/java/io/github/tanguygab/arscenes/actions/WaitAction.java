package io.github.tanguygab.arscenes.actions;

import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public class WaitAction extends Action {

    private final Pattern pattern = Pattern.compile("wait:( )?");

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
        try {
            Thread.sleep(Integer.parseInt(match));
        } catch (Exception ignored) {}
    }
}
