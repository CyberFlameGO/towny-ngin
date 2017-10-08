package net.wesjd.towny.ngin.command;

import com.google.inject.Inject;
import li.l1t.common.intake.provider.annotation.Sender;
import mkremins.fanciful.FancyMessage;
import net.wesjd.towny.ngin.command.framework.annotation.Command;
import net.wesjd.towny.ngin.command.framework.annotation.SubCommand;
import net.wesjd.towny.ngin.command.framework.annotation.parameter.Regex;
import net.wesjd.towny.ngin.player.PlayerManager;
import net.wesjd.towny.ngin.player.TownyPlayer;
import net.wesjd.towny.ngin.town.Town;
import net.wesjd.towny.ngin.town.TownManager;
import net.wesjd.towny.ngin.town.TownPermissions;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.permissions.Permission;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bukkit.ChatColor.*;

/**
 * Contains all subcommands for the /town command
 */
public class TownCommand {

    /**
     * Matches only valid names for warps
     */
    private static final Pattern VALID_WARP = Pattern.compile("^(\\w{1,16})$");

    /**
     * An injected town manager
     */
    @Inject
    private TownManager townManager;
    /**
     * An injected player manager
     */
    @Inject
    private PlayerManager playerManager;

    @Command(name = "town")
    public void onTownCommand(TownyPlayer player) {

    }

    @SubCommand(of = "town", name = "create")
    public void handleTown(TownyPlayer player,
                           @Regex(exp = "^(\\w{1,16})$", fail = "Please supply a valid town name, 1-16 characters.") String name) {
        if (townManager.getTownSafely(name).isPresent()) {
            player.message(RED + "A town with the name " + name + " already exists");
        } else {
            final Town newTown = townManager.createTown(name);
            newTown.generateDefaultRanks(player);
            townManager.addTown(newTown);
            player.setTown(newTown);

            player.message(GREEN + "Created a town with the name of " + YELLOW + name + GREEN + "!");
        }
    }

    @SubCommand(of = "town", name = "info")
    public void handleTownInfo(TownyPlayer player) {
        if (player.getTown() == null) player.message(RED + "You aren't a part of any town!");
        else {
            final Town town = player.getTown();
            player.message(YELLOW + "----- [ About " + GREEN + town.getName() + YELLOW + " ] -----");
            player.message(YELLOW + "Your rank: " + RED + town.getTownRankFor(player).getDisplayName());
            player.message(YELLOW + "Town money: " + RED + NumberFormat.getCurrencyInstance().format(town.getMoney()));

            final Set<String> warps = town.getWarps().keySet();
            if (warps.isEmpty()) player.message(YELLOW + "Warps: " + RED + "None");
            else warps.forEach(warp ->
                    new FancyMessage(" - ").color(GOLD)
                            .then(warp).color(RED).tooltip("Click to warp to " + warp).command("/warp " + warp)
                            .send(player.getWrapped()));
        }
    }

    @SubCommand(of = "town", name = "warp")
    public void handleWarp(TownyPlayer player,
                           @Regex(exp = "^(\\w{1,16})$", fail = "Please supply a valid warp name, 1-16 characters.") String warpName) {
        if(player.getTown() == null) player.message(ChatColor.RED + "You aren't apart of any town");
        else {

        }
    }

    @SubCommand(of = "town warp", name = "create")
    public void handleWarpCreate(TownyPlayer player,
                                 @Regex(exp = "^(\\w{1,16})$", fail = "Please supply a valid warp name, 1-16 characters.") String warpName) {
        if(player.getTown() == null) player.message(ChatColor.RED + "You aren't apart of any town");
        else {

        }
    }

    @SubCommand(of = "town warp", name ="remove")
    public void handleWarpRemove(TownyPlayer player,
                                 @Regex(exp = "^(\\w{1,16})$", fail = "Please supply a valid warp name, 1-16 characters.") String warpName) {

    }

