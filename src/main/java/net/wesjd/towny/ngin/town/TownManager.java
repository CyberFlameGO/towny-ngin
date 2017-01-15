package net.wesjd.towny.ngin.town;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.wesjd.towny.ngin.storage.StorageFolder;

import java.io.File;
import java.util.*;

public class TownManager {

    @Inject @Named("towns")
    private StorageFolder _storage;

    private Set<Town> _towns = new HashSet<>();

    public void saveTowns() {
        _towns.forEach(Town::save);
    }

    public void loadTowns() {
        _towns.clear();
        Arrays.stream(_storage.getAllFiles())
                .map(File::getName)
                .forEach(this::addTown);

        _towns.forEach(Town::load);
    }

    public Town addTown(String name) {
        Town t = new Town(name.toLowerCase(), _storage);
        return _towns.add(t) ? t : null;
    }

    public Optional<Town> getTown(String name) {
        return _towns.stream().filter(t -> t.getTownName().equalsIgnoreCase(name)).findFirst();
    }

    public Collection<Town> getTowns() {
        return _towns;
    }
}
