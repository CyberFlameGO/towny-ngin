package net.wesjd.towny.ngin.storage.pack.impl;

import com.google.inject.Inject;
import net.wesjd.towny.ngin.storage.pack.Packer;
import net.wesjd.towny.ngin.storage.pack.PackerStore;
import net.wesjd.towny.ngin.town.TownData;
import net.wesjd.towny.ngin.town.TownRank;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;
import java.util.ArrayList;

public class TownDataPacker extends Packer<TownData> {

    @Inject
    private PackerStore _packerStore;

    @Override
    public void packup(TownData packing, MessagePacker packer) throws IOException {
        String town = packing.getTown() == null ? "~" : packing.getTown();
        TownRank townRank = packing.getTownRank() == null ? new TownRank("~", "~", new ArrayList<>()) : packing.getTownRank();
        packer.packString(town);
        getRankPacker().packup(townRank, packer);
    }

    @Override
    public TownData unbox(MessageUnpacker unpacker) throws IOException {
        String town = unpacker.unpackString();
        TownRank rank = getRankPacker().unbox(unpacker);
        if(town.equals("~")) town = null;
        if(rank.getName().equals("~")) rank = null;
        return new TownData(town, rank);
    }

    private TownRankPacker getRankPacker() {
        return (TownRankPacker) _packerStore.lookup(TownRank.class).orElseThrow(() -> new RuntimeException("Unable to find packer for TownRank"));
    }
}
