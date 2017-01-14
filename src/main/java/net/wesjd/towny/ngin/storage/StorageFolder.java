package net.wesjd.towny.ngin.storage;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;
import net.wesjd.towny.ngin.Towny;
import net.wesjd.towny.ngin.storage.pack.Packer;
import net.wesjd.towny.ngin.storage.pack.PackerStore;
import org.apache.commons.io.FileUtils;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class StorageFolder {

    @Inject
    private Towny _main;

    @Inject
    private PackerStore _packerStore;

    private File _folder;
    private final LoadingCache<Class, List<Field>> _fieldCache = CacheBuilder.newBuilder()
            .expireAfterAccess(1, TimeUnit.MINUTES)
            .build(new CacheLoader<Class, List<Field>>() {
                @Override
                public List<Field> load(Class key) throws Exception {
                    return Arrays.stream(key.getDeclaredFields())
                            .filter(f -> !Modifier.isStatic(f.getModifiers()))
                            .filter(f -> f.isAnnotationPresent(Data.class))
                            .peek(f -> f.setAccessible(true))
                            .collect(Collectors.toList());
                }
            });

    public StorageFolder(String folder) {
        this._folder = new File(_main.getDataFolder(), folder);

        _folder.mkdir();
    }

    public void packup(String name, Object packable) throws PackException {
        try {
            MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();

            List<Field> fields = _fieldCache.get(packable.getClass());
            packer.packArrayHeader(fields.size());

            for (Field field : fields) {
                Packer p = _packerStore.lookup(field.getType()).orElseThrow(() -> new PackException("Unable to find packer for type " + field.getType()));

                try {
                    packer.packString(field.getName());
                    p.packup(field.get(packable), packer);
                } catch (IOException | IllegalAccessException e) {
                    e.printStackTrace();
                }


            }

            packer.close();

            FileUtils.copyToFile(new ByteArrayInputStream(packer.toByteArray()), new File(_folder, name));

        } catch (IOException | ExecutionException e) {
            throw new PackException("Packing " + packable.getClass(), e);
        }

    }

    public void unbox(String name, Object packable) throws PackException {
        try {

            MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(new FileInputStream(name));

            List<Field> fields = _fieldCache.get(packable.getClass());

            int amount = unpacker.unpackArrayHeader();
            for (int i = 0; i < amount; i++) {
                String field = unpacker.unpackString();
                Field f = findField(fields, field).orElseThrow(() -> new PackException("Unable to find field "+field));
                f.set(packable, _packerStore.lookup(f.getType()).orElseThrow(() -> new PackException("Unable to find packer for type " + f.getType())).unbox(unpacker));
            }

        } catch (IOException | ExecutionException | IllegalAccessException e) {
            throw new PackException("Unboxing " + packable.getClass(), e);
        }
    }

    private Optional<Field> findField(List<Field> fields, String name) {
        return fields.stream().filter(f -> f.getName().equals(name)).findFirst();
    }

}
