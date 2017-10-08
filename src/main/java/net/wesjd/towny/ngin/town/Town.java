package net.wesjd.towny.ngin.town;

import net.wesjd.towny.ngin.player.TownyPlayer;
import net.wesjd.towny.ngin.storage.Data;
import net.wesjd.towny.ngin.storage.StorageFolder;
import net.wesjd.towny.ngin.town.ranks.DefaultRank;
import net.wesjd.towny.ngin.town.ranks.OwnerRank;
import net.wesjd.towny.ngin.util.Region;
import org.bukkit.Location;

import java.util.*;

/**
 * Represents a town
 */
public class Town {

    /**
     * An instance of the folder containing all towns
     */
    private final StorageFolder storage;

    /**
     * The spawn location for the town
     */
    @Data
    private Location spawnLocation;

    /**
     * The region specifying the perimeter of the town
     */
    @Data
    private Region region;

    /**
     * The current balance of the town
     */
    @Data
    private double money;

    /**
     * The warps the town has
     */
    @Data
    private Map<String, Location> warps = new HashMap<>();

    /**
     * The map of player to rank
     */
    @Data
    private Map<UUID, String> playerRanks = new HashMap<>();

    /**
     * The name of the town
     */
    @Data
    private String name;

    /**
     * The ranks that the town has
     */
    @Data
    private Set<TownRank> ranks = new HashSet<>();

    /**
     * Creates a new town with the specified name and folder it's located in
     *
     * @param name The name of the town
     * @param storage The folder the town is stored in
     */
    Town(String name, StorageFolder storage) {
        this.name = name;
        this.storage = storage;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public Map<String, Location> getWarps() {
        return warps;
    }

    public String getName() {
        return name;
    }

    public void setName(String townName) {
        name = townName;
    }

    /**
     * Gets a player's town rank
     *
     * @param player The player to find the rank of
     * @return The player's rank
     * @throws RuntimeException If there was some weird error
     */
    public TownRank getTownRankFor(TownyPlayer player) {
        return getRank(playerRanks.get(player.getUuid())).orElseThrow(() -> new RuntimeException("Unable to find player's rank."));
    }

    /**
     * Saves the town to the file
     */
    public void save() {
        storage.packup(name, this);
    }

    /**
     * Loads the town from the file and sets the fields
     */
    public void load() {
        storage.unbox(name, this);
    }

    /**
     * Generates the generic set of ranks all towns have
     */
    public void generateDefaultRanks(TownyPlayer townOwner) {
        ranks.add(new OwnerRank("owner", "Mayor"));
        ranks.add(new DefaultRank("member", "Member", Collections.emptyList()));
        playerRanks.put(townOwner.getUuid(), "owner");
    }

    /**
     *  Gets a rank by its name
     *
     * @param name The name of the rank
     * @return An {@link Optional<TownRank>} empty if no rank found
     */
    public Optional<TownRank> getRank(String name) {
        return ranks.stream()
                .filter(r -> r.getInternalName().equals(name))
                .findFirst();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Town town = (Town) o;

        return name.equalsIgnoreCase(town.name);
    }

    @Override
    public int hashCode() {
        return name.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Town{");
        sb.append("spawnLocation=").append(spawnLocation);
        sb.append(", region=").append(region);
        sb.append(", money=").append(money);
        sb.append(", warps=").append(warps);
        sb.append(", townName='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
