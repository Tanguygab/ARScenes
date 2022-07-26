package io.github.tanguygab.arscenes.actions;

import io.github.tanguygab.arscenes.scenes.Scene;
import net.minecraft.network.protocol.game.PacketPlayOutCamera;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public class SpectateAction extends Action {

    private final Pattern pattern = Pattern.compile("spectate:( )?");

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
        EntityPlayer handle = ((CraftPlayer)p).getHandle();
        if (npc.equals("stop")) {
            handle.b.a(new PacketPlayOutCamera(handle));
            return;
        }
        Scene scene = getSession(p).getScene();
        scene.npcs.get(npc).spectate(p);
    }
}
