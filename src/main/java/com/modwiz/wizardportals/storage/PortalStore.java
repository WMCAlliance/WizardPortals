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
    public abstract void addPortal(PortalDestination destination, Location leftCorner, Location rightCorner, String portalName);

    public abstract void deletePortal(String portalName);

    public abstract Map<String, Portal> getPortals();

    // Test if a point is inside a portal
    protected boolean isInside(int x, int y, int z, String worldname, Portal portal) {
        if (!portal.worldname.equals(worldname)) {
            return false;
        }

        if (!(x >= portal.x1 && x <= portal.x2)) {
            return false;
        }

        if (!(y >= portal.y1 && y <= portal.y2)) {
            return false;
        }

        if (!(z >= portal.z1 && z <= portal.z2)) {
            return false;
        }

        return true;
    }
}
