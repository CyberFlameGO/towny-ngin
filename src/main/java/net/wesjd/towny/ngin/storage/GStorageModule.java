package net.wesjd.towny.ngin.storage;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.wesjd.towny.ngin.Towny;
import net.wesjd.towny.ngin.storage.pack.PackerStore;

public class GStorageModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(PackerStore.class).in(Singleton.class);
    }

    @Provides
    @Named("players")
    @Singleton
    StorageFolder providePlayersStorageFolder(Towny towny, PackerStore store) {
        return new StorageFolder(towny, store, "players");
    }

}
