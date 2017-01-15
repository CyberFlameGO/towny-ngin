package net.wesjd.towny.ngin.town;

import net.wesjd.towny.ngin.storage.Data;
import net.wesjd.towny.ngin.util.Region;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class Town {

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

    public Town(String name, Location spawnLocation) {
        _townName = name;
        _spawnLocation = spawnLocation;
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
}
