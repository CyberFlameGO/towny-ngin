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
    private String _internalName;
    /**
     * What players will see in chat
     */
    private String _displayName;
    /**
     * The permissions this rank has
     */
    private List<Permission> _permissions;

    /**
     * Creates a new town rank
     *
     * @param internalName The internal name
     * @param display The name players will see
     * @param permissions The permissions this rank has
     */
    public TownRank(String internalName, String display, List<Permission> permissions) {
        _internalName = internalName;
        _displayName = display;
        _permissions = permissions;
    }

    public String getInternalName() {
        return _internalName;
    }

    public String getDisplayName() {
        return _displayName;
    }

    public List<Permission> getPermissions() {
        return _permissions;
    }

    public boolean hasPermission(Permission permission) {
        return _permissions.stream().anyMatch(p -> p.getName().equalsIgnoreCase(permission.getName()));
    }

    public boolean isDeletable() {
        return false;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TownRank{");
        sb.append("_name='").append(_internalName).append('\'');
        sb.append(", _display='").append(_displayName).append('\'');
        sb.append(", _permissions=").append(_permissions);
        sb.append('}');
        return sb.toString();
    }
}
