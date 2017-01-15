package net.wesjd.towny.ngin.player;

import com.google.common.io.Files;
import org.apache.commons.io.Charsets;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.permissions.Permission;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents rank values on the server
 */
public enum Rank {

    NONE(null, ChatColor.GRAY),
    PREMIUM("Premium", ChatColor.AQUA),
    MOD("Mod", ChatColor.DARK_PURPLE),
    ADMIN("Admin", ChatColor.RED);

    /**
     * The rank's chat prefix
     */
    private final String _prefix;
    /**
     * The rank's prefix color
     */
    private final ChatColor _color;

    /**
     * Contains all the permissions for this rank
     */
    private final List<Permission> _permissions = new ArrayList<>();

    Rank(String prefix, ChatColor color) {
        this._prefix = prefix;
        this._color = color;

        try {
            final File permissionsFile = new File(Bukkit.getPluginManager().getPlugin("ngin").getDataFolder(), "permissions/" + toString().toLowerCase());
            if (!permissionsFile.exists()) permissionsFile.createNewFile();
            Files.readLines(permissionsFile, Charsets.UTF_8).forEach(line -> _permissions.add(new Permission(line)));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getPrefix() {
        return _prefix;
    }

    public ChatColor getColor() {
        return _color;
    }

    public List<Permission> getPermissions() {
        return _permissions;
    }

}
