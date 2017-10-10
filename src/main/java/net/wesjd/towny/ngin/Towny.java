package net.wesjd.towny.ngin;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import net.milkbowl.vault.economy.Economy;
import net.wesjd.towny.ngin.command.framework.CommandManager;
import net.wesjd.towny.ngin.command.framework.argument.provider.EnumProvider;
import net.wesjd.towny.ngin.command.framework.argument.provider.OfflineTownyPlayerProvider;
import net.wesjd.towny.ngin.command.framework.argument.verifier.RegexVerifier;
import net.wesjd.towny.ngin.command.framework.argument.verifier.RequiredVerifier;
import net.wesjd.towny.ngin.listeners.ChatListener;
import net.wesjd.towny.ngin.listeners.JoinLeaveListener;
import net.wesjd.towny.ngin.player.OfflineTownyPlayer;
import net.wesjd.towny.ngin.player.PlayerManager;
import net.wesjd.towny.ngin.player.Rank;
import net.wesjd.towny.ngin.storage.GStorageModule;
import net.wesjd.towny.ngin.town.TownManager;
import net.wesjd.towny.ngin.updater.UpdateManager;
import net.wesjd.towny.ngin.util.Scheduling;
import net.wesjd.towny.ngin.util.economy.EconomyInjection;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;

public class Towny extends JavaPlugin {

    private final Injector injector = Guice.createInjector(
            new GStorageModule(),
            new AbstractModule() {
                @Override
                protected void configure() {
                    bind(Towny.class).toInstance(Towny.this);

                    bind(PlayerManager.class).in(Singleton.class);
                    bind(CommandManager.class).in(Singleton.class);
                    bind(UpdateManager.class).in(Singleton.class);
                }
            }
    );

    @Override
    public void onEnable() {
        try {
            getDataFolder().mkdirs();

            registerListeners(
                    JoinLeaveListener.class,
                    ChatListener.class
            );

            final UpdateManager updateManager = injector.getInstance(UpdateManager.class);
            updateManager.addJar("/home/customer/towny-ngin/target/ngin-LATEST.jar");
            Scheduling.syncTimer(updateManager::checkForUpdates, 0, 20 * 15);

            final CommandManager commandManager = injector.getInstance(CommandManager.class);
            commandManager.addVerifier(Object.class, new RequiredVerifier());
            commandManager.addVerifier(String.class, new RegexVerifier());
            commandManager.bind(Rank.class).toProvider(new EnumProvider<>());
            commandManager.bind(OfflineTownyPlayer.class).toProvider(injector.getInstance(OfflineTownyPlayerProvider.class));
            commandManager.registerClassesOf("net.wesjd.towny.ngin.command");

            final Plugin vault = getServer().getPluginManager().getPlugin("Vault");
            getServer().getServicesManager().register(Economy.class, injector.getInstance(EconomyInjection.class), vault, ServicePriority.Normal);
            getLogger().info("Injected custom economy for vault.");

            injector.getInstance(TownManager.class).loadTowns();
        } catch (Exception ex) {
            ex.printStackTrace();
            Bukkit.shutdown();
        }
    }

    @Override
    public void onDisable() {
        injector.getInstance(TownManager.class).saveTowns();
        injector.getInstance(PlayerManager.class).saveLoaded();
    }

    @SafeVarargs
    private final void registerListeners(Class<? extends Listener>... listeners) {
        Arrays.stream(listeners)
                .forEach(listener -> getServer().getPluginManager().registerEvents(injector.getInstance(listener), this));
    }

    public Injector getInjector() {
        return injector;
    }

    public static Towny getPlugin() {
        return getPlugin(Towny.class);
    }

}
