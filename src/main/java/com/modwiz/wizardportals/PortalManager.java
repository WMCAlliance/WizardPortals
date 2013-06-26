package com.modwiz.wizardportals;

import com.modwiz.wizardportals.storage.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sjohnson
 * Date: 1/20/13
 * Time: 11:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class PortalManager {
    PortalStore portalStore;
    WizardPortals plugin;

    private enum PortalManagerType {
        MEMORY,
        DISK
    }

    private PortalManager(WizardPortals plugin, PortalManagerType type) {
        this.plugin = plugin;

        if (type == PortalManagerType.MEMORY) {
            this.portalStore = new MemoryPortalStore();
        } else if (type == PortalManagerType.DISK) {
            this.portalStore = new DiskPortalStore("storedPortals.pstore", plugin);
        }
    }

    public static PortalManager createMemoryStore(WizardPortals plugin) {
        PortalManager portalManager = new PortalManager(plugin, PortalManagerType.MEMORY);
        return portalManager;
    }

    public static PortalManager createDiskStore(WizardPortals plugin) {
        PortalManager portalManager = new PortalManager(plugin, PortalManagerType.DISK);
        return portalManager;
    }

    public void addPortal(PortalDestination destination, PortalRegion region, String portalName) {
        portalStore.addPortal(destination, region, portalName);
    }

    public Portal getPortal(String portalName) {
        return portalStore.getPortal(portalName);
    }

    public void setPortal(Portal portal) {
        portalStore.setPortal(portal);
    }

    public Portal getPortal(Player player) {
        Location playerLocation = player.getLocation();

        return portalStore.getPortal(playerLocation);
    }

    public void removePortal(String portalName) {
        portalStore.deletePortal(portalName);
    }

    public Collection<Portal> getPortals() {
        return Collections.unmodifiableCollection(portalStore.getPortals().values());
    }
}