    @SubCommand(of = "town warp", name = "rename")
    public void handleWarpRename(TownyPlayer player,
                                 @Regex(exp = "^(\\w{1,16})$", fail = "Please supply a valid warp name, 1-16 characters.") String warpName) {

    }

    /*@Command(aliases = "warp", desc = "Configures town warps")
    public void handleWarp2(@Sender TownyPlayer player, String argsConcated) {
        if (player.getTown() == null) player.message(ChatColor.RED + "You aren't apart of any town");
        else {
            String[] args = argsConcated == null ? new String[0] : argsConcated.split(" ");
            if (args.length == 0) {
                List<FancyMessage> messages = new ArrayList<>();
                messages.add(getFancyFor(player, "create", "Creates a new warp", TownPermissions.TOWN_WARP_CREATE));
                messages.add(getFancyFor(player, "remove", "Removes an existing warp", TownPermissions.TOWN_WARP_REMOVE));
                messages.add(getFancyFor(player, "rename", "Renames an existing warp", TownPermissions.TOWN_WARP_RENAME));

                player.message(ChatColor.YELLOW + "Available warp configuration: " + (messages.isEmpty() ? "None" : ""));
                messages.forEach(f -> f.send(player.getWrapped()));
            } else if(args.length < 2) {
                player.message(ChatColor.RED + "A warp name must be supplied");
            } else {
                String warpName = args[1];

                Matcher m1 = VALID_WARP.matcher(warpName);
                if(!m1.matches()) player.message(ChatColor.RED + "Invalid warp name");
                else {
                    Map<String, Location> warps;
                    switch (args[0].toLowerCase()) {
                        case "create":
                            warps = getWarps(player, TownPermissions.TOWN_WARP_CREATE);
                            if(warps != null) {
                                if(warps.containsKey(warpName.toLowerCase()))
                                    player.message(ChatColor.RED + "Warp " + warpName + " already exists");
                                else {
                                    warps.put(warpName.toLowerCase(), player.getWrapped().getLocation());
                                    player.message(ChatColor.GREEN + "Warp " + warpName + " created");
                                }
                            }
                            break;
                        case "remove":
                            warps = getWarps(player, TownPermissions.TOWN_WARP_REMOVE);
                            if(warps != null) {
                                if (!warps.containsKey(warpName.toLowerCase()))
                                    player.message(ChatColor.RED + "Warp " + warpName + " doesn't exist");
                                else {
                                    warps.remove(warpName.toLowerCase());
                                    player.message(ChatColor.GREEN + "Warp " + warpName + " removed");
                                }
                            }
                            break;
                        case "rename":
                            warps = getWarps(player, TownPermissions.TOWN_WARP_RENAME);
                            if(warps != null) {
                                if (args.length < 3) player.message(ChatColor.RED + "A new warp name must be supplied");
                                else {
                                    String newWarp = args[2];

                                    Matcher m2 = VALID_WARP.matcher(newWarp);
                                    if (!m2.matches()) player.message(ChatColor.RED + "Invalid new warp name");
                                    else {
                                        warps.put(newWarp.toLowerCase(), warps.remove(warpName.toLowerCase()));
                                        player.message(ChatColor.GREEN + "Warp " + warpName + " renamed to " + newWarp);
                                    }
                                }
                            }
                            break;
                        default:
                            player.message(ChatColor.RED + "Invalid warp configuration");
                    }
                }
            }
        }
    }*/

    private FancyMessage getFancyFor(TownyPlayer player, String name, String help, Permission permission) {
        return hasPermission(player, permission) ? new FancyMessage(" - ").color(GOLD)
                .then(name + " : " + help).color(RED) : null;
    }

    private boolean hasPermission(TownyPlayer player, Permission permission) {
        return player.getTown().getTownRankFor(player).hasPermission(permission);
    }

    private Map<String, Location> getWarps(TownyPlayer player, Permission permission) {
        final Town town = player.getTown();
        if (!town.getTownRankFor(player).hasPermission(permission))
            player.message(ChatColor.RED + "You don't have permission to " + permission.getDescription());
        else return town.getWarps();
        return null;
    }

}
