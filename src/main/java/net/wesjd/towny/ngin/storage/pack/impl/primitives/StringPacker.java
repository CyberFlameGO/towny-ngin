package net.wesjd.towny.ngin.storage.pack.impl.primitives;

import net.wesjd.towny.ngin.storage.pack.Packer;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

/**
 * Packs a string into a {@link MessagePacker}
 */
public class StringPacker extends Packer<String> {

    @Override
    public void packup(String packing, MessagePacker packer) throws IOException {
        packer.packString(packing);
    }

    @Override
    public String unbox(MessageUnpacker unpacker) throws IOException {
        return unpacker.unpackString();
    }
}
