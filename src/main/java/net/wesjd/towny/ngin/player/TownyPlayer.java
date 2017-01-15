package net.wesjd.towny.ngin.player;

import net.wesjd.towny.ngin.storage.StorageFolder;
import org.bukkit.entity.Player;

/**
 * Represents an online towny player
 */
public class TownyPlayer extends OfflineTownyPlayer {

    /**
     * The wrapped online player
     */
    private final Player wrapped;

    /**
     * Creates an online player wrapper
     *
     * @param wrapped The wrapped {@link Player}
     * @param storage The supplied {@link StorageFolder}
     * @param offline The previously loaded {@link OfflineTownyPlayer}
     */
    TownyPlayer(Player wrapped, StorageFolder storage, OfflineTownyPlayer offline) {
        super(storage, offline);
        this.wrapped = wrapped;
    }

    public Player getWrapped() {
        return wrapped;
    }

}
