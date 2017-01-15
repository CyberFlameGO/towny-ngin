package net.wesjd.towny.ngin.town;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.wesjd.towny.ngin.storage.Data;
import net.wesjd.towny.ngin.storage.StorageFolder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TownManager {

    @Inject @Named("towns")
    private StorageFolder _storage;

    @Data
    private Set<Town> _towns = new HashSet<>();

    public void saveTowns() {
        _storage.packup("towns", _towns);
    }

    public void loadTowns() {
        _storage.unbox("towns", _towns);
    }

    public boolean addTown(Town town) {
        return _towns.add(town);
    }

    public Collection<Town> getTowns() {
        return _towns;
    }
}
