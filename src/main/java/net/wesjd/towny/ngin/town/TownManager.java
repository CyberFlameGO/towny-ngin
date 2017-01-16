package net.wesjd.towny.ngin.town;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.wesjd.towny.ngin.storage.StorageFolder;

import java.io.File;
import java.util.*;

/**
 * Manages all towns
 */
public class TownManager {

    /**
     * The injected storage folder
     */
    @Inject @Named("towns")
    private StorageFolder _storage;

    /**
     * A hashmap of all the current {@link Town}s, used to save on lookup times
     */
    private final Map<String, Town> _towns = new HashMap<>();

    /**
     * Saves all the currently stored towns
     */
    public void saveTowns() {
        _towns.values().forEach(Town::save);
    }

    /**
     * Loads all the stored towns from the towns folder
     */
    public void loadTowns() {
        System.out.println("loading towns");
        _towns.clear();
        Arrays.stream(_storage.getAllFiles())
                .map(File::getName)
                .map(this::createTown)
                .forEach(this::addTown);
        _towns.values().forEach(Town::load);
    }

    /**
     * Creates a town with the specified name
     *
     * @param name The name of the town
     * @return A newly created town
     */
    public Town createTown(String name) {
        return new Town(name.toLowerCase(), _storage);
    }

    /**
     * Adds a {@link Town} to the internal set of towns
     *
     * @param town The {@link Town} to add
     */
    public void addTown(Town town) {
        _towns.put(town.getName(), town);
    }

    /**
     * Gets a {@link Town} and wraps it with an Optional
     *
     * @param name The {@link Town} name
     * @return An {@link Optional<Town>} empty if no town found
     */
    public Optional<Town> getTownSafely(String name) {
        return Optional.ofNullable(getTown(name));
    }

    /**
     * Get a town by its name
     *
     * @param name The name of the town
     * @return A {@link Town} or null if there is no town by that name
     */
    public Town getTown(String name) {
        return _towns.get(name);
    }

    public Collection<Town> getTowns() {
        return _towns.values();
    }

    public Map<String, Town> TEMP_removeMe() {
        return _towns;
    }
}
