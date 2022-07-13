/*
 * Copyright (c)  2022, Kevin Kragenbrink <kevin@writh.net>
 *           This program comes with ABSOLUTELY NO WARRANTY; for details see <https://www.gnu.org/licenses/gpl-3.0.html>.
 *           This is free software, and you are welcome to redistribute it under certain
 *           conditions; detailed at https://www.gnu.org/licenses/gpl-3.0.html
 */

package org.sosly.at.events;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.sosly.at.ArcanumTheoria;
import org.sosly.at.api.capabilities.IRunedBlocksCapability;
import org.sosly.at.api.magic.Rune;
import org.sosly.at.capabilities.entities.rituals.RunedBlocksProvider;

import java.util.List;

@Mod.EventBusSubscriber(modid = ArcanumTheoria.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RunedBlocksCapabilityEvents {
    @SubscribeEvent
    public static void onAttachCapability(AttachCapabilitiesEvent<LevelChunk> event) {
        event.addCapability(IRunedBlocksCapability.RUNED_BLOCKS_CAPABILITY, new RunedBlocksProvider());
    }

    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getWorld();

        if (level.isClientSide) {
            return;
        }

        Player player = event.getPlayer();
        Direction face = event.getFace();
        BlockPos pos = event.getPos();
        LevelChunk chunk = level.getChunkAt(pos);
        InteractionHand hand = event.getHand();

        if (!player.getItemInHand(hand).isEmpty()) {
            return;
        }

        chunk.getCapability(RunedBlocksProvider.RUNED_BLOCKS).ifPresent(p -> {
            ImmutableMap<Direction, Rune> runedFaces = p.getRunesAtBlockPos(pos);
            List<Rune> runes = runedFaces.values().asList();
            Rune rune = p.getRuneAtBlockPos(pos, face);
            if (rune != null && !rune.activate(player, level, pos, runes)) {
                event.setCanceled(true);
            }
        });
    }
}
