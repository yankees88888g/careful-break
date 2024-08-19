package com.yankees88888g.mixin;

import com.google.common.collect.ImmutableList;

import com.yankees88888g.FileLoader;
import com.yankees88888g.Main;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static com.yankees88888g.FileLoader.getPath;
import static net.minecraft.block.Block.getDroppedStacks;
import java.io.FileOutputStream;

@Mixin(Block.class)
public class BlockBreakMixin {
    @Redirect(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;getDroppedStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)Ljava/util/List;"
            ),
            method = "dropStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)V")
    private static List<ItemStack> dropStacks(BlockState state, ServerWorld world, BlockPos pos, BlockEntity blockEntity, Entity entity, ItemStack stack) throws IOException {
        List<ItemStack> drops = getDroppedStacks(state, world, pos, blockEntity, entity, stack);
        if (entity instanceof PlayerEntity && entity.isSneaking() && FileLoader.loadFile(getPath(world))) {
            ServerPlayerEntity player = (ServerPlayerEntity) entity;
            ImmutableList.copyOf(drops).forEach((itemStack) -> {
                if (player.getInventory().insertStack(itemStack)) {
                    drops.remove(itemStack);
                }
            });
        }
        return drops;
    }
}