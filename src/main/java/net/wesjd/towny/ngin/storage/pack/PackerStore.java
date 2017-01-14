package net.wesjd.towny.ngin.storage.pack;

import com.google.inject.Inject;
import net.wesjd.towny.ngin.Towny;
import org.apache.commons.lang.Validate;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Stores all the {@link Packer} implementations
 */
public class PackerStore {

    /**
     * The internal map of Class to {@link Packer}
     * Used for faster lookup times
     */
    private Map<Class, Packer> _packerMap = new HashMap<>();

    /**
     * Looks up all the implementations for {@link Packer} on creation
     */
    @Inject
    public PackerStore(Towny main) {
        Reflections r = new Reflections("net.wesjd.towny.ngin.storage.pack.impl");
        for (Class<? extends Packer> cl : r.getSubTypesOf(Packer.class)) {
            Packer p = main.getInjector().getInstance(cl);
            _packerMap.put(p.getPacking(), p);
        }
    }

    /**
     * Looks up a specified {@link Packer} implementation
     *
     * @param type The type of data being packed
     * @return An {@link Optional<Packer>}, empty if no {@link Packer} found
     */
    public Optional<Packer> lookup(Class type) {
        return Optional.ofNullable(_packerMap.get(type));
    }
}
