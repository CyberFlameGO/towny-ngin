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
    private final String prefix;
    /**
     * The rank's prefix color
     */
    private final ChatColor color;

    Rank(String prefix, ChatColor color) {
        this.prefix = prefix;
        this.color = color;
    }

    /**
     * Gets the rank's chat prefix
     *
     * @return The rank's chat prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Gets the rank's prefix color
     *
     * @return The rank's prefix color
     */
    public ChatColor getColor() {
        return color;
    }

}
