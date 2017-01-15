package net.wesjd.towny.ngin.storage.pack.impl.java;

import com.google.inject.Inject;
import net.wesjd.towny.ngin.storage.pack.Packer;
import net.wesjd.towny.ngin.storage.pack.PackerStore;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListPacker extends Packer<List> {

    @Inject
    private PackerStore _packerStore;

    @Override
    public void packup(List packing, MessagePacker packer) throws IOException {
        packer.packArrayHeader(packing.size());
        boolean writtenListType = false;
        for (Object p : packing) {
            Packer elementPacker = _packerStore.lookup(p.getClass()).orElseThrow(
                    () -> new RuntimeException("Unable to find packer for type " + p.getClass() + " in List"));
            if (!writtenListType) {
                writtenListType = true;
                packer.packString(p.getClass().getName());
            }
            //noinspection unchecked
            elementPacker.packup(p, packer);
        }
    }

    @Override
    public List unbox(MessageUnpacker unpacker) throws IOException {
        int reading = unpacker.unpackArrayHeader();
        List<Object> unpacking = new ArrayList<>();
        Packer elementUnpacker = null;
        for (int i = 0; i < reading; i++) {
            if (elementUnpacker == null) {
                String read = unpacker.unpackString();
                try {
                    elementUnpacker = _packerStore.lookup(Class.forName(read)).orElseThrow(() -> new RuntimeException("Unable to find packer for type " + read));
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException("Unable to find class " + read);
                }
            }
            unpacking.add(elementUnpacker.unbox(unpacker));
        }
        return unpacking;
    }
}
