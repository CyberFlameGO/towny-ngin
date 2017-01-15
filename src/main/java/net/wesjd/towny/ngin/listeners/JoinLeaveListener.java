package net.wesjd.towny.ngin.listeners;

import com.google.inject.Inject;
import net.jodah.expiringmap.ExpiringMap;
import net.wesjd.towny.ngin.player.OfflineTownyPlayer;
import net.wesjd.towny.ngin.player.PlayerManager;
import net.wesjd.towny.ngin.player.TownyPlayer;
import org.bukkit.ChatColor;
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
    private PlayerManager _playerManager;

    /**
     * A map of people logging in to their loaded data, which only lasts 30 seconds
     */
    private final ExpiringMap<UUID, OfflineTownyPlayer> joining = ExpiringMap.builder()
            .expiration(30, TimeUnit.SECONDS)
            .build();

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent e) {
        joining.put(e.getUniqueId(), _playerManager.createOfflineTownyPlayer(e.getUniqueId()));
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if(!joining.containsKey(e.getPlayer().getUniqueId())) e.getPlayer().kickPlayer(ChatColor.RED + "Took too long to login.");
        final TownyPlayer player = _playerManager.initializePlayer(e.getPlayer(), joining.remove(e.getPlayer().getUniqueId()));
        //TODO - Donor custom join message
        e.setJoinMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "> " + ChatColor.WHITE + e.getPlayer().getName());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        e.setQuitMessage(ChatColor.RED.toString() + ChatColor.BOLD + "< " + ChatColor.WHITE + e.getPlayer().getName());

        _playerManager.removePlayer(e.getPlayer());
    }

}
