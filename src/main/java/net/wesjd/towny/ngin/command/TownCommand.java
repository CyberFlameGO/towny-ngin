package net.wesjd.towny.ngin.command;

import com.google.inject.Inject;
import com.sk89q.intake.Command;
import com.sk89q.intake.parametric.annotation.Validate;
import li.l1t.common.intake.provider.annotation.Sender;
import net.wesjd.towny.ngin.player.PlayerManager;
import net.wesjd.towny.ngin.player.TownyPlayer;
import net.wesjd.towny.ngin.town.Town;
import net.wesjd.towny.ngin.town.TownData;
import net.wesjd.towny.ngin.town.TownManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

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
        if (_townManager.getTownSafely(name).isPresent()) player.message(ChatColor.RED + "A town with the name " + name + " already exists");
        else {
            final Town newTown = _townManager.createTown(name);
            newTown.generateDefaultRanks();
            _townManager.addTown(newTown);

            TownData data = new TownData(newTown.getTownName(), newTown.getRankByName("mayor").orElseThrow(() -> new RuntimeException("Unable to find Mayor rank")));
            player.setTownData(data);

            player.message(ChatColor.GREEN + "Created the " + name + " town!");
        }
    }

    @Command(aliases = "info", desc = "Shows your town info")
    public void getTownInfo(@Sender TownyPlayer player) {
        if (player.getTownData() == null) player.message(ChatColor.RED + "You aren't apart of any town!");
        else {
            final TownData data = player.getTownData();
            final Town town = _townManager.getTown(data.getTownName());
            player.message(ChatColor.YELLOW + "----- [ About " + ChatColor.GREEN + data.getTownName() + ChatColor.YELLOW + " ] -----");
            player.message(ChatColor.YELLOW + "Your rank: " + ChatColor.RED + data.getTownRank().getDisplay());
            player.message(ChatColor.YELLOW + "Town money: " + ChatColor.RED + "$" + town.getMoney());
            player.message(ChatColor.YELLOW + "Warps: " + ChatColor.RED + String.join(", ", town.getWarps().keySet()));
        }
    }

    @Command(aliases = "test", desc = "Test saving/load")
    public void testSaveLoad(@Sender Player player) {
        _playerManager.unload();
        _playerManager.initializePlayer(player, _playerManager.createOfflineTownyPlayer(player.getUniqueId()));

        _townManager.saveTowns();
        _townManager.getTowns().clear();

        _townManager.loadTowns();

        TownyPlayer tPlayer = _playerManager.getPlayer(player);
        TownData data = tPlayer.getTownData();
        player.sendMessage(ChatColor.YELLOW + "You are apart of the " + data.getTownName() + " town, and you are " + data.getTownRank().getDisplay());
        Town town = _townManager.getTown(data.getTownName());
        player.sendMessage(ChatColor.YELLOW + "Your town has $" + town.getMoney() + " and the following warps: " + town.getWarps().keySet());
    }
}
