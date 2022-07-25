package io.github.tanguygab.arscenes;

import io.github.tanguygab.arscenes.scenes.SceneSession;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class Listener implements org.bukkit.event.Listener {

    @EventHandler(ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        SceneSession session = ARScenes.get().sessions.get(p);
        if (session == null || session.currentInput.equals("")) return;
        session.inputs.put(session.currentInput,e.getMessage());
        session.currentInput = "";
        e.setCancelled(true);
        synchronized (session.thread) {
            session.thread.notify();
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        SceneSession session;
        if ((session = ARScenes.get().sessions.get(p)) == null) return;
        if (e.getTo() != null && e.getFrom().distance(e.getTo()) == 0) {
            e.setCancelled(!session.canLook);
            return;
        }

        if (!session.canMove)
            e.setCancelled(true);
    }
}
