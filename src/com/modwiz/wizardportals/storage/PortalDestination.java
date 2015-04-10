package com.modwiz.wizardportals.storage;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: sjohnson
 * Date: 1/19/13
 * Time: 8:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class PortalDestination implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 74930298235575477L;
	String world;
    double x;
    double y;
    double z;
    float pitch;
    float yaw;
    boolean isWorldDest = false;

    public Location toLocation(Plugin plugin) {
        if (isWorldDest) {
            Location worldSpawn = plugin.getServer().getWorld(world).getSpawnLocation();
            return worldSpawn;
        }
        World worldObject = plugin.getServer().getWorld(world);

        Location equivLocation = new Location(worldObject, x, y, z, yaw, pitch);

        return equivLocation;
    }

    public PortalDestination() {

    }

    public PortalDestination(Location location) {
        x = location.getX();
        y = location.getY();
        z = location.getZ();

        pitch = location.getPitch();
        yaw = location.getYaw();

        world = location.getWorld().getName();
        isWorldDest = false;
    }

    public PortalDestination(World world) {
        this.world = world.getName();

        isWorldDest = true;
    }

    public PortalDestination(String worldName) {
        this.world = worldName;
        isWorldDest = true;
    }

    @Override
    public String toString() {
        return String.format("(%s,%s,%s,%s)", x, y, z, world);
    }

}
