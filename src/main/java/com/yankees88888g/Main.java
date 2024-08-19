package com.yankees88888g;

import com.mojang.brigadier.arguments.BoolArgumentType;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

import static com.yankees88888g.FileLoader.getPath;
import static net.minecraft.server.command.CommandManager.*;

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
                    .requires(source -> source.hasPermissionLevel(2))
                    .then(argument("value", BoolArgumentType.bool())
                            .executes(context -> {
                                boolean result = BoolArgumentType.getBool(context, "value");
                                ServerCommandSource source = context.getSource();
                                ServerWorld world = source.getWorld();
                                if (result) {
                                    source.sendFeedback(() -> Text.literal("Careful Break is set to True."), true);
                                    FileLoader.updateFile(true, getPath(world));
                                } else {
                                    source.sendFeedback(() -> Text.literal("Careful Break is set to False."), true);
                                    FileLoader.updateFile(false, getPath(world));
                                }
                                return 1;
                            })
                    )
            );
        });
    }
}