package net.wesjd.towny.ngin.listeners;

import net.wesjd.towny.ngin.storage.Data;
import net.wesjd.towny.ngin.storage.StorageFolder;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CustomChestListener {

    public static class CustomChest {
        private StorageFolder _folder;

        private Location _location;

        @Data
        private List<ItemStack> _items = new ArrayList<>();

        private CustomChest(Location location, StorageFolder folder) {
            _location = location;
            _folder = folder;
        }

        private void load() {
            _folder.packup(getStorageName(), this);
        }

        private void unload() {
            _folder.unbox(getStorageName(), this);
        }

        private String getStorageName() {
            return _location.getBlockX() + "-" + _location.getBlockY() + "-" + _location.getBlockZ();
        }
    }
}
