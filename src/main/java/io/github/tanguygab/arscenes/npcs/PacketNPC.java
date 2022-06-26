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
    public void move(Player p, Location loc) {
        setLoc(loc);
        //teleporting because I don't know how to make it move =/
        sendPacket(p,new PacketPlayOutEntityTeleport(entity));
    }
    private void setLoc(Location loc) {
        this.entity.a(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    }

    public void setPose(Player p, int pos) {
        p.sendMessage(pos+" new pose");
        entity.ai().b(new DataWatcherObject<>(6, DataWatcherRegistry.t),EntityPose.values()[pos]);
        updateMetadata(p);
    }
    private void updateMetadata(Player p) {
        sendPacket(p,new PacketPlayOutEntityMetadata(entity.ae(),entity.ai(),true));
    }

    private void sendPacket(Player p, Packet<PacketListenerPlayOut> packet) {
        ((CraftPlayer)p).getHandle().b.a(packet);
    }
}
