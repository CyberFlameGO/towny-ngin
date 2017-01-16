package net.wesjd.towny.ngin.town;

import org.bukkit.permissions.Permission;

public class TownPermissions {

    public static final Permission TOWN_ALL = new Permission("towny.town.*", "all permissions"),
            TOWN_WARP_CREATE = new Permission("towny.town.createwarp", "create warps"),
            TOWN_WARP_REMOVE = new Permission("towny.town.removewarp", "remove warps"),
            TOWN_WARP_RENAME = new Permission("towny.town.renamewarp", "rename warps");
}
