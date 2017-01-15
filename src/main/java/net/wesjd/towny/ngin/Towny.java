package net.wesjd.towny.ngin;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import net.wesjd.towny.ngin.listeners.JoinLeaveListener;
import net.wesjd.towny.ngin.player.PlayerManager;
import net.wesjd.towny.ngin.storage.GStorageModule;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;

public class Towny extends JavaPlugin {

    private final Injector _injector = Guice.createInjector(
            new GStorageModule(),
            new AbstractModule() {
                @Override
                protected void configure() {
                    bind(Towny.class).toInstance(Towny.this);
                    bind(PlayerManager.class).in(Singleton.class);
                }
            }
    );

    @Override
    public void onEnable() {
        getDataFolder().mkdirs();
        new File(getDataFolder(), "permissions").mkdirs();
        registerListeners(JoinLeaveListener.class);
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
