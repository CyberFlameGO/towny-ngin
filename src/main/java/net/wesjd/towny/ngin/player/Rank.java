package net.wesjd.towny.ngin.player;

import com.google.common.io.Files;
import net.wesjd.towny.ngin.Towny;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.permissions.Permission;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

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
    private final String prefix;
    /**
     * The rank's prefix color
     */
    private final ChatColor color;

    /**
     * Contains all the permissions for this rank
     */
    private final Set<Permission> permissions = new HashSet<>();

    Rank(String prefix, ChatColor color) {
        this.prefix = prefix;
        this.color = color;

        try {
            File permissionsFolder = new File(Towny.getPlugin().getDataFolder(), "permissions");
            if(!permissionsFolder.exists()) permissionsFolder.mkdir();

            final File permissionsFile = new File(permissionsFolder, toString().toLowerCase());
            if (!permissionsFile.exists()) permissionsFile.createNewFile();
            Files.readLines(permissionsFile, StandardCharsets.UTF_8).forEach(line -> permissions.add(new Permission(line)));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getPrefix() {
        return prefix;
    }

    public ChatColor getColor() {
        return color;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

}
