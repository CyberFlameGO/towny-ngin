package net.wesjd.towny.ngin.town.ranks;

import org.bukkit.permissions.Permission;

import java.util.Collections;

/**
 * An non-removable rank that has all permissions for towns
 */
public class OwnerRank extends DefaultRank {

    /**
     * Creates a new owner rank
     *
     * @param name        The internal name
     * @param display     The name players will see
     */
    public OwnerRank(String name, String display) {
        super(name, display, Collections.singletonList(new Permission("towny.owner")));
    }
}
