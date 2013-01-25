package com.modwiz.wizardportals.storage;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: sjohnson
 * Date: 1/24/13
 * Time: 7:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class SerializableLocation extends Location implements Serializable {

    public SerializableLocation(World world, double x, double y, double z) {
        super(world, x, y, z);
    }
}
