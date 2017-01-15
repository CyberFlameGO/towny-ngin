package net.wesjd.towny.ngin;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import net.wesjd.towny.ngin.listeners.JoinLeaveListener;
import net.wesjd.towny.ngin.storage.GStorageModule;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class Towny extends JavaPlugin {

    private final Injector injector = Guice.createInjector(
            new GStorageModule(),
            new AbstractModule() {
                @Override
                protected void configure() {
                    bind(Towny.class).toInstance(Towny.this);
                }
            }
    );

    @Override
    public void onEnable() {
<<<<<<< refs/remotes/origin/player-system
<<<<<<< refs/remotes/origin/player-system
        getDataFolder().mkdirs();


        injector = Guice.createInjector(new TownyModule());
=======
        injector.getInstance(Hello.class);
>>>>>>> Fix NPE, clean some stuff up
=======
>>>>>>> It works!
        registerListeners(JoinLeaveListener.class);
    }

    @Override
    public void onDisable() {

    }

    @SafeVarargs
    private final void registerListeners(Class<? extends Listener>... listeners) {
        Arrays.stream(listeners)
                .forEach(listener -> getServer().getPluginManager().registerEvents(injector.getInstance(listener), this));
    }

    public Injector getInjector() {
        return injector;
    }

}
