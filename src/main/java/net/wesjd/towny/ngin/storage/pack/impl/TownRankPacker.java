package net.wesjd.towny.ngin.storage.pack.impl;

import net.wesjd.towny.ngin.storage.pack.Packer;
import net.wesjd.towny.ngin.town.TownRank;
import org.bukkit.permissions.Permission;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TownRankPacker extends Packer<TownRank> {
    @Override
    public void packup(TownRank packing, MessagePacker packer) throws IOException {

        packer.packString(packing.getName());
        packer.packString(packing.getDisplay());

        packer.packArrayHeader(packing.getPermissions().size());
        for (Permission p : packing.getPermissions())
            packer.packString(p.getName());
    }

    @Override
    public TownRank unbox(MessageUnpacker unpacker) throws IOException {

        String name = unpacker.unpackString(), display = unpacker.unpackString();

        int size = unpacker.unpackArrayHeader();

        List<Permission> permissions = new ArrayList<>();

        for (int i = 0; i < size; i++)
            permissions.add(new Permission(unpacker.unpackString()));

        return new TownRank(name, display, permissions);
    }
}
