package net.wesjd.towny.ngin.listeners;

import com.google.inject.Inject;
import net.wesjd.towny.ngin.Towny;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinLeaveListener implements Listener {

    @Inject
    private Towny main;

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.setJoinMessage("test, the version is " + main.getDescription().getVersion());
    }

}
