package com.modwiz.wizardportals.player;

import com.modwiz.wizardportals.WizardPortals;
import com.modwiz.wizardportals.storage.PortalDestination;
import com.modwiz.wizardportals.storage.PortalRegion;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: sjohnson
 * Date: 1/20/13
 * Time: 11:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class Session {
    private WizardPortals plugin;

    private boolean isSelecting = false;
    private boolean isSelectingDestination = false;
    private String playerName = null;
    private boolean isStale = false;
    private Location loc = null;
    private Location leftClick = null;
    private Location rightClick = null;
    private String portalName = null;
    private PortalRegion selection = null;
    private boolean isDebug = false;

    public Session(WizardPortals plugin, Player player) {
        this.plugin = plugin;
        this.playerName = player.getName();
        this.loc = player.getLocation();
    }

    private Player getPlayerFromName() {
        return this.plugin.getServer().getPlayer(playerName);
    }

    public void setStaleLocation(Location staleLoc) {
        if (this.loc.getBlockX() == staleLoc.getBlockX() && this.loc.getBlockY() == staleLoc.getBlockY() && this.loc.getBlockZ() == staleLoc.getBlockZ()) {
            this.isStale = true;
        } else {
            this.loc = staleLoc;
            this.isStale = false;
        }
    }

    public boolean isStale() {
        return this.isStale;
    }

    public boolean setLeftClick(Location location) {
        Vector pos = location.toVector();

        String world = location.getWorld().getName();

        this.leftClick = pos.toLocation(this.getWorldFromName(world));

        // Store but don't alert player, worldedit will do that.
        if (!plugin.worldEditSelections()) {
            this.getPlayerFromName().sendMessage(ChatColor.AQUA + "Primary Point set to: (" + pos.getBlockX() + ", " + pos.getBlockY() + ", " + pos.getBlockZ() + ")");
            return true;
        }

        return false;
    }

    public boolean setRightClick(Location location) {
        Vector pos = location.toVector();

        String world = location.getWorld().getName();

        this.rightClick = pos.toLocation(getWorldFromName(world));

        // Store but don't alert player, worldedit will do that.
        if (!plugin.worldEditSelections()) {
            this.getPlayerFromName().sendMessage(ChatColor.LIGHT_PURPLE + "Secondary Point set to: (" + pos.getBlockX() + ", " + pos.getBlockY() + ", " + pos.getBlockZ() + ")");
            return true;
        }

        return false;
    }

    public PortalRegion getSelectionRegion() {
        if (leftClick == null) {
            this.getPlayerFromName().sendMessage(ChatColor.DARK_AQUA + "You need to LEFT click on a block.");
            return null;
        }

        if (rightClick == null) {
            this.getPlayerFromName().sendMessage(ChatColor.DARK_AQUA + "You need to RIGHT click on a block.");
            return null;
        }

        if (!this.leftClick.getWorld().equals(this.rightClick.getWorld())) {
            this.getPlayerFromName().sendMessage(ChatColor.DARK_AQUA + "You need to select both points in the same world.");
            return null;
        }

        PortalRegion region = new PortalRegion(leftClick, rightClick);

        return region;
    }

    public void setSelecting(boolean selecting) {
        this.isSelecting = selecting;
    }

    public World getWorldFromName(String worldName) {
        return this.plugin.getServer().getWorld(worldName);
    }

    public void setSelectingDestination(boolean selecting) {
        isSelectingDestination = selecting;
    }

    public boolean isSelectingDestination() {
        return isSelectingDestination;
    }

    public String getPortalName() {
        return portalName;
    }
    /*

    public PortalRegion getSelection() {
        return this.selection;
    }

    public void setSelection(PortalRegion region) {
        this.selection = region;
    }
    */

    public void setPortalName(String portalName) {
        this.portalName = portalName;
    }

    public boolean isPortalSelected() {
        if (this.portalName != null) {
            return true;
        }
        return false;
    }

    public boolean portalCreationPending() {
        return leftClick != null && rightClick != null && portalName != null;
    }

    public void clearSelection() {
        this.portalName = null;
        this.leftClick = null;
        this.rightClick = null;
    }

    public void setDebug(boolean debug) {
        this.isDebug = debug;
    }

    public boolean isDebugging() {
        return this.isDebug;
    }
}
