package net.wesjd.towny.ngin.storage.pack.impl.primitives;

import net.wesjd.towny.ngin.storage.pack.Packer;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class BytePacker extends Packer<Byte> {
    @Override
    public void packup(Byte packing, MessagePacker packer) throws IOException {
        packer.packByte(packing);
    }

    @Override
    public Byte unbox(MessageUnpacker unpacker) throws IOException {
        return unpacker.unpackByte();
    }
}
