package com.modwiz.wizardportals.storage;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: sjohnson
 * Date: 1/24/13
 * Time: 7:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class SerializableLocation implements Serializable {
    private int x,y,z;
    private float pitch, yaw;
    private String worldname;

    public SerializableLocation(Location loc) {
        this.x = loc.getBlockX();
        this.y = loc.getBlockY();
        this.z = loc.getBlockZ();
        this.pitch = loc.getPitch();
        this.yaw = loc.getYaw();
        this.worldname = loc.getWorld().getName();
    }

    public int getBlockX() {
        return x;
    }

    public int getBlockY() {
        return y;
    }

    public int getBlockZ() {
        return z;
    }

    public String getWorldName() {
        return worldname;
    }

    public String toString() {
        return "X: " + x + "\n" +
                "Y: " + y + "\n" +
                "Z: " + z + "\n" +
                "World: " + worldname;
    }
}
