package com.modwiz.wizardportals.storage;

import com.modwiz.wizardportals.WizardPortals;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sjohnson
 * Date: 1/24/13
 * Time: 7:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class DiskPortalStore extends PortalStore{
    WizardPortals plugin;
    String filename;
    MemoryPortalStore memoryCache;
    public DiskPortalStore(String filename, WizardPortals plugin) {
        this.plugin = plugin;
        this.filename = filename;
        this.memoryCache = new MemoryPortalStore();
    }

    public void load() {
        File storedData = new File(this.plugin.getDataFolder(), this.filename);

        if (storedData.exists()) {
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storedData));
                Object object = ois.readObject();

                if (object instanceof MemoryPortalStore) {
                    this.memoryCache = (MemoryPortalStore) object;
                }
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (ClassNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    public void save() {
        File storedData = new File(this.plugin.getDataFolder(), this.filename);

        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storedData));
            oos.writeObject(this.memoryCache);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    @Override
    public Portal getPortal(String name) {
        load();

        return memoryCache.getPortal(name);
    }

    @Override
    public Portal getPortal(int x, int y, int z, String world) {
        load();

        return memoryCache.getPortal(x, y, z, world);
    }

    @Override
    public Portal getPortal(Location location) {
        load();

        return memoryCache.getPortal(location);
    }

    @Override
    public void addPortal(PortalDestination destination, PortalRegion region, String portalName) {
        memoryCache.addPortal(destination, region, portalName);

        save();
    }

    @Override
    public void setPortal(Portal portal) {
        memoryCache.setPortal(portal);
        save();
    }

    @Override
    public void deletePortal(String portalName) {
        memoryCache.deletePortal(portalName);

        save();
    }

    @Override
    public Map<String, Portal> getPortals() {
        load();

        return memoryCache.getPortals();
    }
}
