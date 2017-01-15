package net.wesjd.towny.ngin.storage.pack.impl;

import com.google.inject.Inject;
import net.wesjd.towny.ngin.storage.pack.Packer;
import net.wesjd.towny.ngin.storage.pack.PackerStore;
import net.wesjd.towny.ngin.util.Region;
import org.bukkit.Location;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class RegionPacker extends Packer<Region> {

    @Inject
    private PackerStore _packerStore;

    @Override
    public void packup(Region packing, MessagePacker packer) throws IOException {
        LocationPacker locationPacker = getLocationPacker();

        locationPacker.packup(packing.getPos1(), packer);
        locationPacker.packup(packing.getPos2(), packer);
    }

    @Override
    public Region unbox(MessageUnpacker unpacker) throws IOException {
        LocationPacker locationPacker = getLocationPacker();

        return new Region(locationPacker.unbox(unpacker), locationPacker.unbox(unpacker));
    }

    private LocationPacker getLocationPacker() {
        return (LocationPacker) _packerStore.lookup(Location.class).orElseThrow(() -> new RuntimeException("Unable to find packer for Location"));
    }
}
