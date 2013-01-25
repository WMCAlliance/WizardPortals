package com.modwiz.wizardportals.storage;

import org.bukkit.Location;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sjohnson
 * Date: 1/18/13
 * Time: 10:36 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class PortalStore {
    // Get our portal by its name
    public abstract Portal getPortal(String name);

    // Take an x, y, z and world and test if your in a portal.
    public abstract Portal getPortal(int x, int y, int z, String world);

    // Takes a more common location object and tests for a portal then returns it
    public abstract Portal getPortal(Location location);

    // Adds a new portal only using location objects
    public abstract void addPortal(PortalDestination destination, PortalRegion region, String portalName);

    public abstract void deletePortal(String portalName);

    public abstract Map<String, Portal> getPortals();

    // Test if a point is inside a portal
    protected boolean isInside(int x, int y, int z, String worldname, Portal portal) {
        if (!portal.worldname.equals(worldname)) {
            return false;
        }

        int upperX, lowerX;
        int upperY, lowerY;
        int upperZ, lowerZ;

        PortalRegion area = portal.getInterior();

        lowerX = area.leftCorner.getBlockX();
        upperX = area.rightCorner.getBlockX();

        lowerY = area.leftCorner.getBlockY();
        upperY = area.rightCorner.getBlockY();

        lowerZ = area.leftCorner.getBlockZ();
        upperZ = area.rightCorner.getBlockZ();

        if (!(x >= lowerX && x <= upperX)) {
            return false;
        }

        if (!(y >= lowerY && y <= upperY)) {
            return false;
        }

        if (!(z >= lowerZ && z <= upperZ)) {
            return false;
        }

        return true;
    }
}
