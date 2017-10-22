package net.wesjd.towny.ngin.util;

import net.wesjd.towny.ngin.Towny;
import net.wesjd.towny.ngin.player.PlayerManager;
import net.wesjd.towny.ngin.player.TownyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

/**
 * Contains utility methods that are directed towards all online players
 */
public class Everyone {

    /**
     * Kicks all online players with a message
     *
     * @param message The message to kick the players with
     */
    public static void kick(String message) {
        each(player -> player.kickPlayer(message));
    }

    /**
     * Sends an action bar to all online players
     *
     * @param message The action bar message
     */
    public static void sendActionBar(String message) {
        final PlayerManager pm = Towny.getPlugin().getInjector().getInstance(PlayerManager.class);
        each(player -> {
            final TownyPlayer p = pm.getPlayer(player);
            if(p != null) p.sendActionBar(message); // player is null when called from PlayerQuitEvent & iterating through players
        });
    }

    /**
     * Messages all online players
     *
     * @param message The message to send the players
     */
    public static void message(String message) {
        each(player -> player.sendMessage(message));
    }

    /**
     * Iterates through all online players, utility method
     *
     * @param consumer Callback for each iteration
     */
    private static void each(Consumer<Player> consumer) {
        Bukkit.getOnlinePlayers().forEach(consumer);
    }

}
