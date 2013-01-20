package com.modwiz.wizardportals;

import com.modwiz.wizardportals.handlers.EventsHandler;
import com.modwiz.wizardportals.handlers.PlayerSelectState;
import com.modwiz.wizardportals.storage.MemoryPortalStore;
import com.modwiz.wizardportals.storage.Portal;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created with IntelliJ IDEA.
 * User: sjohnson
 * Date: 1/18/13
 * Time: 10:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class WizardPortals extends JavaPlugin {
    private MemoryPortalStore tempPortalStore;

    private EventsHandler eventHandling;
    @Override
    public void onEnable() {
        tempPortalStore = new MemoryPortalStore();
        eventHandling = new EventsHandler(tempPortalStore);

        getServer().getPluginManager().registerEvents(eventHandling, this);
    }

    @Override
    public void onDisable() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("wp")) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("list")) {
                    for (Portal i : tempPortalStore.getPortals().values()) {
                        if (i != null) {
                            sender.sendMessage(i.name);
                        }
                    }
                    return true;
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("create")) {
                    eventHandling.setState(sender.getName(), PlayerSelectState.SELECT_LEFT);
                    eventHandling.setPortalName(sender.getName(), args[1]);
                    return true;
                } else if (args[0].equalsIgnoreCase("delete")) {
                    tempPortalStore.deletePortal(args[1]);
                    return true;
                }
            } else {
                sender.sendMessage("Failure, command does not exist.");
            }
        }
        return false;
    }
}
