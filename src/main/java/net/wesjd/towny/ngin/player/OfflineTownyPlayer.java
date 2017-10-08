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
    private final StorageFolder storage;

    /**
     * The player's uuid
     */
    private final UUID uuid;
    /**
     * The player's town object, cached to save on lookup time
     */
    private Town town;

    /**
     * The amount of money the player has
     */
    @Data
    private double money = 0;
    /**
     * The player's {@link Rank}
     */
    @Data
    private Rank rank = Rank.NONE;

    /**
     * The last username the player had when logging into our server
     */
    @Data
    private String lastKnownName;

    /**
     * The current town this player is apart of, only used in to save
     */
    @Data
    private String townName;

    /**
     * Fills this offline player with a previous one (used in {@link TownyPlayer#TownyPlayer(Player, StorageFolder, OfflineTownyPlayer)})
     *
     * @param fill The offline player to fill from
     */
    protected OfflineTownyPlayer(StorageFolder storage, OfflineTownyPlayer fill) {
        this.storage = storage;
        uuid = fill.getUuid();
        town = fill.getTown();
        money = fill.getMoney();
        rank = fill.getRank();
        lastKnownName = fill.getLastKnownName();
        townName = fill.townName;
        town = fill.getTown();
    }

    /**
     * Creates and loads offline player data for this uuid
     *
     * @param uuid The {@link UUID} to load data about
     */
    OfflineTownyPlayer(StorageFolder storage, TownManager townManager, UUID uuid) {
        this.storage = storage;
        this.uuid = uuid;
        this.storage.unbox(uuid.toString(), this);
        town = townManager.getTown(townName);
    }

    public UUID getUuid() {
        return uuid;
    }

    public double getMoney() {
        return money;
    }

    public void removeMoney(double amount) {
        setMoney(getMoney() - amount);
    }

    public void addMoney(double amount) {
        setMoney(getMoney() + amount);
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public Rank getRank() {
        return rank;
    }

    public boolean hasRank(Rank rank) {
        return (this.rank.compareTo(rank) >= 0);
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public String getLastKnownName() {
        return lastKnownName;
    }

    public void setLastKnownName(String lastName) {
        lastKnownName = lastName;
    }

    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
        townName = town.getName();
    }

    /**
     * Save the player's data to the file
     */
    public void save() {
        storage.packup(uuid.toString(), this);
    }

}
