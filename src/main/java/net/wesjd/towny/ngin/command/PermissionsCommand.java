package net.wesjd.towny.ngin.command;

import com.google.inject.Inject;
import net.wesjd.towny.ngin.command.framework.Commandable;
import net.wesjd.towny.ngin.command.framework.annotation.Command;
import net.wesjd.towny.ngin.command.framework.annotation.Requires;
import net.wesjd.towny.ngin.command.framework.annotation.SubCommand;
import net.wesjd.towny.ngin.command.framework.annotation.parameter.Required;
import net.wesjd.towny.ngin.player.PlayerManager;
import net.wesjd.towny.ngin.player.Rank;
import net.wesjd.towny.ngin.player.TownyPlayer;
import org.bukkit.permissions.Permission;

import java.util.Optional;

import static org.bukkit.ChatColor.*;

/**
 * All commands related to permissions
 */
public class PermissionsCommand implements Commandable {

    /**
     * An injected player manager
     */
    @Inject
    private PlayerManager _playerManager;

    @Command(name = "permissions")
    private void permissionsCommand(TownyPlayer player) {
        player.message(YELLOW + "You currently have the global rank of " + BLUE + player.getRank().toString() + YELLOW + ", which has the following permissions:");
        player.getRank().getPermissions().forEach(permission -> player.message(GOLD + " - " + RED + permission.getName()));
    }

    @SubCommand(of = "permissions", name = "add")
    @Requires(Rank.ADMIN)
    private void addPermissionCommand(TownyPlayer player,
                                     @Required(fail = "Please supply a permission name.") String permission,
                                     @Required(fail = "Please supply a valid rank.") Rank rank) {
        if(rank.getPermissions().stream().noneMatch(perm -> perm.getName().equals(permission))) {
            rank.getPermissions().add(new Permission(permission));
            recalculatePermissionsFor(rank);
            player.message(GREEN + "Added the permission " + YELLOW + permission + GREEN + " to " + YELLOW + rank);
        } else player.message(RED + "That rank already has the permission of " + YELLOW + permission);
    }

    @SubCommand(of = "permissions", name = "remove")
    @Requires(Rank.ADMIN)
    private void removePermissionsCommand(TownyPlayer player,
                                          @Required(fail = "Please supply a permission name.") String permission,
                                          @Required(fail = "Please supply a valid rank.") Rank rank) {
        final Optional<Permission> matching = rank.getPermissions().stream()
                .filter(perm -> perm.getName().equals(permission))
                .findFirst();
        if(matching.isPresent()) {
            rank.getPermissions().remove(matching.get());
            recalculatePermissionsFor(rank);
            player.message(GREEN + "Removed the permission " + YELLOW + permission + GREEN + " from " + YELLOW + rank);
        } else player.message(RED + "That rank doesn't already have the permission of " + YELLOW + permission);
    }

    /**
     * Will recalculate the permissions for all users with a certain rank
     *
     * @param rank The rank to recalculate permissions for
     */
    private void recalculatePermissionsFor(Rank rank) {
        _playerManager.getOnlinePlayers().stream()
                .filter(player -> player.getRank() == rank)
                .forEach(player -> player.setRank(player.getRank()));
    }

}
