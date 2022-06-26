package io.github.tanguygab.arscenes.actions;

import io.github.tanguygab.arscenes.Utils;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public class MsgAction extends Action {

    private final Pattern pattern = Pattern.compile("msg:( )?");

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
        Utils.msg(p,match);
    }
}
