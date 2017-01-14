package net.wesjd.towny.ngin.player;

import com.google.inject.Singleton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Where {@link Player}s are mapped to their {@link TownyPlayer} wrapper
 */
@Singleton
public class PlayerManager {

    /**
     * {@link UUID} to {@link TownyPlayer} store
     */
    private final Map<UUID, TownyPlayer> store = new HashMap<>();

    /**
     * Creates a wrapper for a player
     *
     * @param player The {@link Player} to create a wrapper for
     */
    public TownyPlayer initializePlayer(Player player) {
        //TODO - Implement offline player creation cache?
        return store.put(player.getUniqueId(), new TownyPlayer(player));
    }

}
