package com.modwiz.wizardportals;

import com.modwiz.wizardportals.handlers.EventsHandler;
import com.modwiz.wizardportals.player.Session;
import com.modwiz.wizardportals.storage.Portal;
import com.modwiz.wizardportals.storage.PortalDestination;
import com.modwiz.wizardportals.storage.PortalRegion;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditPermissionException;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created with IntelliJ IDEA.
 * User: sjohnson      -
 * Date: 1/18/13
 * Time: 10:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class WizardPortals extends JavaPlugin {
    private PortalManager portalManager;
    private SessionManager sessionManager;

    private EventsHandler eventHandling;
    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        portalManager = PortalManager.createDiskStore(this);
        sessionManager = new SessionManager(this);
        eventHandling = new EventsHandler(this);

        getServer().getPluginManager().registerEvents(eventHandling, this);
    }

    @Override
    public void onDisable() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("wp")) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("list")) {
                    for (Portal i : portalManager.getPortals()) {
                        if (i != null) {
                            sender.sendMessage(ChatColor.DARK_AQUA + i.name);
                        }
                    }
                    sender.sendMessage(ChatColor.DARK_GREEN + "Portal list complete.");
                    return true;
                }  else if (args[0].equalsIgnoreCase("help")) {
                    String helpText = ChatColor.GREEN + "WizardPortals Help\n" +
                            ChatColor.AQUA + "/wp create <name> - Ends a selection and creates a portal with a destination at your current location.\n" +
                            "/wp delete <name> - Deletes a portal.\n" +
                            "/wp select - Turns on selection mode.\n" +
                            "/wp list - List the names of all current portals.\n" +
                            "/wp here - Set the destination to your current location\n" +
                            "/wp world <worldname> - Set the destination to that world.";
                    sender.sendMessage(helpText);
                    return true;
                } else if (args[0].equalsIgnoreCase("debug")) {
                    for (Portal i : portalManager.getPortals()) {
                        if (i != null) {
                            sender.sendMessage(ChatColor.DARK_AQUA + String.valueOf(i.getInterior().isInsideDebug(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ(), player.getWorld().getName())));

                        }
                    }
                    sender.sendMessage(ChatColor.DARK_GREEN + "Portal list complete.");
                    return true;
                } else if (args[0].equalsIgnoreCase("here")) {
                    Session playerSession = sessionManager.getSession(player);

                    if (playerSession.isSelectingDestination()) {
                        PortalDestination destination = new PortalDestination(player.getLocation());
                        
                        WorldEditPlugin worldEditPlugin = null;
                        worldEditPlugin = (WorldEditPlugin) getServer().getPluginManager().getPlugin("WorldEdit");
                        Selection sel = worldEditPlugin.getSelection(player);
                    
                    
                        Vector min = sel.getNativeMinimumPoint();
                        Vector max = sel.getNativeMaximumPoint();
                        Location minLoc = new Location(sel.getWorld(), min.getX(), min.getY(), min.getZ());
                        Location maxLoc = new Location(sel.getWorld(), max.getX(), max.getY(), max.getZ());
                        PortalRegion region = new PortalRegion(minLoc, maxLoc);

                        playerSession.setSelectingDestination(false);


                        portalManager.addPortal(destination, region, playerSession.getPortalName());
                        sender.sendMessage(ChatColor.AQUA + "Portal " + playerSession.getPortalName() + " has been created.");
                    } else {
                        sender.sendMessage(ChatColor.DARK_AQUA + "Sorry you are not currently setting a portals destination.");
                    }

                    return true;
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("create")) {
                    Session playerSession = sessionManager.getSession(player);
                    WorldEditPlugin worldEditPlugin = null;
                    worldEditPlugin = (WorldEditPlugin) getServer().getPluginManager().getPlugin("WorldEdit");
                    Selection sel = worldEditPlugin.getSelection(player);
                    
                    
                    if (sel == null) {
                        sender.sendMessage(ChatColor.DARK_AQUA + "You need to select a region for that portal first!");
                    }

                    playerSession.setSelectingDestination(true);
                    playerSession.setSelecting(false);
                    playerSession.setPortalName(args[1]);
                    sender.sendMessage(ChatColor.AQUA + "Portal \"" + args[1] + "\" has almost been created.");
                    sender.sendMessage(ChatColor.AQUA + "Please select destination.");
                    sender.sendMessage(ChatColor.DARK_AQUA + "Selection mode disabled.");


                    return true;
                } else if (args[0].equalsIgnoreCase("delete")) {
                    if (portalManager.getPortal(args[1]) == null) {
                        sender.sendMessage(ChatColor.AQUA + "Sorry that portal does not exist.");
                        return true;
                    }
                    portalManager.removePortal(args[1]);
                    sender.sendMessage(ChatColor.AQUA + "Portal \"" + args[1] + "\" has been deleted.");
                    return true;
                } else if (args[0].equalsIgnoreCase("world")) {
                    Session playerSession = sessionManager.getSession(player);

                    if (getServer().getWorld(args[1]) == null) {
                        sender.sendMessage(ChatColor.DARK_AQUA + "Sorry the destination world must exist.");
                        return true;
                    }
                    if (playerSession.isSelectingDestination()) {
                        PortalDestination destination = new PortalDestination(args[1]);
                        PortalRegion region = playerSession.getSelectionRegion();

                        playerSession.setSelectingDestination(false);
                        portalManager.addPortal(destination, region, playerSession.getPortalName());
                        sender.sendMessage(ChatColor.AQUA + "Portal " + playerSession.getPortalName() + " has been created.");
                    } else {
                        sender.sendMessage(ChatColor.DARK_AQUA + "Sorry you are not currently setting a portals destination.");
                    }
                    return true;
                }
            } else {
                sender.sendMessage(ChatColor.GREEN + "Failure, command does not exist.");
            }
        }
        return false;
    }

    public PortalManager getPortalManager() {
        return portalManager;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
