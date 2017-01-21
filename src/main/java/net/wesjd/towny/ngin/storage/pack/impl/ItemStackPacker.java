package net.wesjd.towny.ngin.storage.pack.impl;

import net.wesjd.towny.ngin.storage.pack.Packer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class ItemStackPacker extends Packer<ItemStack> {
    @Override
    public void packup(ItemStack packing, MessagePacker packer) throws IOException {
        YamlConfiguration config = new YamlConfiguration();
        config.set("0", packing); //0 used to save space
        packer.packString(config.saveToString());
    }

    @Override
    public ItemStack unbox(MessageUnpacker unpacker) throws IOException {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(unpacker.unpackString());
        } catch (InvalidConfigurationException e) {
            throw new RuntimeException("Unable to unpack ItemStack");
        }
        return config.getItemStack("0");
    }


}
