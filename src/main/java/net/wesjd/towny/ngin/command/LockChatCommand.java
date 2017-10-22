package net.wesjd.towny.ngin.command;

import com.google.inject.Inject;
import net.wesjd.towny.ngin.chatlock.ChatLockManager;
import net.wesjd.towny.ngin.command.framework.Commandable;
import net.wesjd.towny.ngin.command.framework.annotation.Command;
import net.wesjd.towny.ngin.command.framework.annotation.Requires;
import net.wesjd.towny.ngin.player.Rank;
import net.wesjd.towny.ngin.player.TownyPlayer;
import org.bukkit.ChatColor;

public class LockChatCommand implements Commandable {

    @Inject
    private ChatLockManager chatLockManager;

    @Command(name = "chatlock")
    @Requires(Rank.ADMIN)
    private void onCommand(TownyPlayer player) {
        final boolean isLocked = chatLockManager.toggleLock();
        player.message((isLocked ? ChatColor.RED : ChatColor.GREEN) + "You have " + (!isLocked ? "un" : "") + "locked chat.");
    }
}
