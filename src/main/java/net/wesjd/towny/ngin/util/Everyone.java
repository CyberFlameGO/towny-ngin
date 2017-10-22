package net.wesjd.towny.ngin.util;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.wesjd.towny.ngin.Towny;
import net.wesjd.towny.ngin.player.PlayerManager;
import net.wesjd.towny.ngin.player.TownyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.function.Consumer;

public class Everyone {

    public static void kick(String message) {
        each(player -> player.kickPlayer(message));
        Bukkit.getOnlinePlayers().forEach((player) -> player.kickPlayer(message));
    }

    public static void sendActionBar(String message) {
        final PlayerManager pm = Towny.getPlugin().getInjector().getInstance(PlayerManager.class);
        each(player -> {
            final TownyPlayer p = pm.getPlayer(player);
            if(p != null) p.sendActionBar(message); // player is null when called from PlayerQuitEvent & iterating through players
        });
    }

    public static void message(String message) {
        each(player -> player.sendMessage(message));
    }

    private static void each(Consumer<Player> consumer) {
        Bukkit.getOnlinePlayers().forEach(consumer);
    }

}
