package net.wesjd.towny.ngin.command;

import com.sk89q.intake.Command;
import li.l1t.common.intake.provider.annotation.ItemInHand;
import li.l1t.common.intake.provider.annotation.Sender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TestCommand {

    @Command(aliases = "",
            desc = "just testing")
    public void testCommand(@Sender Player sender, @ItemInHand ItemStack hand) {
        sender.sendMessage("yo nice " + hand.getType());
    }

}
