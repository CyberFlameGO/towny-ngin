package net.wesjd.towny.ngin.storage.pack.impl.primitives;

import net.wesjd.towny.ngin.storage.pack.Packer;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class ShortPacker extends Packer<Short> {
    @Override
    public void packup(Short packing, MessagePacker packer) throws IOException {
        packer.packShort(packing);
    }

    @Override
    public Short unbox(MessageUnpacker unpacker) throws IOException {
        return unpacker.unpackShort();
    }
}
