package net.wesjd.towny.ngin.listeners;

import net.wesjd.towny.ngin.player.PlayerManager;
import net.wesjd.towny.ngin.player.Rank;
import net.wesjd.towny.ngin.player.TownyPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.inject.Inject;

/**
 * Automatically gives admins their op permissions on login
 */
public class AutoOpListener implements Listener {

    @Inject
    private PlayerManager playerManager;

    /**
     * Gives their perms on login
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        final Player bukkitPlayer = e.getPlayer();
        final TownyPlayer player = playerManager.getPlayer(bukkitPlayer);
        if(player.hasRank(Rank.ADMIN)) bukkitPlayer.setOp(true);
    }

}
