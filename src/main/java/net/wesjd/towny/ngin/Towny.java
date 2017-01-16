package net.wesjd.towny.ngin;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import li.l1t.common.intake.CommandsManager;
import li.l1t.common.intake.provider.annotation.Sender;
import net.milkbowl.vault.economy.Economy;
import net.wesjd.towny.ngin.command.PermissionsCommand;
import net.wesjd.towny.ngin.command.TestCommand;
import net.wesjd.towny.ngin.command.TownCommand;
import net.wesjd.towny.ngin.command.WarpCommand;
import net.wesjd.towny.ngin.command.provider.RankProvider;
import net.wesjd.towny.ngin.command.provider.TownyPlayerProvider;
import net.wesjd.towny.ngin.listeners.JoinLeaveListener;
import net.wesjd.towny.ngin.player.PlayerManager;
import net.wesjd.towny.ngin.player.Rank;
import net.wesjd.towny.ngin.player.TownyPlayer;
import net.wesjd.towny.ngin.storage.GStorageModule;
import net.wesjd.towny.ngin.town.TownManager;
import net.wesjd.towny.ngin.util.economy.EconomyInjection;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.Locale;

public class Towny extends JavaPlugin {

    private final Injector _injector = Guice.createInjector(
            new GStorageModule(),
            new AbstractModule() {
                @Override
                protected void configure() {
                    bind(Towny.class).toInstance(Towny.this);

                    bind(PlayerManager.class).in(Singleton.class);
                    bind(TownManager.class).in(Singleton.class);

                    bind(PermissionsCommand.class);
                }
            }
    );

    @Override
    public void onEnable() {
        try {
            getDataFolder().mkdirs();
            new File(getDataFolder(), "permissions").mkdirs();
            registerListeners(JoinLeaveListener.class);

            final CommandsManager commandsManager = new CommandsManager(this);
            commandsManager.getTranslator().setLocale(Locale.ROOT);
            commandsManager.bind(TownyPlayer.class)
                    .annotatedWith(Sender.class)
                    .toProvider(new TownyPlayerProvider(commandsManager.getTranslator(), _injector.getInstance(PlayerManager.class)));
            commandsManager.bind(Rank.class)
                    .toProvider(new RankProvider());
            commandsManager.registerCommand(new TestCommand(), "test");
            commandsManager.registerCommand(new WarpCommand(), "warp");
            commandsManager.registerCommand(_injector.getInstance(TownCommand.class), "town");
            commandsManager.registerCommand(_injector.getInstance(PermissionsCommand.class), "permissions");

            final Plugin vault = getServer().getPluginManager().getPlugin("Vault");
            getServer().getServicesManager().register(Economy.class, _injector.getInstance(EconomyInjection.class), vault, ServicePriority.Normal);
            getLogger().info("Injected custom economy for vault.");

            _injector.getInstance(TownManager.class).loadTowns();
        } catch (Exception ex) {
            ex.printStackTrace();
            Bukkit.shutdown();
        }
    }

    @Override
    public void onDisable() {
        _injector.getInstance(TownManager.class).saveTowns();
        _injector.getInstance(PlayerManager.class).saveLoaded();
    }

    @SafeVarargs
    private final void registerListeners(Class<? extends Listener>... listeners) {
        Arrays.stream(listeners)
                .forEach(listener -> getServer().getPluginManager().registerEvents(_injector.getInstance(listener), this));
    }

    public Injector getInjector() {
        return _injector;
    }

}
