package net.wesjd.towny.ngin.command.global;

import net.wesjd.towny.ngin.command.framework.Commandable;
import net.wesjd.towny.ngin.command.framework.annotation.Command;
import net.wesjd.towny.ngin.command.framework.annotation.Requires;
import net.wesjd.towny.ngin.command.framework.annotation.SubCommand;
import net.wesjd.towny.ngin.command.framework.annotation.parameter.Required;
import net.wesjd.towny.ngin.player.OfflineTownyPlayer;
import net.wesjd.towny.ngin.player.Rank;
import net.wesjd.towny.ngin.player.TownyPlayer;
import org.bukkit.permissions.Permission;

import java.util.Optional;

import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;
import static org.bukkit.ChatColor.WHITE;
import static org.bukkit.Color.YELLOW;

/**
 * /rank command
 */
public class RankCommand implements Commandable {

    @Command(name = "rank")
    private void rankCommand(TownyPlayer player) {
        final Rank rank = player.getRank();
        player.message(YELLOW + "You currently have the global rank of " + rank.getColor() + rank.toString());
    }

    @SubCommand(of = "rank", name = "set")
    @Requires(Rank.ADMIN)
    private void rankSetCommand(TownyPlayer player,
                                @Required(fail = "Please supply a player name of uuid.") OfflineTownyPlayer target,
                                @Required(fail = "Please supply a valid rank.") Rank rank) {
        target.setRank(rank);
        target.save();
    }

    @SubCommand(of = "rank", name = "perm add")
    @Requires(Rank.ADMIN)
    private void rankPermAddCommand(TownyPlayer player,
                                   @Required(fail = "Please supply a valid rank.") Rank rank,
                                   @Required(fail = "Please supply a permission node.") String node) {
        if(rankHasNode(rank, node)) player.message(RED + "Rank " + formatRank(rank) + RED + " already has that node!");
        else {
            rank.getPermissions().add(new Permission(node));
            rank.recalculatePermissions();
            rank.savePermissions();
            player.message(GREEN + "Added node " + YELLOW + node + GREEN + " to rank " + formatRank(rank) + GREEN + ".");
        }
    }

    @SubCommand(of = "rank", name = "perm remove")
    @Requires(Rank.ADMIN)
    private void rankPermRemoveCommand(TownyPlayer player,
                                       @Required(fail = "Please supply a valid rank.") Rank rank,
                                       @Required(fail = "Please supply a permission node.") String node) {
        if(rankHasNode(rank, node)) {
            rank.getPermissions().remove(getPermission(rank, node).get());
            rank.recalculatePermissions();
            rank.savePermissions();
            player.message(GREEN + "Removed node " + YELLOW + node + GREEN + " from the " + formatRank(rank) + GREEN + ".");
        } else player.message(RED + "Rank " + formatRank(rank) + RED + " doesn't have the node " + YELLOW + node + RED + "!");
    }

    @SubCommand(of = "rank", name = "perm list")
    @Requires(Rank.ADMIN)
    private void rankPermListCommand(TownyPlayer player,
                                     @Required(fail = "Please supply a valid rank.") Rank rank) {
        player.message(YELLOW + "The global rank of " + formatRank(rank) + YELLOW + " has the following permissions:");
        rank.getPermissions().forEach(permission -> player.message(YELLOW + "- " + WHITE + permission.getName()));
    }


    /**
     * Formats a rank into it's stringified name
     *
     * @param rank The rank to format the name of
     * @return The formatted rank name
     */
    private String formatRank(Rank rank) {
        return rank.getColor() + rank.toString();
    }

    /**
     * Whether the specified rank has the node supplied
     *
     * @param rank The rank to check
     * @param node The node to check
     * @return Whether the rank has the node
     */
    private boolean rankHasNode(Rank rank, String node) {
        return getPermission(rank, node).isPresent();
    }

    /**
     * Gets a possible permission that the provided rank has
     *
     * @param rank The rank to get the permission from
     * @param node The node to get
     * @return An {@link Optional} containing a possible permission object
     */
    private Optional<Permission> getPermission(Rank rank, String node) {
        return rank.getPermissions().stream()
                .filter(perm -> perm.getName().equals(node))
                .findFirst();
    }

}
