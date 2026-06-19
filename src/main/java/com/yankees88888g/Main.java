package com.yankees88888g;

import com.mojang.brigadier.arguments.BoolArgumentType;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.MinecraftServer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import net.minecraft.server.level.ServerLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.commands.Commands;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

import static com.yankees88888g.FileLoader.getPath;
import static net.minecraft.commands.Commands.*;

public class Main implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("modid");

    @Override
    public void onInitialize() {
        /*try {
            carefulBreak = FileLoader.loadFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("carefulBreak")
                    .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                    .then(argument("value", BoolArgumentType.bool())
                            .executes(context -> {
                                boolean result = BoolArgumentType.getBool(context, "value");
                                CommandSourceStack source = context.getSource();
                                ServerLevel world = source.getLevel();
                                if (result) {
                                    source.sendSuccess(() -> Component.literal("Careful Break is set to True."), true);
                                    FileLoader.updateFile(true, getPath(world.getLevel()));
                                } else {
                                    source.sendSuccess(() -> Component.literal("Careful Break is set to False."), true);
                                    FileLoader.updateFile(false, getPath(world.getLevel()));
                                }
                                return 1;
                            })
                    )
            );
        });
    }
}