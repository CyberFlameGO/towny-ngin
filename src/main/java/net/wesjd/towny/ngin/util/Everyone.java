package net.wesjd.towny.ngin.util;

import org.bukkit.Bukkit;

public class Everyone {

    public static void kick(String message) {
        Bukkit.getOnlinePlayers().forEach((player) -> player.kickPlayer(message));
    }

}
