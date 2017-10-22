package net.wesjd.towny.ngin.player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import net.wesjd.towny.ngin.storage.StorageFolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.lang.reflect.InvocationTargetException;

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
     * Get their display name
     *
     * @return Their display name
     */
    public String getName() {
        return getLastKnownName();
    }

    /**
     * Send the player a message
     *
     * @param message The message to send the player
     */
    public void message(String message) {
        wrapped.sendMessage(message);
    }

    /**
     * Sends an action bar to the wrapped player
     *
     * @param message The action bar to send
     */
    public void sendActionBar(String message) {

        try {
            final PacketContainer chat = new PacketContainer(PacketType.Play.Server.CHAT);

            chat.getChatComponents().write(0, WrappedChatComponent.fromText(message));
            chat.getBytes().write(0, (byte) 2);

            ProtocolLibrary.getProtocolManager().sendServerPacket(wrapped, chat);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Failed to send action bar packet", e);
        }
    }

    public Player getWrapped() {
        return wrapped;
    }

}
