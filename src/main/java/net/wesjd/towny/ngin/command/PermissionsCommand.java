package net.wesjd.towny.ngin.command;

import com.google.inject.Inject;
import com.sk89q.intake.Command;
import com.sk89q.intake.Require;
import li.l1t.common.intake.provider.annotation.Sender;
import net.wesjd.towny.ngin.player.PlayerManager;
import net.wesjd.towny.ngin.player.Rank;
import net.wesjd.towny.ngin.player.TownyPlayer;
import org.bukkit.permissions.Permission;

import static org.bukkit.ChatColor.*;

/**
 * All commands related to permissions
 */
public class PermissionsCommand {

    /**
     * An injected player manager
     */
    @Inject
    private PlayerManager _playerManager;

    @Command(aliases = "", desc = "Show your permissions")
    public void showPermissions(@Sender TownyPlayer player) {
        player.message(YELLOW + "You currently have the global rank of " + BLUE + player.getRank().toString() + YELLOW + ", which has the following permissions:");
        player.getRank().getPermissions().forEach(permission -> player.message(GOLD + " - " + RED + permission.getName()));
        //TODO - show town permissions too?
    }

    @Command(aliases = "add", desc = "Add a permission to a group", usage = "<permission> <group>", min = 2)
    @Require("towny.permissions")
    public void addPermission(@Sender TownyPlayer player, String permission, Rank group) {
        if(group.getPermissions().add(new Permission(permission))) {
            recalculatePermissionsFor(group);
            player.message(GREEN + "Added the permission " + YELLOW + permission + GREEN + " to " + YELLOW + group);
        } else player.message(RED + "Something went wrong.");
    }

    @Command(aliases = "remove", desc = "Remove a permission from a group", usage = "<permission> <group>", min = 2)
    @Require("towny.permissions")
    public void removePermission(@Sender TownyPlayer player, String permission, Rank group) {
        if(group.getPermissions().remove(new Permission(permission))) {
            recalculatePermissionsFor(group);
            player.message(GREEN + "Added the permission " + YELLOW + permission + GREEN + " to " + YELLOW + group);
        } else player.message(RED + "Something went wrong. Perhaps that group didn't have that permission?");
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
