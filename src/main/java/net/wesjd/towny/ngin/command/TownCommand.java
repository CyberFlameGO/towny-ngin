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

public class TownCommand {

    @Inject
    private TownManager _townManager;

    @Inject
    private PlayerManager _playerManager;

    @Command(aliases = "createtown", usage = "<townname>", desc = "Creates a new town", min = 1)
    public void createTownCommand(@Sender Player player, @Validate(regex = "^(\\w{1,16})$") String name) {
        Town town = _townManager.addTown(name);
        if (town == null) player.sendMessage(ChatColor.RED + "A town with the name " + name + " already exists");
        else {
            town.generateDefaultRanks();
            TownData data = new TownData(town.getTownName(), town.getRankByName("mayor").orElseThrow(() -> new RuntimeException("Unable to find Mayor rank")));
            _playerManager.getPlayer(player).setTownData(data);
            player.sendMessage(ChatColor.GREEN + "Created the " + name + " town!");
        }
    }

    @Command(aliases = "info", desc = "Tells you what town you're apart of and your rank")
    public void getTownInfo(@Sender Player player) {
        TownyPlayer tPlayer = _playerManager.getPlayer(player);
        if (tPlayer.getTownData() == null) player.sendMessage(ChatColor.RED + "You aren't apart of any town!");
        else {
            TownData data = tPlayer.getTownData();
            player.sendMessage(ChatColor.YELLOW + "You are apart of the " + data.getTown() + " town, and you are " + data.getTownRank().getDisplay());
            Town town = _townManager.getTown(data.getTown())
                    .orElseThrow(() -> new RuntimeException("Unable to find town " + data.getTown() + " for player " + player.getName()));
            player.sendMessage(ChatColor.YELLOW + "Your town has $" + town.getMoney() + " and the following warps: " + town.getWarps().keySet());
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
        player.sendMessage(ChatColor.YELLOW + "You are apart of the " + data.getTown() + " town, and you are " + data.getTownRank().getDisplay());
        Town town = _townManager.getTown(data.getTown())
                .orElseThrow(() -> new RuntimeException("Unable to find town " + data.getTown() + " for player " + player.getName()));
        player.sendMessage(ChatColor.YELLOW + "Your town has $" + town.getMoney() + " and the following warps: " + town.getWarps().keySet());
    }
}
