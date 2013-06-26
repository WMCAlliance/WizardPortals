package com.modwiz.wizardportals.storage;

import org.bukkit.Location;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sjohnson
 * Date: 1/18/13
 * Time: 10:39 PM
 * Represents a stored portal in memory
 * This class does not save portals to the disk besides paging by the os, therefore its not safe to use in a
 * production server
 */
public class MemoryPortalStore extends PortalStore implements Serializable {
    private Map<String, Portal> storedPortals;

    public MemoryPortalStore() {
        storedPortals = new HashMap<String, Portal>();
    }

    @Override
    public Portal getPortal(String name) {
        return storedPortals.get(name);
    }

    @Override
    public Portal getPortal(int x, int y, int z, String world) {
        for (Portal portal : storedPortals.values()) {
            if (portal == null) {
                continue;
            }

            PortalRegion portalArea = portal.getInterior();

            if (portalArea.isInside(x, y, z, world)) {
                return portal;
            }
        }
        return null;
    }

    @Override
    public Portal getPortal(Location location) {
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        String worldName = location.getWorld().getName();
        Portal portal = getPortal(x, y, z, worldName);

        return portal;
    }

    @Override
    public void addPortal(PortalDestination destination, PortalRegion region, String portalName) {
        Portal portal;

        portal = new Portal(destination, region, portalName);

        storedPortals.put(portalName, portal);
    }

    @Override
    public void setPortal(Portal portal) {
        storedPortals.put(portal.name, portal);
    }

    @Override
    public void deletePortal(String portalName) {
        storedPortals.remove(portalName);
    }

    @Override
    public Map<String, Portal> getPortals() {
        return storedPortals;
    }
}
