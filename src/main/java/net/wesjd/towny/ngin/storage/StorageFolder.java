package net.wesjd.towny.ngin.storage;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
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

/**
 * Manages the loading and saving of Objects to a specific folder.
 */
public class StorageFolder {

    /**
     * The injected {@link Towny} instance
     */
    private Towny _towny;
    /**
     * The injected {@link PackerStore} containing all {@link Packer} implementations
     */
    private PackerStore _packerStore;
    /**
     * The folder all files are stored in
     */
    private File _folder;
    /**
     * A {@link LoadingCache} containing a {@link List<Field>} for a specified {@link Class}
     */
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

    /**
     * Creates a new {@link StorageFolder}
     * @param towny The main class instance
     * @param packerStore The global store of packers
     * @param folder The folder to store files in
     */
    public StorageFolder(Towny towny, PackerStore packerStore, String folder) {
        _towny = towny;
        _packerStore = packerStore;
        _folder = new File(_towny.getDataFolder(), folder);
        _folder.mkdir();
    }

    /**
     * Reads variables annotated with {@link Data}
     * and saves them to a file specified with
     * the name.
     *
     * @param name Name of the file to save to
     * @param packable The object to save
     * @throws PackException Thrown when unable to write to packer or a packer doesn't exist for a field
     */
    public void packup(String name, Object packable) throws PackException {
        try {
            MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();

            List<Field> fields = _fieldCache.get(packable.getClass());
            packer.packArrayHeader(fields.size());

            for (Field field : fields) {
                Packer p = _packerStore.lookup(field.getType()).orElseThrow(() -> new PackException("Unable to find packer for type " + field.getType()));
                try {
                    packer.packString(field.getName());
                    Object obj = field.get(packable);

                    packer.packBoolean(obj != null); //signifies whether a field has data
                    if(obj != null)
                        p.packup(obj, packer);
                } catch (IOException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            packer.close();

            FileUtils.copyInputStreamToFile(new ByteArrayInputStream(packer.toByteArray()), new File(_folder, name));

        } catch (IOException | ExecutionException e) {
            throw new PackException("Packing " + packable.getClass(), e);
        }
    }

    /**
     * Sets all the variables for a
     * specified object from a file
     *
     * @param name The name of the {@link File} containing the data
     * @param packable The object to set fields for
     * @throws PackException Thrown when the file doesn't exist or there isn't a packer for a field
     */
    public void unbox(String name, Object packable) throws PackException {
        try {
            MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(new FileInputStream(new File(_folder, name)));

            List<Field> fields = _fieldCache.get(packable.getClass());

            int amount = unpacker.unpackArrayHeader();
            for (int i = 0; i < amount; i++) {
                String field = unpacker.unpackString();
                Field f = findField(fields, field)
                        .orElseGet(() -> {
                    _towny.getLogger().warning("Unable to find field "+field+" in "+packable.getClass());
                    return null;
                });
                if (f != null) {
                    if(unpacker.unpackBoolean()) 
                        f.set(packable, _packerStore.lookup(f.getType())
                                .orElseThrow(() -> new PackException("Unable to find packer for type " + f.getType())).unbox(unpacker));
                }
            }

        } catch (IOException | ExecutionException | IllegalAccessException e) {
            throw new PackException("Unboxing " + packable.getClass(), e);
        }
    }

    /**
     * Gets a {@link Field} from a {@link List<Field>} by its name
     *
     * @param fields The {@link List<Field>} to iterate through
     * @param name The name of the field
     * @return An {@link Optional<Field>}, empty if not found
     */
    private Optional<Field> findField(List<Field> fields, String name) {
        return fields.stream().filter(f -> f.getName().equals(name)).findFirst();
    }

}
