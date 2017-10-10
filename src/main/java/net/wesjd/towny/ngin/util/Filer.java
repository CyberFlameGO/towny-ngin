package net.wesjd.towny.ngin.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

public class Filer {

    public static void writeLines(File file, String... lines) {
        writeLines(file, Arrays.asList(lines));
    }

    public static void writeLines(File file, Collection<String> lines) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for(String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            writer.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
