package com.yankees88888g.mixin;

import com.google.common.collect.ImmutableList;

import com.yankees88888g.FileLoader;
import com.yankees88888g.Main;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

import 	net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static com.yankees88888g.FileLoader.getPath;
import static net.minecraft.world.level.block.Block.getDrops;

import java.io.FileOutputStream;

@Mixin(Block.class)
public class BlockBreakMixin {
    @Redirect(
            method = "dropResources(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/Block;getDrops(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)Ljava/util/List;"
            )
    )
    private static List<ItemStack> dropStacks(BlockState state, ServerLevel world, BlockPos pos, BlockEntity blockEntity, Entity entity, ItemStack stack) throws IOException {
        List<ItemStack> drops = getDrops(state, world, pos, blockEntity, entity, stack);
        if (entity instanceof ServerPlayer && entity.isShiftKeyDown() && FileLoader.loadFile(getPath(world))) {
            ServerPlayer player = (ServerPlayer) entity;
            ImmutableList.copyOf(drops).forEach((itemStack) -> {
                if (player.getInventory().add(itemStack.copy())) {
                    drops.remove(itemStack);
                }
            });
        }
        return drops;
    }
}