package net.wesjd.towny.ngin.player;

import net.wesjd.towny.ngin.storage.Data;
import net.wesjd.towny.ngin.storage.StorageFolder;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Represents and offline towny player
 */
public class OfflineTownyPlayer {

    /**
     * The storage folder for players
     */
    private StorageFolder _storage;

    /**
     * The player's uuid
     */
    private final UUID _uuid;

    /**
     * The amount of money the player has
     */
    @Data
    private long _money = 0;
    /**
     * The player's {@link Rank}
     */
    @Data
    private Rank _rank = Rank.NONE;

    /**
     * The last username the player had when logging into our server
     */
    @Data
    private String _lastName;

    { //new scope runs this code in all constructors
        setRank(_rank); //to load the attachments for online players
    }

    /**
     * Fills this offline player with a previous one (used in {@link TownyPlayer#TownyPlayer(Player, StorageFolder, OfflineTownyPlayer)})
     *
     * @param fill The offline player to fill from
     */
    protected OfflineTownyPlayer(StorageFolder storage, OfflineTownyPlayer fill) {
        _storage = storage;
        _uuid = fill.getUuid();
        _money = fill.getMoney();
        _rank = fill.getRank();
        _lastName = fill.getLastName();
    }

    /**
     * Creates and loads offline player data for this uuid
     *
     * @param uuid The {@link UUID} to load data about
     */
    OfflineTownyPlayer(StorageFolder storage, UUID uuid) {
        _storage = storage;
        _uuid = uuid;
        _storage.unbox(uuid.toString(), this);
    }

    public UUID getUuid() {
        return _uuid;
    }

    public long getMoney() {
        return _money;
    }

    public void setMoney(long money) {
        _money = money;
    }

    public Rank getRank() {
        return _rank;
    }

    public void setRank(Rank rank) {
        _rank = rank;
    }

    public String getLastName() {
        return _lastName;
    }

    public void setLastName(String lastName) {
        _lastName = lastName;
    }

    /**
     * Save the player's data to the file
     */
    public void save() {
        _storage.packup(_uuid.toString(), this);
    }

}
