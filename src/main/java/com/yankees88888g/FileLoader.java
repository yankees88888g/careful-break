package com.yankees88888g;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.LevelResource;

import java.io.*;
import java.nio.file.Path;
import java.util.Properties;

public class FileLoader {
    public static boolean loadFile(String savePath) throws IOException {
        File file = new File(savePath);
        if (file.exists() && file.isFile()) {
            FileReader reader = new FileReader(file);
            Properties prop = new Properties();
            prop.load(reader);
            return Boolean.parseBoolean(prop.getProperty("carefulBreak"));
        } else {
            Properties prop = new Properties();
            prop.setProperty("carefulBreak", "true");
            prop.store(new FileOutputStream(savePath), null);
            return true;
        }
    }

    public static void updateFile(boolean updatedSetting, String savePath) {
        Properties prop = new Properties();
        prop.setProperty("carefulBreak", String.valueOf(updatedSetting));
        try {
            prop.store(new FileOutputStream(savePath), null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static String getPath(ServerLevel world) {
        Path worldSavePath;
        MinecraftServer server = world.getServer();
        if (server != null) {
            return "config/carefulBreak.config";
        } else {
            worldSavePath = server.getWorldPath(LevelResource.ROOT).resolve("carefulBreak.config");
        return worldSavePath.toString();
        }
    }
}