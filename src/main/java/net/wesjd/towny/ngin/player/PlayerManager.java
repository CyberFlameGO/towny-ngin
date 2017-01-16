package net.wesjd.towny.ngin.player;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.wesjd.towny.ngin.storage.StorageFolder;
import net.wesjd.towny.ngin.util.UUIDFetcher;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Where {@link Player}s are mapped to their {@link TownyPlayer} wrapper
 */
public class PlayerManager {

    /**
     * The injected {@link StorageFolder} for players
     */
    @Inject @Named("players")
    private StorageFolder _storage;

    /**
     * {@link UUID} to {@link TownyPlayer} store
     */
    private final Map<UUID, TownyPlayer> _store = new HashMap<>();

    /**
     * A simple name to uuid cache
     */
    private final LoadingCache<String, UUID> _nameCache = CacheBuilder.newBuilder()
            .expireAfterAccess(3, TimeUnit.MINUTES)
            .maximumSize(200)
            .build(new CacheLoader<String, UUID>() {
                @Override
                public UUID load(String key) throws Exception {
                    return UUIDFetcher.getUUIDOf(key);
                }
            });

    /**
     * Gets the uuid from a player's name
     *
     * @param name The player's name to get the uuid of
     * @return The player's {@link UUID}
     */
    public UUID getUUIDFor(String name) {
        try {
            return _nameCache.get(name);
        } catch (ExecutionException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Creates a new {@link OfflineTownyPlayer}
     *
     * @param uuid The uuid to create the player from
     * @return The created {@link OfflineTownyPlayer}
     */
    public OfflineTownyPlayer createOfflineTownyPlayer(UUID uuid) {
        return new OfflineTownyPlayer(_storage, uuid);
    }

    /**
     * Creates a wrapper for a player
     *
     * @param player The {@link Player} to create a wrapper for
     */
    public TownyPlayer initializePlayer(Player player, OfflineTownyPlayer offline) {
        Validate.isTrue(!_store.containsKey(offline.getUuid()));
        return _store.put(offline.getUuid(), new TownyPlayer(player, _storage, offline));
    }

    /**
     * Gets an online player's {@link TownyPlayer} wrapper
     *
     * @param player The {@link Player} to get the wrapper for
     * @return The wrapper
     */
    public TownyPlayer getPlayer(Player player) {
        return _store.get(player.getUniqueId());
    }

    /**
     * Gets an online player's {@link TownyPlayer} wrapper
     *
     * @param uuid The {@link UUID} to get the wrapper for
     * @return The wrapper
     */
    public TownyPlayer getPlayer(UUID uuid) {
        return _store.get(uuid);
    }

    /**
     * Removes a player from the server
     *
     * @param player The {@link Player} to remove
     */
    public void removePlayer(Player player) {
        _store.remove(player.getUniqueId()).save();
    }

    /**
     * Get all of the online players
     *
     * @return A collection containing all online players
     */
    public Collection<TownyPlayer> getOnlinePlayers() {
        return Collections.unmodifiableCollection(_store.values());
    }

}
