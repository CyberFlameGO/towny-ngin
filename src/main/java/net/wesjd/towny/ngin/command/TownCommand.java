package net.wesjd.towny.ngin.command;

import com.google.inject.Inject;
import com.sk89q.intake.Command;
import com.sk89q.intake.parametric.annotation.Validate;
import li.l1t.common.intake.provider.annotation.Sender;
import mkremins.fanciful.FancyMessage;
import net.wesjd.towny.ngin.player.PlayerManager;
import net.wesjd.towny.ngin.player.TownyPlayer;
import net.wesjd.towny.ngin.town.Town;
import net.wesjd.towny.ngin.town.TownManager;

import java.text.NumberFormat;
import java.util.Set;

import static org.bukkit.ChatColor.*;

/**
 * Contains all subcommands for the /town command
 */
public class TownCommand {

    /**
     * An injected town manager
     */
    @Inject
    private TownManager _townManager;
    /**
     * An injected player manager
     */
    @Inject
    private PlayerManager _playerManager;

    @Command(aliases = "create", usage = "<town name>", desc = "Creates a new town", min = 1)
    public void createTownCommand(@Sender TownyPlayer player, @Validate(regex = "^(\\w{1,16})$") String name) {
        if (_townManager.getTownSafely(name).isPresent()) {
            player.message(RED + "A town with the name " + name + " already exists towns: "+_townManager.TEMP_removeMe());
        }
        else {
            final Town newTown = _townManager.createTown(name);
            newTown.generateDefaultRanks(player);
            _townManager.addTown(newTown);
            player.setTown(newTown);

            player.message(GREEN + "Created a town with the name of " + YELLOW + name + GREEN + "!");
        }
    }

    @Command(aliases = "info", desc = "Shows your town info")
    public void getTownInfo(@Sender TownyPlayer player) {
        if (player.getTown() == null) player.message(RED + "You aren't a part of any town!");
        else {
            final Town town = player.getTown();
            player.message(YELLOW + "----- [ About " + GREEN + town.getName() + YELLOW + " ] -----");
            player.message(YELLOW + "Your rank: " + RED + town.getTownRankFor(player).getDisplayName());
            player.message(YELLOW + "Town money: " + RED + NumberFormat.getCurrencyInstance().format(town.getMoney()));

            final Set<String> warps = town.getWarps().keySet();
            if(warps.isEmpty()) player.message(YELLOW + "Warps: " + RED + "None");
            else warps.forEach(warp ->
                    new FancyMessage(" - ").color(GOLD)
                    .then(warp).color(RED).tooltip("Click to warp to " + warp).command("/warp " + warp)
                    .send(player.getWrapped()));
        }
    }

}
