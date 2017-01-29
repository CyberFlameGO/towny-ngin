package net.wesjd.towny.ngin.player;

import net.wesjd.towny.ngin.storage.Data;
import net.wesjd.towny.ngin.storage.StorageFolder;
import net.wesjd.towny.ngin.town.Town;
import net.wesjd.towny.ngin.town.TownManager;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Represents and offline towny player
 */
public class OfflineTownyPlayer {

    /**
     * The storage folder for players
     */
    private final StorageFolder _storage;

    /**
     * The player's uuid
     */
    private final UUID _uuid;
    /**
     * The player's town object, cached to save on lookup time
     */
    private Town _town;

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
    private String _lastKnownName;

    /**
     * The current town this player is apart of, only used in to save
     */
    @Data
    private String _townName;

    /**
     * Fills this offline player with a previous one (used in {@link TownyPlayer#TownyPlayer(Player, StorageFolder, OfflineTownyPlayer)})
     *
     * @param fill The offline player to fill from
     */
    protected OfflineTownyPlayer(StorageFolder storage, OfflineTownyPlayer fill) {
        _storage = storage;
        _uuid = fill.getUuid();
        _town = fill.getTown();
        _money = fill.getMoney();
        _rank = fill.getRank();
        _lastKnownName = fill.getLastKnownName();
        _townName = fill._townName;
        _town = fill.getTown();
    }

    /**
     * Creates and loads offline player data for this uuid
     *
     * @param uuid The {@link UUID} to load data about
     */
    OfflineTownyPlayer(StorageFolder storage, TownManager townManager, UUID uuid) {
        _storage = storage;
        _uuid = uuid;
        _storage.unbox(uuid.toString(), this);
        _town = townManager.getTown(_townName);
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

    public String getLastKnownName() {
        return _lastKnownName;
    }

    public void setLastKnownName(String lastName) {
        _lastKnownName = lastName;
    }

    public Town getTown() {
        return _town;
    }

    public void setTown(Town town) {
        _town = town;
        _townName = town.getName();
    }

    /**
     * Save the player's data to the file
     */
    public void save() {
        _storage.packup(_uuid.toString(), this);
    }

}
