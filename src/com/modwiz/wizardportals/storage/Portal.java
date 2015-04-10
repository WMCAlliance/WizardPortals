package com.modwiz.wizardportals.storage;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: sjohnson
 * Date: 1/18/13
 * Time: 10:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class Portal implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -9158310672125284985L;

	// Name of the portal
    public String name;

    // World name
    public String worldname;

    public PortalRegion portalInterior;

    // Destination
    private PortalDestination destination;

    public Portal(PortalDestination destination, PortalRegion interior, String portalName) {
        this.destination = destination;
        this.portalInterior = interior;
        this.worldname = interior.leftCorner.getWorldName();
        this.name = portalName;
    }
    public PortalRegion getInterior() {
        return portalInterior;
    }

    public PortalDestination getDestination() {
        return destination;
    }

    public void setDestination(PortalDestination dest) {
        destination = dest;
    }
}
