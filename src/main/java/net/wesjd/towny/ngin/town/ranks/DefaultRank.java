package net.wesjd.towny.ngin.town.ranks;

import net.wesjd.towny.ngin.storage.InheritSuperPacker;
import net.wesjd.towny.ngin.town.TownRank;
import org.bukkit.permissions.Permission;
import net.wesjd.towny.ngin.town.Town;

import java.util.List;

/**
 * An non-removable rank that represents the beginning rank in a {@link Town}
 */
@InheritSuperPacker
public class DefaultRank extends TownRank {
    /**
     * Creates a new default rank
     *
     * @param internalName        The internal name
     * @param displayName     The name players will see
     * @param permissions The permissions this rank has
     */
    public DefaultRank(String internalName, String displayName, List<Permission> permissions) {
        super(internalName, displayName, permissions);
    }

    @Override
    public boolean isDeletable() {
        return false;
    }
}
