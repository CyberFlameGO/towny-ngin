package net.wesjd.towny.ngin.player;

import net.wesjd.towny.ngin.storage.Data;
import net.wesjd.towny.ngin.storage.StorageFolder;
import org.bukkit.Bukkit;
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
    private double _money = 0;
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

    public double getMoney() {
        return _money;
    }

    public void removeMoney(double amount) {
        setMoney(getMoney() - amount);
    }

    public void addMoney(double amount) {
        setMoney(getMoney() + amount);
    }

    public void setMoney(double money) {
        _money = money;
    }

    public Rank getRank() {
        return _rank;
    }

    public boolean hasRank(Rank rank) {
        return (_rank.compareTo(rank) >= 0);
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
