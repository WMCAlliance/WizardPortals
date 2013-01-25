package com.modwiz.wizardportals.storage;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: sjohnson
 * Date: 1/19/13
 * Time: 8:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class PortalDestination implements Serializable {
    String world;
    double x;
    double y;
    double z;
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
        x = location.getX();
        y = location.getY();
        z = location.getZ();

        pitch = location.getPitch();
        yaw = location.getYaw();

        world = location.getWorld().getName();
    }
}
