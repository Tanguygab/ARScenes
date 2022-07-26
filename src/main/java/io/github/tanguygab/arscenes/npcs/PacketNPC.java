package io.github.tanguygab.arscenes.npcs;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.PropertyMap;
import io.github.tanguygab.arscenes.ARScenes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.EntityPose;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class PacketNPC {

    public final String name;
    public final EntityPlayer entity;
    public final UUID uuid;
    private final Location spawnLoc;
    private final int spawnPos;

    public PacketNPC(String name, Map<String,Object> config, World world) {
        this.name = name;
        Map<String,Object> pos = (Map<String, Object>) config.get("pos");
        spawnLoc = new Location(world,
                (double) pos.get("x"),
                (double) pos.get("y"),
                (double) pos.get("z"),
                (float)(double) pos.get("yaw"),
                (float)(double) pos.get("pitch")
        );
        spawnPos = (int) config.getOrDefault("pose",0);
        uuid = UUID.randomUUID();
        GameProfile gameProfile = new GameProfile(uuid,name);
        PropertyMap skin = ARScenes.get().skins.getSkin(config.get("skin")+"");
        if (skin != null)
            gameProfile.getProperties().putAll(skin);

        entity = new EntityPlayer(((CraftServer)Bukkit.getServer()).getServer(),
                ((CraftWorld)world).getHandle(),
                gameProfile,
                null
        );
        entity.ai().b(new DataWatcherObject<>(17, DataWatcherRegistry.a),(byte)0x40);
    }
    public void spawn(Player p) {
        setLoc(spawnLoc);
        show(p);
        setPose(p,spawnPos);
    }
    public void show(Player p) {
        sendPacket(p,new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a,entity));
        PacketPlayOutNamedEntitySpawn sp = new PacketPlayOutNamedEntitySpawn(entity);
        p.sendMessage(sp.d()+" "+sp.e()+" "+sp.f());
        sendPacket(p,sp);
        updateMetadata(p);
    }
    public void hide(Player p) {
        sendPacket(p,new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e,entity));
        try {
            sendPacket(p,PacketPlayOutEntityDestroy.class.getConstructor(int[].class).newInstance(new int[]{entity.ae()}));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void rotate(Player p, float yaw, float pitch) {
        Location loc = entity.getBukkitEntity().getLocation();
        loc.setYaw(yaw);
        loc.setPitch(pitch);
        setLoc(loc);
        sendPacket(p,new PacketPlayOutEntity.PacketPlayOutEntityLook(entity.ae(), (byte)((int)(yaw * 256.0F / 360.0F)),(byte)((int)(pitch * 256.0F / 360.0F)),true));
    }
    public void move(Player p, Location loc, int delay) {
        Location cl = entity.getBukkitEntity().getLocation();

        double cx = cl.getX(), cy = cl.getY(), cz = cl.getZ();
        double fx = loc.getX(), fy = loc.getY(), fz = loc.getZ();
        boolean nx = Math.max(cx,fx) == cx, ny = Math.max(cy,fy) == cy, nz = Math.max(cz,fz) == cz;

        while (checkContinue(cx,fx,nx) || checkContinue(cy,fy,ny) || checkContinue(cz,fz,nz)) {
            if (checkContinue(cx,fx,nx)) cx = increment(cx,nx);
            if (checkContinue(cy,fy,ny)) cy = increment(cy,ny);
            if (checkContinue(cz,fz,nz)) cz = increment(cz,nz);
            teleport(p,new Location(loc.getWorld(),cx,cy,cz));
            try {Thread.sleep(delay);}
            catch (InterruptedException ignored) {}
        }
        setLoc(loc);
    }
    private double increment(double from, boolean inverted) {
        if (inverted) from-=0.1;
        else from+=0.1;
        return from;
    }
    private boolean checkContinue(double from, double to, boolean inverted) {
        return inverted ? from>to : from < to;
    }

    public void teleport(Player p, Location loc) {
        setLoc(loc);
        sendPacket(p,new PacketPlayOutEntityTeleport(entity));
    }
    private void setLoc(Location loc) {
        this.entity.a(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    }

    public void setPose(Player p, int pos) {
        entity.ai().b(new DataWatcherObject<>(6, DataWatcherRegistry.t),EntityPose.values()[pos]);
        updateMetadata(p);
    }
    private void updateMetadata(Player p) {
        sendPacket(p,new PacketPlayOutEntityMetadata(entity.ae(),entity.ai(),true));
    }

    private void sendPacket(Player p, Packet<PacketListenerPlayOut> packet) {
        ((CraftPlayer)p).getHandle().b.a(packet);
    }

    public void spectate(Player p) {
        sendPacket(p,new PacketPlayOutCamera(entity));
    }
}
