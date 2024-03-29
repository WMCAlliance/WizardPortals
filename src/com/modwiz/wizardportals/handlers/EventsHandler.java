package com.modwiz.wizardportals.handlers;

import com.modwiz.wizardportals.WizardPortals;
import com.modwiz.wizardportals.player.Session;
import com.modwiz.wizardportals.storage.*;
import com.sk89q.worldedit.WorldEdit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Created with IntelliJ IDEA.
 * User: sjohnson
 * Date: 1/18/13
 * Time: 10:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventsHandler implements Listener {
    private WizardPortals plugin;

    public EventsHandler(WizardPortals plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void playerMoveEvent(PlayerMoveEvent event) {
        Session playerSession = plugin.getSessionManager().getSession(event.getPlayer());

        playerSession.setStaleLocation(event.getPlayer().getLocation());

        if (playerSession.isStale()) {
            return;
        }

        Player player = event.getPlayer();
        Portal possiblePortal = plugin.getPortalManager().getPortal(player);

        if (possiblePortal != null && (hasPermission(possiblePortal.name,player))) {
            if (playerSession.isDebugging()) {
                player.sendMessage(ChatColor.BLUE + "Portal Name: " + ChatColor.DARK_AQUA + possiblePortal.name);
                player.sendMessage(ChatColor.BLUE + "Portal Destination: " + ChatColor.DARK_AQUA + possiblePortal.getDestination().toString());
                player.sendMessage(ChatColor.BLUE + "First Corner: " + ChatColor.DARK_AQUA + possiblePortal.getInterior().leftCorner.toString());
                player.sendMessage(ChatColor.BLUE + "Second Corner: " + ChatColor.DARK_AQUA + possiblePortal.getInterior().rightCorner.toString());
            } else {
				Location destination = possiblePortal.getDestination().toLocation(plugin);
				if (destination.isWorldLoaded()) {
					player.teleport(destination);
				} else {
					player.sendMessage(ChatColor.RED + "The world you are trying to Portal to is currently unavailable.");
				}
            }
        }
    }

    @EventHandler
    public void blockSelectEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Session playerSession = plugin.getSessionManager().getSession(player);

        Material selectionWand = Material.WOODEN_AXE;
        if (plugin.worldEditSelections()) {
            WorldEdit worldEdit = WorldEdit.getInstance();
			selectionWand = Material.matchMaterial(worldEdit.getConfiguration().wandItem);
        }

        if (event.getAction() == Action.LEFT_CLICK_BLOCK && event.getItem() != null && event.getItem().getType() == selectionWand) {
            if (playerSession.setLeftClick(event.getClickedBlock().getLocation())) {
                    event.setCancelled(true);
            }
        }  else if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getItem() != null && event.getItem().getType() == selectionWand) {
            if (playerSession.setRightClick(event.getClickedBlock().getLocation())) {
                event.setCancelled(true);
            }
        }
    }
    
    private boolean hasPermission(String portalName, Player player) {
        boolean all = player.hasPermission("wizardportals.*");
        boolean specific = player.hasPermission("wizardportals.use." + portalName);
        boolean specificAll = player.hasPermission("wizardportals.use.*");
        boolean op = player.isOp();
        return (all || specific || specificAll || op);
    }
}
