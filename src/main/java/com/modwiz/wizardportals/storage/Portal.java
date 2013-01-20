package com.modwiz.wizardportals.storage;

/**
 * Created with IntelliJ IDEA.
 * User: sjohnson
 * Date: 1/18/13
 * Time: 10:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class Portal {
    // Name of the portal
    public String name;

    // Bottom left corner
    public int x1;
    public int y1;
    public int z1;

    //Top right corner
    public int x2;
    public int y2;
    public int z2;

    // World name
    public String worldname;

    // Destination
    public int destX;
    public int destY;
    public int destZ;

    public float destPitch;
    public float destYaw;

    public String destWorld;

    public PortalDestination getDestination() {
        PortalDestination destination = new PortalDestination();

        destination.world = destWorld;
        destination.x = destX;
        destination.y = destY;
        destination.z = destZ;

        destination.pitch = destPitch;
        destination.yaw = destYaw;

        return destination;
    }
}
