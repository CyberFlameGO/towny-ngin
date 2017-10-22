package net.wesjd.towny.ngin.listeners;

import com.google.inject.Inject;
import net.jodah.expiringmap.ExpiringMap;
import net.wesjd.towny.ngin.player.OfflineTownyPlayer;
import net.wesjd.towny.ngin.player.PlayerManager;
import net.wesjd.towny.ngin.player.Rank;
import net.wesjd.towny.ngin.player.TownyPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Handles the login and quitting of players
 */
public class JoinLeaveListener implements Listener {

    /**
     * The injected {@link PlayerManager}
     */
    @Inject
    private PlayerManager playerManager;

    /**
     * A map of people logging in to their loaded data, which only lasts 30 seconds
     */
    private final ExpiringMap<UUID, OfflineTownyPlayer> joining = ExpiringMap.builder()
            .expiration(30, TimeUnit.SECONDS)
            .build();

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent e) {
        joining.put(e.getUniqueId(), playerManager.createOfflineTownyPlayer(e.getUniqueId()));
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        final Player bukkitPlayer = e.getPlayer();
        if(!joining.containsKey(bukkitPlayer.getUniqueId())) bukkitPlayer.kickPlayer(ChatColor.RED + "Took too long to login.");
        final TownyPlayer player = playerManager.initializePlayer(bukkitPlayer, joining.remove(bukkitPlayer.getUniqueId()));
        //TODO - Donor custom join message
        if(player.getWrapped().getName().equals("WesJD")) player.setRank(Rank.ADMIN);
        e.setJoinMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "> " + ChatColor.WHITE + bukkitPlayer.getName());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        e.setQuitMessage(ChatColor.RED.toString() + ChatColor.BOLD + "< " + ChatColor.WHITE + e.getPlayer().getName());

        playerManager.removePlayer(e.getPlayer());
    }
}
