package net.wesjd.towny.ngin.storage.pack.impl;

import net.wesjd.towny.ngin.storage.pack.Packer;
import net.wesjd.towny.ngin.town.ranks.DefaultRank;
import net.wesjd.towny.ngin.town.ranks.OwnerRank;
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

        packer.packString(packing.getInternalName());
        packer.packString(packing.getDisplayName());
        packer.packByte((byte) (packing instanceof OwnerRank ? 1 : packing instanceof DefaultRank ? 2 : 0));

        packer.packArrayHeader(packing.getPermissions().size());
        for (Permission p : packing.getPermissions())
            packer.packString(p.getName());
    }

    @Override
    public TownRank unbox(MessageUnpacker unpacker) throws IOException {

        String name = unpacker.unpackString(), display = unpacker.unpackString();
        byte rankType = unpacker.unpackByte();

        int size = unpacker.unpackArrayHeader();

        List<Permission> permissions = new ArrayList<>();

        for (int i = 0; i < size; i++)
            permissions.add(new Permission(unpacker.unpackString()));

        switch (rankType) {
            case 1:
                return new OwnerRank(name, display);
            case 2:
                return new DefaultRank(name, display, permissions);
            default:
                return new TownRank(name, display, permissions);
        }
    }
}
