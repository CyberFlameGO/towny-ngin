package net.wesjd.towny.ngin.storage.pack.impl.java;

import com.google.inject.Inject;
import net.wesjd.towny.ngin.storage.InheritSuperPacker;
import net.wesjd.towny.ngin.storage.pack.Packer;
import net.wesjd.towny.ngin.storage.pack.PackerStore;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class SetPacker extends Packer<Set> {

    @Inject
    private PackerStore packerStore;

    @Override
    public void packup(Set packing, MessagePacker packer) throws IOException {
        packer.packArrayHeader(packing.size());
        boolean writtenListType = false;
        Packer elementPacker = null;
        for (Object p : packing) {
            if (!writtenListType) {
                writtenListType = true;

                Class cl = p.getClass();
                while (cl.isAnnotationPresent(InheritSuperPacker.class)) cl = cl.getSuperclass();

                elementPacker = packerStore.lookup(cl).orElseThrow(
                        () -> new RuntimeException("Unable to find packer for type " + p.getClass() + " in Set"));

                packer.packString(cl.getName());
            }
            //noinspection unchecked
            elementPacker.packup(p, packer);
        }
    }

    @Override
    public Set unbox(MessageUnpacker unpacker) throws IOException {
        int reading = unpacker.unpackArrayHeader();
        Set<Object> unpacking = new HashSet<>();
        Packer elementUnpacker = null;
        for (int i = 0; i < reading; i++) {
            if (elementUnpacker == null) {
                String read = unpacker.unpackString();
                try {

                    elementUnpacker = packerStore.lookup(Class.forName(read)).orElseThrow(() -> new RuntimeException("Unable to find packer for type " + read));
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException("Unable to find class " + read);
                }
            }
            unpacking.add(elementUnpacker.unbox(unpacker));
        }
        return unpacking;
    }
}
