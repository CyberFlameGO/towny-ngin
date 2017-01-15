package net.wesjd.towny.ngin.storage.pack.impl;

import net.wesjd.towny.ngin.storage.pack.Packer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class LocationPacker extends Packer<Location> {
    @Override
    public void packup(Location packing, MessagePacker packer) throws IOException {
        packer.packString(packing.getWorld().getName());
        packer.packDouble(packing.getX());
        packer.packDouble(packing.getY());
        packer.packDouble(packing.getZ());

        packer.packFloat(packing.getYaw());
        packer.packFloat(packing.getPitch());
    }

    @Override
    public Location unbox(MessageUnpacker unpacker) throws IOException {
        World w = Bukkit.getWorld(unpacker.unpackString());
        double x = unpacker.unpackDouble(), y = unpacker.unpackDouble(), z = unpacker.unpackDouble();
        float yaw = unpacker.unpackFloat(), pitch = unpacker.unpackFloat();
        return new Location(w, x, y, z, yaw, pitch);
    }
}
