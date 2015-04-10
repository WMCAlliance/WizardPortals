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

    // Set an existing portal
    public abstract void setPortal(Portal portal);

    public abstract void deletePortal(String portalName);

    public abstract Map<String, Portal> getPortals();

}
