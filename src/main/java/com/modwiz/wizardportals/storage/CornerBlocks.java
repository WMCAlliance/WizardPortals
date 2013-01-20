package com.modwiz.wizardportals.storage;

import org.bukkit.Location;

/**
 * Created with IntelliJ IDEA.
 * User: sjohnson
 * Date: 1/19/13
 * Time: 7:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class CornerBlocks {
    public Location leftCorner;
    public Location rightCorner;

    public CornerBlocks() {

    }

    public CornerBlocks(Location leftCorner, Location rightCorner) {
        this.leftCorner = leftCorner;
        this.rightCorner = rightCorner;
    }

    @Override
    public String toString() {
        return this.leftCorner.toString() + "\n" + this.rightCorner.toString();
    }
}
