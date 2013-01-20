package com.modwiz.wizardportals.handlers;

import com.modwiz.wizardportals.storage.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
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
    private PortalStore portalStore;

    private Map<String, PlayerSelectState> playerState;
    private Map<String, CornerBlocks> selectedCorners;
    private Map<String, String> portalName;


    public EventsHandler(PortalStore portalStore) {
        playerState = new HashMap<String, PlayerSelectState>();
        selectedCorners = new HashMap<String, CornerBlocks>();
        portalName = new HashMap<String, String>();

        this.portalStore = portalStore;
    }
    @EventHandler
    public void playerMoveEvent(PlayerMoveEvent event) {
        if (event.getTo().getWorld().equals(event.getFrom().getWorld())) {
            if (event.getTo().toVector().equals(event.getFrom().toVector())) {
                return;
            }
        }
        Portal possiblePortal = portalStore.getPortal(event.getTo());
        if (possiblePortal != null) {
            event.getPlayer().teleport(possiblePortal.getDestination().toLocation());
        }
    }

    @EventHandler
    public void blockBreakEvent(BlockBreakEvent event) {

        Player player = event.getPlayer();

        PlayerSelectState state = playerState.get(player.getName());

        if (state == null) {
            state = PlayerSelectState.UNSELECTED;
        } else if (state == PlayerSelectState.SELECT_LEFT) {
            CornerBlocks selected = new CornerBlocks();

            selected.leftCorner = event.getBlock().getLocation();

            state = PlayerSelectState.SELECT_RIGHT;

            selectedCorners.put(player.getName(), selected);

            event.setCancelled(true);

        }else if (state == PlayerSelectState.SELECT_RIGHT) {
            CornerBlocks selected = selectedCorners.get(player.getName());

            if (selected == null) {
                event.setCancelled(true);
                return;
            }

            selected.rightCorner = event.getBlock().getLocation();

            state = PlayerSelectState.SELECTED;

            selectedCorners.put(player.getName(), selected);
            event.setCancelled(true);
        }

        if (state == PlayerSelectState.SELECTED) {
            CornerBlocks portalCorners = selectedCorners.get(player.getName());
            String nameOfPortal = portalName.get(player.getName());
            portalStore.addPortal(new PortalDestination(player.getLocation()), portalCorners.leftCorner, portalCorners.rightCorner, nameOfPortal);

            state = PlayerSelectState.UNSELECTED;

        }

        playerState.put(player.getName(), state);

        System.out.println(playerState.get(player.getName()));
        System.out.println(selectedCorners.get(player.getName()));
    }

    public void setState(String playerName, PlayerSelectState state) {
        playerState.put(playerName, state);
    }

    public PlayerSelectState getState(String playerName) {
        PlayerSelectState state = playerState.get(playerName);

        if (state == null) {
            return PlayerSelectState.UNSELECTED;
        }

        return state;
    }

    public CornerBlocks getSelection(String playerName) {
        return selectedCorners.get(playerName);
    }

    public void setPortalName(String player, String portalName) {
        this.portalName.put(player, portalName);
    }

}
