package com.modwiz.wizardportals.storage;

import org.bukkit.Location;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: sjohnson
 * Date: 1/24/13
 * Time: 7:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class SerializableLocation implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6663244398764393246L;
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
        return String.format("(%s,%s,%s,%s)", x, y, z, worldname);
    }
}
