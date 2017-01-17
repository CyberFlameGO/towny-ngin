package net.wesjd.towny.ngin;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import net.milkbowl.vault.economy.Economy;
import net.wesjd.towny.ngin.command.framework.CommandManager;
import net.wesjd.towny.ngin.listeners.JoinLeaveListener;
import net.wesjd.towny.ngin.player.PlayerManager;
import net.wesjd.towny.ngin.storage.GStorageModule;
import net.wesjd.towny.ngin.util.economy.EconomyInjection;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class Towny extends JavaPlugin {

    private final Injector _injector = Guice.createInjector(
            new GStorageModule(),
            new AbstractModule() {
                @Override
                protected void configure() {
                    bind(Towny.class).toInstance(Towny.this);
                    bind(PlayerManager.class).in(Singleton.class);
                    bind(CommandManager.class).in(Singleton.class);
                }
            }
    );

    @Override
    public void onEnable() {
        try {
            getDataFolder().mkdirs();
            registerListeners(JoinLeaveListener.class);



            final Plugin vault = getServer().getPluginManager().getPlugin("Vault");
            getServer().getServicesManager().register(Economy.class, _injector.getInstance(EconomyInjection.class), vault, ServicePriority.Normal);
            getLogger().info("Injected custom economy for vault.");
        } catch (Exception ex) {
            ex.printStackTrace();
            Bukkit.shutdown();
        }
    }

    @Override
    public void onDisable() {

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
