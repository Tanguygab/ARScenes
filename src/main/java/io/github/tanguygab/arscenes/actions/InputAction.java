package io.github.tanguygab.arscenes.actions;

import io.github.tanguygab.arscenes.scenes.SceneSession;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public class InputAction extends Action {

    private final Pattern pattern = Pattern.compile("input:( )?");

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
        String inputName = match.split(" ")[0];
        p.sendMessage(match.replace(inputName+" ",""));
        SceneSession session = getSession(p);
        session.currentInput = inputName;
        synchronized (session.thread) {
            try {session.thread.wait();}
            catch (InterruptedException e) {e.printStackTrace();}
        }

    }
}
