package net.wesjd.towny.ngin.chat;

import com.google.inject.Inject;
import net.wesjd.towny.ngin.Towny;
import net.wesjd.towny.ngin.player.PlayerManager;
import net.wesjd.towny.ngin.player.Rank;
import net.wesjd.towny.ngin.player.TownyPlayer;
import net.wesjd.towny.ngin.util.Everyone;
import net.wesjd.towny.ngin.util.Scheduling;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;

/**
 * Manages whether chat is locked or not
 */
public class ChatLock implements Listener {

    /**
     * True if chat is locked and no players are able to speak
     */
    private boolean enabled = false;

    @Inject
    private PlayerManager playerManager;

    @Inject
    public ChatLock(Towny towny) {
        Bukkit.getPluginManager().registerEvents(this, towny);
        Scheduling.syncTimer(() -> {
            if(enabled) Everyone.sendActionBar(RED + "Chat is currently locked!");
        }, 0, 20);
    }

    /**
     * Check if it's enabled
     *
     * @return The state
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Toggles the lock and informs players of the new status
     *
     * @return The new state of the lock
     */
    public boolean toggle() {
        enabled = !enabled;

        if (enabled) Everyone.sendActionBar(RED + "Chat has been locked.");
        else Everyone.sendActionBar(GREEN + "Chat has been unlocked.");

        return enabled;
    }

    /**
     * Checks to see if any staff remain
     *
     * @return Whether there are staff online
     */
    private boolean hasRemainingStaff() {
        return playerManager.getOnlinePlayers().stream().anyMatch(p -> p.hasRank(Rank.MOD));
    }

    /**
     * Checks to see if there are no staff and chat is locked,
     * then unlocking to prevent chat being stuck locked
     */
    @EventHandler
    public void onQuit(PlayerQuitEvent ev) {
        if (enabled && !hasRemainingStaff()) toggle();
    }

    /**
     * Prevents players under the MOD rank to speak
     * when the chat lock is enabled
     */
    @EventHandler
    public void onChat(AsyncPlayerChatEvent ev) {
        final TownyPlayer player = playerManager.getPlayer(ev.getPlayer());
        if (enabled && !player.hasRank(Rank.MOD)) {
            ev.setCancelled(true);
            player.message(RED + "Chat is currently locked!");
        }
    }

}
