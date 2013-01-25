package com.modwiz.wizardportals.player;

import com.modwiz.wizardportals.WizardPortals;
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
    private String playerName = null;
    private boolean isStale = false;
    private Location loc = null;
    private Location leftClick = null;
    private Location rightClick = null;

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
        if (!this.isSelecting) {
            return false;
        }

        Vector pos = location.toVector();

        String world = location.getWorld().getName();

        this.leftClick = pos.toLocation(this.getWorldFromName(world));

        this.getPlayerFromName().sendMessage(ChatColor.AQUA + "Primary Point set to: (" + pos.getBlockX() + ", " + pos.getBlockY() + ", " + pos.getBlockZ() + ")");
        return true;
    }

    public boolean setRightClick(Location location) {
        if (!this.isSelecting) {
            return false;
        }

        Vector pos = location.toVector();

        String world = location.getWorld().getName();

        this.rightClick = pos.toLocation(getWorldFromName(world));

        this.getPlayerFromName().sendMessage(ChatColor.LIGHT_PURPLE + "Secondary Point set to: (" + pos.getBlockX() + ", " + pos.getBlockY() + ", " + pos.getBlockZ() + ")");
        return true;
    }

    public PortalRegion getSelectionRegion() {
        if (leftClick == null) {
            this.getPlayerFromName().sendMessage("You need to LEFT click on a block.");
            return null;
        }

        if (rightClick == null) {
            this.getPlayerFromName().sendMessage("You need to RIGHT click on a block.");
            return null;
        }

        if (!this.leftClick.getWorld().equals(this.rightClick.getWorld())) {
            this.getPlayerFromName().sendMessage("You need to select both points in the same world.");
            return null;
        }

        this.setSelecting(false);

        return new PortalRegion(leftClick, rightClick);
    }

    public void setSelecting(boolean selecting) {
        this.isSelecting = selecting;
    }

    public World getWorldFromName(String worldName) {
        return this.plugin.getServer().getWorld(worldName);
    }
}
