package net.wesjd.towny.ngin.player;

import org.bukkit.ChatColor;

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

    Rank(String prefix, ChatColor color) {
        this._prefix = prefix;
        this._color = color;
    }

    public String getPrefix() {
        return _prefix;
    }

    public ChatColor getColor() {
        return _color;
    }

}
