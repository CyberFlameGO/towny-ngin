package net.wesjd.towny.ngin.storage.pack.impl.primitives;

import net.wesjd.towny.ngin.storage.pack.Packer;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class DoublePacker extends Packer<Double> {
    @Override
    public void packup(Double packing, MessagePacker packer) throws IOException {
        packer.packDouble(packing);
    }

    @Override
    public Double unbox(MessageUnpacker unpacker) throws IOException {
        return unpacker.unpackDouble();
    }
}
