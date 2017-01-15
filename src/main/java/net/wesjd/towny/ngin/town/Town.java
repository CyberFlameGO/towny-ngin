package net.wesjd.towny.ngin.town;

import net.wesjd.towny.ngin.storage.Data;
import net.wesjd.towny.ngin.storage.StorageFolder;
import net.wesjd.towny.ngin.util.Region;
import org.bukkit.Location;

import java.util.*;

public class Town {

    private final StorageFolder _storage;

    @Data
    private Location _spawnLocation;

    @Data
    private Region _region;

    @Data
    private double _money;

    @Data
    private Map<String, Location> _warps = new HashMap<>();

    @Data
    private String _townName;


    public Town(String name, StorageFolder storage) {
        _townName = name;
        _storage = storage;
    }

    public Location getSpawnLocation() {
        return _spawnLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        _spawnLocation = spawnLocation;
    }

    public Region getRegion() {
        return _region;
    }

    public void setRegion(Region region) {
        _region = region;
    }

    public double getMoney() {
        return _money;
    }

    public void setMoney(double money) {
        _money = money;
    }

    public Map<String, Location> getWarps() {
        return _warps;
    }

    public String getTownName() {
        return _townName;
    }

    public void setTownName(String townName) {
        _townName = townName;
    }

    public void save() {
        _storage.packup(_townName, this);
    }

    public void load() {
        _storage.unbox(_townName, this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Town town = (Town) o;

        return _townName.equalsIgnoreCase(town._townName);
    }

    @Override
    public int hashCode() {
        return _townName.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Town{");
        sb.append("_spawnLocation=").append(_spawnLocation);
        sb.append(", _region=").append(_region);
        sb.append(", _money=").append(_money);
        sb.append(", _warps=").append(_warps);
        sb.append(", _townName='").append(_townName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
