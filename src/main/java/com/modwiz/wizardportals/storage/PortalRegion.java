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
        if (!this.leftCorner.getWorldName().equals(worldname)) {
            return false;
        }

        int upperX, lowerX;
        int upperY, lowerY;
        int upperZ, lowerZ;

        lowerX = leftCorner.getBlockX();
        upperX = rightCorner.getBlockX();

        if (lowerX > upperX) {
            int tempX = upperX;
            upperX = lowerX;
            lowerX = tempX;
        }

        lowerY = leftCorner.getBlockY();
        upperY = rightCorner.getBlockY();

        if (lowerY > upperY) {
            int tempY = upperY;
            upperY = lowerY;
            lowerY = tempY;
        }

        lowerZ = leftCorner.getBlockZ();
        upperZ = rightCorner.getBlockZ();

        if (lowerZ > upperZ) {
            int tempZ = upperZ;
            upperZ = lowerZ;
            lowerZ = tempZ;
        }

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
    public boolean isInsideDebug(int x, int y, int z, String worldname) {
        if (!this.leftCorner.getWorldName().equals(worldname)) {
            return false;
        }

        int upperX, lowerX;
        int upperY, lowerY;
        int upperZ, lowerZ;


        lowerX = leftCorner.getBlockX();
        upperX = rightCorner.getBlockX();

        if (lowerX > upperX) {
            int tempX = upperX;
            upperX = lowerX;
            lowerX = tempX;
        }

        lowerY = leftCorner.getBlockY();
        upperY = rightCorner.getBlockY();

        if (lowerY > upperY) {
            int tempY = upperY;
            upperY = lowerY;
            lowerY = tempY;
        }

        lowerZ = leftCorner.getBlockZ();
        upperZ = rightCorner.getBlockZ();

        if (lowerZ > upperZ) {
            int tempZ = upperZ;
            upperZ = lowerZ;
            lowerZ = tempZ;
        }

        System.out.println("MOVE DEBUG BEGIN");

        if (!(x >= lowerX && x <= upperX)) {
            System.out.println("X fail");
            System.out.println("Expected range for X was between " + lowerX + " and " + upperX);
            System.out.println("Actually got " + x);
            System.out.println("MOVE DEBUG END");

            return false;
        }
        System.out.println("X success");

        if (!(y >= lowerY && y <= upperY)) {
            System.out.println("Y fail");
            System.out.println("Expected range for Y was between " + lowerY + " and " + upperY);
            System.out.println("Actually got " + y);
            System.out.println("MOVE DEBUG END");

            return false;
        }
        System.out.println("Y success");
        if (!(z >= lowerZ && z <= upperZ)) {
            System.out.println("Z fail");
            System.out.println("Expected range for Z was between " + lowerZ + " and " + upperZ);
            System.out.println("Actually got " + z);
            System.out.println("MOVE DEBUG END");

            return false;
        }
        System.out.println("Z success");
        System.out.println("MOVE DEBUG END");
        return true;
    }

    @Override
    public String toString() {
        return this.leftCorner.toString() + "\n" + this.rightCorner.toString();
    }
}
