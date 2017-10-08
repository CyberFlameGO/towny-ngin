package net.wesjd.towny.ngin.town;

import org.bukkit.permissions.Permission;

import java.util.List;

/**
 * Represents a town rank, customizable in name and permissions
 */
public class TownRank {

    /**
     * The rank's internal name
     */
    private String internalName;
    /**
     * What players will see in chat
     */
    private String displayName;
    /**
     * The permissions this rank has
     */
    private List<Permission> permissions;

    /**
     * Creates a new town rank
     *
     * @param internalName The internal name
     * @param display The name players will see
     * @param permissions The permissions this rank has
     */
    public TownRank(String internalName, String display, List<Permission> permissions) {
        this.internalName = internalName;
        displayName = display;
        this.permissions = permissions;
    }

    public String getInternalName() {
        return internalName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public boolean hasPermission(Permission permission) {
        return permissions.stream().anyMatch(p -> p.getName().equalsIgnoreCase(permission.getName()));
    }

    public boolean isDeletable() {
        return false;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TownRank{");
        sb.append("name='").append(internalName).append('\'');
        sb.append(", display='").append(displayName).append('\'');
        sb.append(", permissions=").append(permissions);
        sb.append('}');
        return sb.toString();
    }
}
