package net.wesjd.towny.ngin.util;

import net.wesjd.towny.ngin.Towny;
import net.wesjd.towny.ngin.player.PlayerManager;
import net.wesjd.towny.ngin.player.TownyPlayer;

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
        each(player -> player.getWrapped().kickPlayer(message));
    }

    /**
     * Sends an action bar to all online players
     *
     * @param message The action bar message
     */
    public static void sendActionBar(String message) {
        each(player -> player.sendActionBar(message));
    }

    /**
     * Messages all online players
     *
     * @param message The message to send the players
     */
    public static void message(String message) {
        each(player -> player.message(message));
    }

    /**
     * Iterates through all online players, utility method
     *
     * @param consumer Callback for each iteration
     */
    private static void each(Consumer<TownyPlayer> consumer) {
        final PlayerManager pm = Towny.getPlugin().getInjector().getInstance(PlayerManager.class);
        pm.getOnlinePlayers().forEach(consumer);
    }

}
