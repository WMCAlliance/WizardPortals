package com.modwiz.wizardportals.handlers;

import com.modwiz.wizardportals.WizardPortals;
import com.modwiz.wizardportals.player.Session;
import com.modwiz.wizardportals.storage.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.Map;

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

        Portal possiblePortal = plugin.getPortalManager().getPortal(event.getPlayer());

        if (possiblePortal != null && (hasPermission(possiblePortal.name,event.getPlayer()))) {
            if (playerSession.isDebugging()) {
                Player player = event.getPlayer();
                player.sendMessage(ChatColor.BLUE + "Portal Name: " + ChatColor.DARK_AQUA + possiblePortal.name);
                player.sendMessage(ChatColor.BLUE + "Portal Destination: " + ChatColor.DARK_AQUA + possiblePortal.getDestination().toString());
                player.sendMessage(ChatColor.BLUE + "First Corner: " + ChatColor.DARK_AQUA + possiblePortal.getInterior().leftCorner.toString());
                player.sendMessage(ChatColor.BLUE + "Second Corner: " + ChatColor.DARK_AQUA + possiblePortal.getInterior().rightCorner.toString());
            } else {
                event.getPlayer().teleport(possiblePortal.getDestination().toLocation(plugin));
            }
        }
    }

    @EventHandler
    public void blockSelectEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Session playerSession = plugin.getSessionManager().getSession(player);

        if (event.getAction() == Action.LEFT_CLICK_BLOCK && event.getItem() != null && event.getItem().getType() == Material.WOOD_AXE) {
            if (playerSession.setLeftClick(event.getClickedBlock().getLocation())) {
                    event.setCancelled(true);
            }
        }  else if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getItem() != null && event.getItem().getType() == Material.WOOD_AXE) {
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
