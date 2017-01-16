package net.wesjd.towny.ngin.command;

import com.sk89q.intake.Command;
import com.sk89q.intake.parametric.annotation.Optional;
import com.sk89q.intake.parametric.annotation.Validate;
import li.l1t.common.intake.provider.annotation.Sender;
import mkremins.fanciful.FancyMessage;
import net.wesjd.towny.ngin.player.TownyPlayer;
import net.wesjd.towny.ngin.town.Town;
import net.wesjd.towny.ngin.town.TownPermissions;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.permissions.Permission;

import java.util.Map;
import java.util.Set;

import static org.bukkit.ChatColor.*;

public class WarpCommand {

    @Command(aliases = "", desc = "Warps to a warp", min = 1)
    public void warpTo(@Sender TownyPlayer player, @Optional String warpName) {
        if (player.getTown() == null) player.message(ChatColor.RED + "You aren't apart of any town");
        else if (warpName == null) {
            final Town town = player.getTown();
            final Set<String> warps = town.getWarps().keySet();

            if (warps.isEmpty()) player.message(YELLOW + "Warps: " + RED + "None");
            else warps.forEach(warp ->
                    new FancyMessage(" - ").color(GOLD)
                            .then(warp).color(RED).tooltip("Click to warp to " + warp).command("/warp " + warp)
                            .send(player.getWrapped()));
        } else {
            final Location warp = player.getTown().getWarps().get(warpName.toLowerCase());

            if (warp == null) player.message(ChatColor.RED + "Warp " + warpName + " doesn't exist");
            else player.getWrapped().teleport(warp);
        }
    }

    @Command(aliases = "createwarp", desc = "Creates a warp", min = 1, max = 1)
    public void createWarp(@Sender TownyPlayer player, @Validate(regex = "^(\\w{1,16})$") String warpName) {
        final Map<String, Location> warps = getWarps(player, TownPermissions.TOWN_WARP_CREATE);
        if(warps != null) {
            if (warps.containsKey(warpName.toLowerCase()))
                player.message(ChatColor.RED + "Warp " + warpName + " already exists");
            else {
                warps.put(warpName.toLowerCase(), player.getWrapped().getLocation()); //TODO when regions implemented, only allow warps inside town region?
                player.message(ChatColor.YELLOW + "Warp " + warpName + " created");
            }
        }
    }

    @Command(aliases = "removewarp", desc = "Removes a warp", min = 1, max = 1)
    public void removeWarp(@Sender TownyPlayer player, @Validate(regex = "^(\\w{1,16})$") String warpName) {
        final Map<String, Location> warps = getWarps(player, TownPermissions.TOWN_WARP_REMOVE);

        if (warps != null) {
            if (warps.containsKey(warpName.toLowerCase()))
                player.message(ChatColor.RED + "Warp " + warpName + " doesn't exist");
            else {
                warps.remove(warpName.toLowerCase());
                player.message(ChatColor.YELLOW + "Warp " + warpName + " removed");
            }
        }
    }

    @Command(aliases = "renamewarp", desc = "Renames a warp", min = 2, max = 2)
    public void renameWarp(@Sender TownyPlayer player, @Validate(regex = "^(\\w{1,16})$") String initialName, @Validate(regex = "^(\\w{1,16})$") String newName) {
        final Map<String, Location> warps = getWarps(player, TownPermissions.TOWN_WARP_RENAME);

        if (warps != null) {
            if (!warps.containsKey(initialName.toLowerCase()))
                player.message(ChatColor.RED + "Warp " + initialName + " doesn't exist");
            else {
                warps.put(newName.toLowerCase(), warps.remove(initialName.toLowerCase()));
                player.message(ChatColor.YELLOW + "Warp " + initialName + " created");
            }
        }
    }

    private Map<String, Location> getWarps(TownyPlayer player, Permission permission) {
        final Town town = player.getTown();
        if (town == null) player.message(ChatColor.RED + "You aren't apart of any town");
        else {
            if (!town.getTownRankFor(player).hasPermission(permission))
                player.message(ChatColor.RED + "You don't have permission to " + permission.getDescription());
            else return town.getWarps();
        }
        return null;
    }
}
