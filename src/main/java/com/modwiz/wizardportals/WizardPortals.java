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
        // First check that the command is correct
        if (cmd.getName().equalsIgnoreCase("wp")) {
            // Now check which command it is
            if (args.length == 0) {
                sender.sendMessage(ChatColor.AQUA + "Use /wp help for WizardPortals help.");
            } else {
                // Its a list command.
                if (args[0].equalsIgnoreCase("list")) {
                    if (sender.hasPermission("wizardportals.list")) {
                        // Header "Portals List - X portals loaded"
                        sender.sendMessage(ChatColor.BLUE + "Portals List - " + ChatColor.DARK_AQUA + portalManager.getPortals().size() + ChatColor.BLUE + " portals loaded.");

                        // Followed by XXXXX - Destination: (x, y, z, worldName)
                        for (Portal portal : portalManager.getPortals()) {
                            sender.sendMessage(ChatColor.DARK_AQUA + portal.name + ChatColor.BLUE + " - Destination: " + portal.getDestination().toString());
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "You don't have permission to list portals.");
                    }
                } else if (args[0].equalsIgnoreCase("create")) {
                    // Create a new portal
                    if (sender instanceof Player) {
                        Player player = (Player) sender;

                        // Check permissions
                        if (player.hasPermission("wizardportals.create")) {
                            // Get the players session.
                            Session session = sessionManager.getSession(player);

                            // Do they have a selection?
                            if (session.getSelectionRegion() != null) {
                                // Setup the portal name
                                String portalName = args[1];
                                if (portalManager.getPortal(portalName) != null) {
                                    player.sendMessage(ChatColor.RED + "Try again with a different name. That name is already taken.");
                                } else {
                                    session.setPortalName(portalName);
                                    player.sendMessage(ChatColor.BLUE + "Your portal has been created with your current location as the destination.");
                                    player.sendMessage(ChatColor.BLUE + "If you wish to select a new destination use /wp dest <newDestination>.");

                                    PortalDestination dest = new PortalDestination(player.getLocation());
                                    PortalRegion region = session.getSelectionRegion();
                                    portalManager.addPortal(dest, region, portalName);
                                }
                            } else {
                                player.sendMessage(ChatColor.RED + "You need to select both points with a wooden axe first.");
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "You don't have permission to create a portal.");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Only players can create a new portal.");
                    }
                } else if (args[0].equalsIgnoreCase("select")) {
                    // Select a portal to perform operations on.
                    // Will only be able to work with players.
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        // Do they have permission?
                        if (player.hasPermission("wizardportals.select")) {
                            Session session = sessionManager.getSession(player);
                            session.clearSelection();
                            // The portal must exist.
                            if (portalManager.getPortal(args[1]) != null) {
                                session.setPortalName(args[1]);
                                player.sendMessage(ChatColor.BLUE + "Portal " + ChatColor.DARK_AQUA + args[1] + " " + ChatColor.BLUE + "selected.");
                            } else {
                                player.sendMessage(ChatColor.RED + "Invalid portal name, see /wp list for a list of all portals.");
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "You don't have permission to select a portal.");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Sorry only players can select portals.");
                    }
                } else if (args[0].equalsIgnoreCase("dest")) {
                    // Set a destination on a selected portal.
                    // Must be a player to use this command
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        Session session = sessionManager.getSession(player);

                        // check permission
                        if (player.hasPermission("wizardportals.dest")) {
                            if (session.getPortalName() == null) {
                                player.sendMessage(ChatColor.RED + "You must select a portal with /wp select first.");
                            } else {
                                Portal portal = portalManager.getPortal(session.getPortalName());
                                if (portal == null) {
                                    player.sendMessage(ChatColor.RED + "Selected portal invalid: Does not exist!");
                                } else {
                                    if (args[1].startsWith("w:")) {
                                        String worldName = args[1].substring(2);
                                        if (getServer().getWorld(worldName) == null) {
                                            player.sendMessage(ChatColor.RED + "Destination world does not exist.");
                                        } else {
                                            PortalDestination dest = new PortalDestination(worldName);
                                            portal.setDestination(dest);
                                            portalManager.setPortal(portal);
                                            player.sendMessage(ChatColor.BLUE + "Set destination to world " + ChatColor.DARK_AQUA + worldName + ChatColor.BLUE + ".");
                                        }
                                    } else if (args[1].equalsIgnoreCase("here")) {
                                        PortalDestination dest = new PortalDestination(player.getLocation());
                                        portal.setDestination(dest);
                                        portalManager.setPortal(portal);
                                        player.sendMessage(ChatColor.BLUE + "Set destination to your current location.");
                                    } else {
                                        player.sendMessage(ChatColor.RED + "Invalid destination. Valid destinations are here and w:<worldName>");
                                    }
                                }
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "You don't have permission to set the destination of a portal.");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "You have to be a player to use this command.");
                    }
                } else if (args[0].equalsIgnoreCase("delete")) {
                    // Delete command
                    if (sender.hasPermission("wizardportals.delete")) {
                        if (portalManager.getPortal(args[1]) != null) {
                            portalManager.removePortal(args[1]);
                            sender.sendMessage(ChatColor.BLUE + "The portal has been deleted.");
                        } else {
                            sender.sendMessage(ChatColor.RED + "No portal exists by that name. Use /wp list to view a list of all portals.");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "You don't have permission to delete a portal.");
                    }
                } else if (args[0].equalsIgnoreCase("debug")) {
                    // Debug mode toggle
                    if (sender instanceof Player) {
                        Player player = (Player) sender;

                        if (player.hasPermission("wizardportals.debug")) {
                            Session session = sessionManager.getSession(player);
                            session.setDebug(!session.isDebugging());

                            String state = session.isDebugging() ? ChatColor.DARK_AQUA + "on" : ChatColor.RED + "off";
                            player.sendMessage(ChatColor.BLUE + "Debug mode has been toggled " + state + ChatColor.BLUE + ".");
                        } else {
                            player.sendMessage(ChatColor.RED + "You don't have permission to toggle debug mode.");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Only players can toggle debug mode.");
                    }
                } else if (args[0].equalsIgnoreCase("help")) {
                    // Help command, only show commands that the sender has permission to use.

                    if (sender.hasPermission("wizardportals.help")) {
                        sender.sendMessage(ChatColor.BLUE + "/wp help - " + ChatColor.DARK_AQUA + "View commands and their help.");

                        // List portals help
                        if (sender.hasPermission("wizardportals.list")) {
                            sender.sendMessage(ChatColor.BLUE + "/wp list - " + ChatColor.DARK_AQUA + "List all the portals.");
                        }

                        // Create portal help
                        if (sender.hasPermission("wizardportals.create")) {
                            sender.sendMessage(ChatColor.BLUE + "/wp create <portalName> - " + ChatColor.DARK_AQUA + "Create a new portal.");
                        }

                        // Select portal help
                        if (sender.hasPermission("wizardportals.select")) {
                            sender.sendMessage(ChatColor.BLUE + "/wp select <portalName> - " + ChatColor.DARK_AQUA + "Select a portal.");
                        }

                        // Modify destination help
                        if (sender.hasPermission("wizardportals.dest")) {
                            sender.sendMessage(ChatColor.BLUE + "/wp dest <destination> - " + ChatColor.DARK_AQUA + "Modify destination, destinations are w:<worldname> and here.");
                        }

                        // Delete portal help
                        if (sender.hasPermission("wizardportals.delete")) {
                            sender.sendMessage(ChatColor.BLUE + "/wp delete <portalName> - " + ChatColor.DARK_AQUA + "Delete the specified portal.");
                        }

                        // Debug portal help
                        if (sender.hasPermission("wizardportals.debug")) {
                            sender.sendMessage(ChatColor.BLUE + "/wp debug - " + ChatColor.DARK_AQUA + "Toggle debug mode.");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "You don't have permission to view help.");
                    }
                }
            }
        }
        return true;
    }

    public PortalManager getPortalManager() {
        return portalManager;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    /**
     * Is WorldEdit installed and enabled?
     *
     * @return True if WorldEdit is enabled, false otherwise.
     */
    public boolean worldEditSelections() {
        return getServer().getPluginManager().isPluginEnabled("WorldEdit");
    }
}
