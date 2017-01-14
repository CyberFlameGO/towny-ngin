package net.wesjd.towny.ngin;

import com.google.inject.*;
import com.google.inject.name.Named;
import net.wesjd.towny.ngin.listeners.JoinLeaveListener;
import net.wesjd.towny.ngin.storage.GStorageModule;
import net.wesjd.towny.ngin.storage.StorageFolder;
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

    static class Hello {
        @Inject
        @Named("players")
        private StorageFolder folder;
    }

    @Override
    public void onEnable() {
        injector.getInstance(Hello.class);
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
