package net.wesjd.towny.ngin;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import net.wesjd.towny.ngin.listeners.JoinLeaveListener;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class Towny extends JavaPlugin {

    private Injector injector;

    @Override
    public void onEnable() {
        getDataFolder().mkdirs();


        injector = Guice.createInjector(new TownyModule());
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

    private class TownyModule extends AbstractModule {

        @Override
        protected void configure() {
            bind(Towny.class).toInstance(Towny.this);
        }

    }

}
