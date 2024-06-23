package com.yankees88888g;

import java.io.*;
import java.nio.file.Path;
import java.util.Properties;

public class FileLoader {
    public static boolean loadFile(Path worldSavePath) throws IOException {
        File file = new File(worldSavePath + "/carefulBreak.config");
        if (file.exists() && file.isFile()) {
            FileReader reader = new FileReader(file);
            Properties prop = new Properties();
            prop.load(reader);
            return Boolean.parseBoolean(prop.getProperty("carefulBreak"));
        } else {
            Properties prop = new Properties();
            prop.setProperty("carefulBreak", "true");
            prop.store(new FileOutputStream(worldSavePath + "/carefulBreak.config"), null);
            return true;
        }
    }

    public static void updateFile(boolean updatedSetting, Path worldSavePath) {
        Properties prop = new Properties();
        prop.setProperty("carefulBreak", String.valueOf(updatedSetting));
        try {
            prop.store(new FileOutputStream(worldSavePath + "/carefulBreak.config"), null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
