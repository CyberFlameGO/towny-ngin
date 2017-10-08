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
    private final Player wrapped;
    /**
     * The attachment for the player, used to give their rank permissions
     */
    private final PermissionAttachment attachment;

    /**
     * Creates an online player wrapper
     *
     * @param wrapped The wrapped {@link Player}
     * @param storage The supplied {@link StorageFolder}
     * @param offline The previously loaded {@link OfflineTownyPlayer}
     */
    TownyPlayer(Player wrapped, StorageFolder storage, OfflineTownyPlayer offline) {
        super(storage, offline);
        this.wrapped = wrapped;
        attachment = new PermissionAttachment(Bukkit.getPluginManager().getPlugin("ngin"), this.wrapped);
        setLastKnownName(wrapped.getName());
        setRank(getRank()); //to give the player their permissions
    }

    @Override
    public void setRank(Rank rank) {
        getRank().getPermissions().forEach(attachment::unsetPermission);
        super.setRank(rank);
        rank.getPermissions().forEach(perm -> attachment.setPermission(perm, true));
    }

    /**
     * Send the player a message
     *
     * @param message The message to send the player
     */
    public void message(String message) {
        wrapped.sendMessage(message);
    }

    public Player getWrapped() {
        return wrapped;
    }

}
