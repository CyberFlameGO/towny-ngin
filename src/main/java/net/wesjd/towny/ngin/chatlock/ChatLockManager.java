package net.wesjd.towny.ngin.chatlock;

import com.google.inject.Inject;
import net.wesjd.towny.ngin.Towny;
import net.wesjd.towny.ngin.player.PlayerManager;
import net.wesjd.towny.ngin.player.Rank;
import net.wesjd.towny.ngin.player.TownyPlayer;
import net.wesjd.towny.ngin.town.TownManager;
import net.wesjd.towny.ngin.util.Everyone;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.logging.Logger;

/**
 * Manages whether chat is locked or not
 */
public class ChatLockManager implements Listener {

    /**
     * True if chat is locked and no players are able to speak
     */
    private boolean isLocked;

    @Inject
    private PlayerManager playerManager;

    @Inject
    public ChatLockManager(Towny towny) {
        Bukkit.getPluginManager().registerEvents(this, towny);
    }

    public boolean isLocked() {
        return this.isLocked;
    }

    /**
     * Toggles the lock and informs players of the new status
     *
     * @return The new state of the lock
     */
    public boolean toggleLock() {
        this.isLocked = !this.isLocked;
        if (this.isLocked)
            Everyone.sendActionBar(ChatColor.RED + "Chat has been locked.");
        else Everyone.sendActionBar(ChatColor.GREEN + "Chat has been unlocked.");
        return this.isLocked;
    }

    /**
     * Checks to see if any staff remain
     *
     * @return Whether there are staff online
     */
    private boolean hasRemainingStaff() {
        return Bukkit.getOnlinePlayers().stream().anyMatch(p -> {
            final TownyPlayer player = playerManager.getPlayer(p); // player is null if they have just left
            return player != null && player.hasRank(Rank.MOD);
        });
    }


    /**
     * Checks to see if there are no staff and chat is locked,
     * then unlocking to prevent chat being stuck locked
     *
     * @param ev The event that fired
     */
    @EventHandler
    public void onQuit(PlayerQuitEvent ev) {
        if(isLocked && !hasRemainingStaff())
            toggleLock();
    }

    /**
     * Prevents players under the MOD rank to speak
     * when the chat lock is enabled
     *
     * @param ev The event that fired
     */
    @EventHandler
    public void onChat(AsyncPlayerChatEvent ev) {
        final TownyPlayer player = playerManager.getPlayer(ev.getPlayer());
        if (!player.hasRank(Rank.MOD) && isLocked) {
            ev.setCancelled(true);
            player.sendActionBar(ChatColor.RED + "Chat is currently locked!");
        }
    }
}
