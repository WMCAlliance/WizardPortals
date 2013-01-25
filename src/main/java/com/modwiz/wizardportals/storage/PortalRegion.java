package com.modwiz.wizardportals.storage;

import org.bukkit.Location;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: sjohnson
 * Date: 1/19/13
 * Time: 7:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class PortalRegion implements Serializable {
    public SerializableLocation leftCorner;
    public SerializableLocation rightCorner;

    public PortalRegion() {

    }

    public PortalRegion(Location leftCorner, Location rightCorner) {
        this.leftCorner = new SerializableLocation(leftCorner);
        this.rightCorner = new SerializableLocation(rightCorner);
    }

    public boolean isInside(int x, int y, int z, String worldname) {
        if (!worldname.equals(worldname)) {
            return false;
        }

        int upperX, lowerX;
        int upperY, lowerY;
        int upperZ, lowerZ;


        lowerX = leftCorner.getBlockX();
        upperX = rightCorner.getBlockX();

        lowerY = leftCorner.getBlockY();
        upperY = rightCorner.getBlockY();

        lowerZ = leftCorner.getBlockZ();
        upperZ = rightCorner.getBlockZ();

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

    @Override
    public String toString() {
        return this.leftCorner.toString() + "\n" + this.rightCorner.toString();
    }
}
