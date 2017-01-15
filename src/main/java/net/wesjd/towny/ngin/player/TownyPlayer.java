package net.wesjd.towny.ngin.player;

import net.wesjd.towny.ngin.storage.StorageFolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

/**
 * Represents an online towny player
 */
public class TownyPlayer extends OfflineTownyPlayer {

    /**
     * The wrapped online player
     */
    private final Player _wrapped;
    /**
     * The attachment for the player, used to give their rank permissions
     */
    private final PermissionAttachment _attachment;

    /**
     * Creates an online player wrapper
     *
     * @param wrapped The wrapped {@link Player}
     * @param storage The supplied {@link StorageFolder}
     * @param offline The previously loaded {@link OfflineTownyPlayer}
     */
    TownyPlayer(Player wrapped, StorageFolder storage, OfflineTownyPlayer offline) {
        super(storage, offline);
        _wrapped = wrapped;
        _attachment = new PermissionAttachment(Bukkit.getPluginManager().getPlugin("ngin"), _wrapped);
        setRank(getRank()); //to give the player their permissions
    }

    @Override
    public void setRank(Rank rank) {
        getRank().getPermissions().forEach(_attachment::unsetPermission);
        super.setRank(rank);
        rank.getPermissions().forEach(perm -> _attachment.setPermission(perm, true));
    }

    public Player getWrapped() {
        return _wrapped;
    }

}
