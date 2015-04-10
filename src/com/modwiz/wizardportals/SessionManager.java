package com.modwiz.wizardportals;

import com.modwiz.wizardportals.player.Session;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: sjohnson
 * Date: 1/20/13
 * Time: 11:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class SessionManager {
    private HashMap<String, Session> sessionStore;
    private WizardPortals plugin;

    public SessionManager(WizardPortals plugin) {
        this.plugin = plugin;
        this.sessionStore = new HashMap<String, Session>();
    }

    public Session getSession(Player player) {
        if (sessionStore.containsKey(player.getName())) {
            return sessionStore.get(player.getName());
        }

        Session newPlayerSession = new Session(plugin, player);
        sessionStore.put(player.getName(), newPlayerSession);

        return newPlayerSession;
    }
}
