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
    private String _name;
    /**
     * What players will see in chat
     */
    private String _display;
    /**
     * The permissions this rank has
     */
    private List<Permission> _permissions;

    /**
     * Creates a new town rank
     *
     * @param name The internal name
     * @param display The name players will see
     * @param permissions The permissions this rank has
     */
    public TownRank(String name, String display, List<Permission> permissions) {
        _name = name;
        _display = display;
        _permissions = permissions;
    }

    public String getName() {
        return _name;
    }

    public String getDisplay() {
        return _display;
    }

    public List<Permission> getPermissions() {
        return _permissions;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TownRank{");
        sb.append("_name='").append(_name).append('\'');
        sb.append(", _display='").append(_display).append('\'');
        sb.append(", _permissions=").append(_permissions);
        sb.append('}');
        return sb.toString();
    }
}
