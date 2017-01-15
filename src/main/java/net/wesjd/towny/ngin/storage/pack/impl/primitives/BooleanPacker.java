package net.wesjd.towny.ngin.storage.pack.impl.primitives;

import net.wesjd.towny.ngin.storage.pack.Packer;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class BooleanPacker extends Packer<Boolean> {
    @Override
    public void packup(Boolean packing, MessagePacker packer) throws IOException {
        packer.packBoolean(packing);
    }

    @Override
    public Boolean unbox(MessageUnpacker unpacker) throws IOException {
        return unpacker.unpackBoolean();
    }
}
