package net.wesjd.towny.ngin.storage.pack.impl.java;

import net.wesjd.towny.ngin.storage.pack.Packer;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;
import java.util.UUID;

public class UUIDPacker extends Packer<UUID> {
    @Override
    public void packup(UUID packing, MessagePacker packer) throws IOException {
        packer.packString(packing.toString());
    }

    @Override
    public UUID unbox(MessageUnpacker unpacker) throws IOException {
        return UUID.fromString(unpacker.unpackString());
    }
}
