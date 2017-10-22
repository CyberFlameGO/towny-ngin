package net.wesjd.towny.ngin.chat;

import net.wesjd.towny.ngin.player.PlayerManager;
import net.wesjd.towny.ngin.player.Rank;
import net.wesjd.towny.ngin.player.TownyPlayer;
import net.wesjd.towny.ngin.util.Everyone;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import javax.inject.Inject;

import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.RESET;

/**
 * Handles ingame chat
 */
public class FormatListener implements Listener {

    @Inject
    private PlayerManager playerManager;

    /**
     * Formats chat
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent e) {
        final TownyPlayer player = playerManager.getPlayer(e.getPlayer());
        final Rank rank = player.getRank();
        Everyone.message(rank.getColor() + rank.getPrefix() + GRAY + ": " + RESET + e.getMessage());

        e.setCancelled(true);
    }

}
