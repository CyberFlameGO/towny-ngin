package net.wesjd.towny.ngin.util;

import net.wesjd.towny.ngin.Towny;
import org.bukkit.Bukkit;

/**
 * Schedules tasks
 */
public class Scheduling {

    /**
     * Run a sync task
     *
     * @param runnable What to run
     */
    public static void sync(Runnable runnable) {
        Bukkit.getScheduler().runTask(Towny.getPlugin(), runnable);
    }

    /**
     * Run a sync task on a timer
     *
     * @param runnable What to run
     * @param wait How long to wait before starting
     * @param period The time between executions
     */
    public static void syncTimer(Runnable runnable, long wait, long period) {
        Bukkit.getScheduler().runTaskTimer(Towny.getPlugin(), runnable, wait, period);
    }

    /**
     * Run a sync task later
     *
     * @param runnable What to run
     * @param wait How long to wait before execution
     */
    public static void syncLater(Runnable runnable, long wait) {
        Bukkit.getScheduler().runTaskLater(Towny.getPlugin(), runnable, wait);
    }

    /**
     * Run an async task
     *
     * @param runnable What to run
     */
    public static void async(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(Towny.getPlugin(), runnable);
    }

    /**
     * Run an async task on a timer
     *
     * @param runnable What to run
     * @param wait How long to wait before starting
     * @param period The time between executions
     */
    public static void asyncTimer(Runnable runnable, long wait, long period) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(Towny.getPlugin(), runnable, wait, period);
    }

    /**
     * Run an async task later
     *
     * @param runnable What to run
     * @param wait How long to wait before execution
     */
    public static void asyncLater(Runnable runnable, long wait) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(Towny.getPlugin(), runnable, wait);
    }

}
