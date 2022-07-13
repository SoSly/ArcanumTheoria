/*
 * Copyright (c)  2022, Kevin Kragenbrink <kevin@writh.net>
 *           This program comes with ABSOLUTELY NO WARRANTY; for details see <https://www.gnu.org/licenses/gpl-3.0.html>.
 *           This is free software, and you are welcome to redistribute it under certain
 *           conditions; detailed at https://www.gnu.org/licenses/gpl-3.0.html
 */

package org.sosly.at.items.ritual;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;
import org.sosly.at.api.magic.Rune;
import org.sosly.at.capabilities.entities.rituals.RunedBlocksProvider;
import org.sosly.at.magic.RuneRegistry;

import java.util.concurrent.atomic.AtomicBoolean;

public class ChiselItem extends TieredItem {
    public ChiselItem(Tier tier) {
        super(tier, (new Properties()).tab(CreativeModeTab.TAB_TOOLS));
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        BlockState state = context.getLevel().getBlockState(context.getClickedPos());
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        Direction face = context.getClickedFace();
        InteractionHand hand = context.getHand();

        if (isCorrectChiselForBlock(stack, state) && chiselBlock(stack, state, pos, face, level, (LivingEntity)player, hand)) {
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }

    public boolean chiselBlock(ItemStack stack, BlockState state, BlockPos pos, Direction face, Level level, LivingEntity entity, InteractionHand hand) {
        AtomicBoolean result = new AtomicBoolean(false);
        LevelChunk chunk = level.getChunkAt(pos);
        chunk.getCapability(RunedBlocksProvider.RUNED_BLOCKS).ifPresent(cap -> {
            if (level.isClientSide) {
                result.set(true);
                return;
            }

            Rune rune = cap.getRuneAtBlockPos(pos, face);
            if (rune != null) {
                // no point in continuing; we already have a rune here.
                return;
            }

            result.set(cap.setRuneAtBlockPos(pos, face, RuneRegistry.BREAK.get())); // todo: need to make the rune selection more dynamic than this
            // todo: push the capability to the client
        });

        if (result.get() && state.getDestroySpeed(level, pos) != 0.0F) {
            stack.hurtAndBreak(1, entity, (p) -> {
                p.broadcastBreakEvent(hand);
            });
        }

        return result.get();
    }

    public boolean isCorrectChiselForBlock(ItemStack stack, BlockState state) {
        return TierSortingRegistry.isCorrectTierForDrops(getTier(), state);
    }
}
