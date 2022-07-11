/*
 * Copyright (c)  2022, Kevin Kragenbrink <kevin@writh.net>
 *           This program comes with ABSOLUTELY NO WARRANTY; for details see <https://www.gnu.org/licenses/gpl-3.0.html>.
 *           This is free software, and you are welcome to redistribute it under certain
 *           conditions; detailed at https://www.gnu.org/licenses/gpl-3.0.html
 */

package org.sosly.at.events;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.sosly.at.ArcanumTheoria;
import org.sosly.at.advancements.AdvancementTriggerRegistry;
import org.sosly.at.api.capabilities.IRitualistCapability;
import org.sosly.at.capabilities.entities.rituals.RitualistProvider;

@Mod.EventBusSubscriber(modid = ArcanumTheoria.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RitualistCapabilityEvents {
    @SubscribeEvent
    public static void onAttachCapability(AttachCapabilitiesEvent<?> event) {
        event.addCapability(IRitualistCapability.RITUALIST_CAPABILITY, new RitualistProvider());
    }

    @SubscribeEvent
    public static void onLoad(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getPlayer();
        player.getCapability(RitualistProvider.RITUALIST).ifPresent(p -> {
            if (p.hasRitualKnowledge(IRitualistCapability.Basic)) {
                p.remRitualKnowledge(IRitualistCapability.Basic);
            }
        });
    }

    @SubscribeEvent
    public static void onRightClick(RightClickBlock event) {
        BlockPos pos = event.getPos();
        Player player = event.getPlayer();
        Level level = player.getLevel();
        BlockState blockState = level.getBlockState(pos);
        Block block = blockState.getBlock();
        if (block == Blocks.BOOKSHELF && !level.isClientSide) {
            InteractionHand hand = event.getHand();
            if (hand == InteractionHand.MAIN_HAND) {
                ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
                if (stack.isEmpty()) {
                    player.getCapability(RitualistProvider.RITUALIST).ifPresent(p -> {
                        if (!p.hasRitualKnowledge(IRitualistCapability.Basic)) {
                            p.addRitualKnowledge(IRitualistCapability.Basic);
                            AdvancementTriggerRegistry.FOUND_RITUALIST_GUIDEBOOK.trigger((ServerPlayer) player);
                        }
                    });
                }
            }
        }
    }
}
