package net.wesjd.towny.ngin.storage.pack.impl.java.primitives;

import net.wesjd.towny.ngin.storage.pack.Packer;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class IntegerPacker extends Packer<Integer> {
    @Override
    public void packup(Integer packing, MessagePacker packer) throws IOException {
        packer.packInt(packing);
    }

    @Override
    public Integer unbox(MessageUnpacker unpacker) throws IOException {
        return unpacker.unpackInt();
    }
}
