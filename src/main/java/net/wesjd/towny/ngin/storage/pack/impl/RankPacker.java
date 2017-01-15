package net.wesjd.towny.ngin.storage.pack.impl;

import net.wesjd.towny.ngin.player.Rank;
import net.wesjd.towny.ngin.storage.pack.Packer;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class RankPacker extends Packer<Rank> {

    @Override
    public void packup(Rank packing, MessagePacker packer) throws IOException {
        packer.packString(packing.toString());
    }

    @Override
    public Rank unbox(MessageUnpacker unpacker) throws IOException {
        return Rank.valueOf(unpacker.unpackString());
    }

}
