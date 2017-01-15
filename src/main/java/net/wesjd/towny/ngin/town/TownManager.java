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
                .map(this::createTown)
                .forEach(this::addTown);

        _towns.forEach(Town::load);
    }

    public Town createTown(String name) {
        return new Town(name, _storage);
    }

    public void addTown(Town town) {
        _towns.add(town);
    }

    public Optional<Town> getTownSafely(String name) {
        return Optional.ofNullable(getTown(name));
    }

    public Town getTown(String name) {
        return _towns.stream()
                .filter(t -> t.getTownName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    public Collection<Town> getTowns() {
        return _towns;
    }
}
