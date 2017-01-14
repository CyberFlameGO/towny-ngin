package net.wesjd.towny.ngin.player;

import org.bukkit.entity.Player;

/**
 * Represents an online towny player
 */
public class TownyPlayer extends OfflineTownyPlayer {

    /**
     * The wrapped online player
     */
    private final Player wrapped;

    public TownyPlayer(Player wrapped, OfflineTownyPlayer offline) {
        super(offline);
        this.wrapped = wrapped;
    }

    public TownyPlayer(Player wrapped) {
        this.wrapped = wrapped;
    }

}
