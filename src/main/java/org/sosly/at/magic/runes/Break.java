/*
 * Copyright (c)  2022, Kevin Kragenbrink <kevin@writh.net>
 *           This program comes with ABSOLUTELY NO WARRANTY; for details see <https://www.gnu.org/licenses/gpl-3.0.html>.
 *           This is free software, and you are welcome to redistribute it under certain
 *           conditions; detailed at https://www.gnu.org/licenses/gpl-3.0.html
 */

package org.sosly.at.magic.runes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import org.sosly.at.api.magic.Rune;
import org.sosly.at.capabilities.entities.rituals.RunedBlocksProvider;
import org.sosly.at.magic.RuneRegistry;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Break extends Rune {
    @Override
    public boolean activate(Player player, Level level, BlockPos oPos, List<Rune> runes) {
        getMarkedNeighbors(level, oPos).forEach(pos -> destroyBlock(player, level, pos, -1, -1));
        return true;
    }

    private static Set<BlockPos> getMarkedNeighbors(Level level, BlockPos pos) {
        Set<BlockPos> blocks = new HashSet<>();
        return getMarkedNeighbors(level, pos, blocks);
    }

    private static Set<BlockPos> getMarkedNeighbors(Level level, BlockPos oPos, Set<BlockPos> blocks) {
        blocks.add(oPos);

        Direction.stream().forEach(direction -> {
            BlockPos pos = oPos.relative(direction);
            if (level.getBlockState(pos).getBlock() != Blocks.AIR) {
                level.getChunkAt(pos).getCapability(RunedBlocksProvider.RUNED_BLOCKS).ifPresent(p -> {
                    if (p.getRunesAtBlockPos(pos).values().stream().anyMatch(t -> t == RuneRegistry.BREAK.get())) {
                        if (!blocks.contains(pos)) {
                            getMarkedNeighbors(level, pos, blocks);
                        }
                    }
                });
            }
        });

        return blocks;
    }

    private static boolean destroyBlock(Player player, Level level, BlockPos pos, int fortune, int silk) {
        BlockState state = level.getBlockState(pos);
        float hardness = state.getDestroySpeed(level, pos);

        BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(level, pos, state, player);
        if (MinecraftForge.EVENT_BUS.post(event)) {
            return false;
        } else {
            // create a dummy pick to perform the block break
            ItemStack stack = new ItemStack(Items.DIAMOND_PICKAXE);
            if (silk > 0) {
                stack.enchant(Enchantments.SILK_TOUCH, 1);
            }
            if (fortune > 0) {
                stack.enchant(Enchantments.BLOCK_FORTUNE, fortune);
            }

            int experience = state.getExpDrop(level, level.random, pos, fortune, silk);
            if (experience > 0) {
                state.getBlock().popExperience((ServerLevel)level, pos, experience);
            }

            Block.dropResources(state, level, pos, level.getBlockEntity(pos), player, stack);
            if (level.destroyBlock(pos, false)) {
                if (!level.isOutsideBuildHeight(pos)) {
                    level.sendBlockUpdated(pos, state, state, 3);
                }
                return true;
            }
        }

        return false;
    }
}