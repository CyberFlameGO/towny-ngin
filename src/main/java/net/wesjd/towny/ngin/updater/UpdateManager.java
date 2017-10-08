package net.wesjd.towny.ngin.updater;

import net.wesjd.towny.ngin.util.Everyone;
import net.wesjd.towny.ngin.util.Scheduling;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.bukkit.ChatColor.UNDERLINE;
import static org.bukkit.ChatColor.YELLOW;

/**
 * Handles server jar updates
 */
public class UpdateManager {

    private final File PLUGINS_FOLDER = new File("plugins");
    private final File UPDATE_FOLDER = new File(PLUGINS_FOLDER, "update");

    /**
     * Files to be watched for updates
     */
    private final List<File> files = new ArrayList<>();

    /**
     * Add a jar to be watched for updates
     *
     * @param saveLocation The raw location of the jar (where it will change upon updates)
     */
    public void addJar(String saveLocation) {
        files.add(new File(saveLocation));
    }

    /**
     * Checks for available jar updates
     */
    public void checkForUpdates() {
        try {
            boolean reboot = false;

            for(File file : files) {
                final File localJar = new File(PLUGINS_FOLDER, file.getName());

                File destinationFolder;
                if(localJar.exists()) {
                    if(getMd5Hex(file).equals(getMd5Hex(localJar))) continue;

                    destinationFolder = UPDATE_FOLDER;
                    reboot = true;
                } else destinationFolder = PLUGINS_FOLDER;


                FileUtils.copyFile(file, destinationFolder);
            }

            if(reboot) {
                Bukkit.broadcastMessage(YELLOW.toString() + UNDERLINE + "Server is restarting for an update!");
                Scheduling.syncLater(() -> {
                    Everyone.kick(YELLOW + "Server is updating.");
                    Bukkit.shutdown();
                }, 20 * 3);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Gets the md5 hex for a file
     *
     * @param file The file to get the md5 hex for
     * @return The file's md5 hex
     */
    private String getMd5Hex(File file) {
        try(FileInputStream fileInputStream = new FileInputStream(file)) {
            return DigestUtils.md5Hex(fileInputStream);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
