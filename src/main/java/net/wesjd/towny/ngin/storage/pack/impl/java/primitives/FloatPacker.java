package net.wesjd.towny.ngin.storage.pack.impl.java.primitives;

import net.wesjd.towny.ngin.storage.pack.Packer;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class FloatPacker extends Packer<Float> {
    @Override
    public void packup(Float packing, MessagePacker packer) throws IOException {
        packer.packFloat(packing);
    }

    @Override
    public Float unbox(MessageUnpacker unpacker) throws IOException {
        return unpacker.unpackFloat();
    }
}
