package io.github.tanguygab.arscenes.actions;

import io.github.tanguygab.arscenes.ARScenes;
import io.github.tanguygab.arscenes.scenes.SceneSession;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Action {

    private static final List<Action> ACTIONS = new ArrayList<>();

    public static Action find(String action) {
        for (Action e : ACTIONS) {
            Matcher matcher = e.getPattern().matcher(action);
            if (matcher.find())
                return e;
        }
        return null;
    }

    public static void execute(String event, Action e, Player p) {
        if (e == null) return;
        if (e.replaceMatch())
            event = event.replaceAll(e.getPattern().pattern(),"");
        e.execute(event,p);
    }

    public static void findAndExecute(String event, Player p) {
        Action e = find(event);
        execute(ARScenes.get().sessions.get(p).parseInputs(event),e,p);
    }

    public abstract Pattern getPattern();

    public abstract boolean replaceMatch();

    public SceneSession getSession(Player p) {
        return ARScenes.get().sessions.get(p);
    }

    public abstract void execute(String match, Player p);

    public static void registerAll() {
        ACTIONS.addAll(List.of(
                new MsgAction(),
                new WaitAction(),
                new InputAction(),

                new CanMoveAction(),

                new TpAction(),
                new MoveAction(),
                new RotateAction(),
                new PoseAction()
        ));
    }

}
