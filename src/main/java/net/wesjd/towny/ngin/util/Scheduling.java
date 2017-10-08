package net.wesjd.towny.ngin.util;

import net.wesjd.towny.ngin.Towny;
import org.bukkit.Bukkit;

public class Scheduling {

    public static void sync(Runnable runnable) {
        Bukkit.getScheduler().runTask(Towny.getPlugin(), runnable);
    }

    public static void syncTimer(Runnable runnable, long wait, long period) {
        Bukkit.getScheduler().runTaskTimer(Towny.getPlugin(), runnable, wait, period);
    }

    public static void syncLater(Runnable runnable, long wait) {
        Bukkit.getScheduler().runTaskLater(Towny.getPlugin(), runnable, wait);
    }

    public static void async(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(Towny.getPlugin(), runnable);
    }

    public static void asyncLater(Runnable runnable, long wait) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(Towny.getPlugin(), runnable, wait);
    }

    public static void asyncTimer(Runnable runnable, long wait, long period) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(Towny.getPlugin(), runnable, wait, period);
    }

}
