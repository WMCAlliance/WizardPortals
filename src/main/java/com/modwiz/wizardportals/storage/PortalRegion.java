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
    public Location leftCorner;
    public Location rightCorner;

    public PortalRegion() {

    }

    public PortalRegion(Location leftCorner, Location rightCorner) {
        this.leftCorner =leftCorner;
        this.rightCorner =rightCorner;
    }

    @Override
    public String toString() {
        return this.leftCorner.toString() + "\n" + this.rightCorner.toString();
    }
}
