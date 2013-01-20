package com.modwiz.wizardportals.storage;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * Created with IntelliJ IDEA.
 * User: sjohnson
 * Date: 1/19/13
 * Time: 8:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class PortalDestination {
    String world;
    int x;
    int y;
    int z;
    float pitch;
    float yaw;

    public Location toLocation() {
        World worldObject = Bukkit.getWorld(world);

        Location equivLocation = new Location(worldObject, x, y, z, yaw, pitch);

        return equivLocation;
    }

    public PortalDestination() {

    }

    public PortalDestination(Location location) {
        x = location.getBlockX();
        y = location.getBlockY();
        z = location.getBlockZ();

        pitch = location.getPitch();
        yaw = location.getYaw();

        world = location.getWorld().getName();
    }
}
