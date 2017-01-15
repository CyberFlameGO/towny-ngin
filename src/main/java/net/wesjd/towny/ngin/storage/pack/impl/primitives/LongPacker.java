package net.wesjd.towny.ngin.storage.pack.impl.primitives;

import net.wesjd.towny.ngin.storage.pack.Packer;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class LongPacker extends Packer<Long> {
    @Override
    public void packup(Long packing, MessagePacker packer) throws IOException {
        packer.packLong(packing);
    }

    @Override
    public Long unbox(MessageUnpacker unpacker) throws IOException {
        return unpacker.unpackLong();
    }
}
