package com.modwiz.wizardportals;

import com.modwiz.wizardportals.handlers.EventsHandler;
import com.modwiz.wizardportals.player.Session;
import com.modwiz.wizardportals.storage.Portal;
import com.modwiz.wizardportals.storage.PortalDestination;
import com.modwiz.wizardportals.storage.PortalRegion;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created with IntelliJ IDEA.
 * User: sjohnson
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
                } else if (args[0].equalsIgnoreCase("select")) {
                    Session playerSession = sessionManager.getSession(player);
                    playerSession.setSelecting(true);
                    player.sendMessage(ChatColor.AQUA + "Selection mode enabled");
                    return true;
                } else if (args[0].equalsIgnoreCase("help")) {
                    String helpText = ChatColor.GREEN + "WizardPortals Help\n" +
                            ChatColor.AQUA + "/wp create <name> - Ends a selection and creates a portal with a destination at your current location.\n" +
                            "/wp delete <name> - Deletes a portal.\n" +
                            "/wp select - Turns on selection mode.\n" +
                            "/wp list - List the names of all current portals.";
                    sender.sendMessage(helpText);
                    return true;
                } else if (args[0].equalsIgnoreCase("debug")) {
                    for (Portal i : portalManager.getPortals()) {
                        if (i != null) {
                            sender.sendMessage(ChatColor.DARK_AQUA + i.getInterior().toString());
                        }
                    }
                    sender.sendMessage(ChatColor.DARK_GREEN + "Portal list complete.");
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("create")) {
                    Session playerSession = sessionManager.getSession(player);
                    PortalRegion region = playerSession.getSelectionRegion();

                    if (region != null) {
                        PortalDestination destination = new PortalDestination(player.getLocation());
                        portalManager.addPortal(destination, region, args[1]);
                    }

                    sender.sendMessage(ChatColor.AQUA + "Portal \"" + args[1] + "\" has been created.");
                    sender.sendMessage(ChatColor.DARK_AQUA + "Selection mode disabled.");
                    return true;
                } else if (args[0].equalsIgnoreCase("delete")) {
                    portalManager.removePortal(args[1]);
                    sender.sendMessage(ChatColor.AQUA + "Portal \"" + args[1] + "\" has been deleted.");
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
