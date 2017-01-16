package net.wesjd.towny.ngin.town.ranks;

import net.wesjd.towny.ngin.town.TownRank;
import org.bukkit.permissions.Permission;
import net.wesjd.towny.ngin.town.Town;

import java.util.List;

/**
 * An non-removable rank that represents the beginning rank in a {@link Town}
 */
public class DefaultRank extends TownRank {
    /**
     * Creates a new default rank
     *
     * @param name        The internal name
     * @param display     The name players will see
     * @param permissions The permissions this rank has
     */
    public DefaultRank(String name, String display, List<Permission> permissions) {
        super(name, display, permissions);
    }

    @Override
    public boolean isDeletable() {
        return false;
    }
}
