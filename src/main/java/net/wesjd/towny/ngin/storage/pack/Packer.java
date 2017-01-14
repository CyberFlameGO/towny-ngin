package net.wesjd.towny.ngin.storage.pack;

import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;

/**
 * Serializes an object into a {@link MessagePacker}
 *
 * @param <T> The type of object being serialized
 */
public abstract class Packer<T> {

    /**
     * The {@link MessagePacker} to write to
     *
     * @param packing The object you're packing
     * @param packer The {@link MessagePacker} you're storing data in
     */
    public abstract void packup(T packing, MessagePacker packer) throws IOException;

    /**
     * The {@link MessagePacker} to read from
     *
     * @param unpacker The {@link MessagePacker} you stored your data in
     * @return The object you've unpacked
     */
    public abstract T unbox(MessageUnpacker unpacker) throws IOException;

    /**
     * The class of the object
     * you're packing
     *
     * @return The {@link Class<T>} of the object you're packing
     */
    public Class<T> getPacking() {
        //noinspection unchecked
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

}
