package net.wesjd.towny.ngin.town;

import org.bukkit.permissions.Permission;

import java.util.List;

public class TownRank {

    private String _name, _display;
    private List<Permission> _permissions;

    public TownRank(String _name, String _display, List<Permission> _permissions) {
        this._name = _name;
        this._display = _display;
        this._permissions = _permissions;
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
}
