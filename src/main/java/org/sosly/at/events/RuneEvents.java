/*
 * Copyright (c)  2022, Kevin Kragenbrink <kevin@writh.net>
 *           This program comes with ABSOLUTELY NO WARRANTY; for details see <https://www.gnu.org/licenses/gpl-3.0.html>.
 *           This is free software, and you are welcome to redistribute it under certain
 *           conditions; detailed at https://www.gnu.org/licenses/gpl-3.0.html
 */

package org.sosly.at.events;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ViewArea;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import org.apache.logging.log4j.core.jmx.Server;
import org.sosly.at.ArcanumTheoria;
import org.sosly.at.api.capabilities.IRunedBlocksCapability;
import org.sosly.at.api.magic.Rune;
import org.sosly.at.capabilities.entities.rituals.RunedBlocksProvider;
import org.sosly.at.networking.PacketHandler;
import org.sosly.at.networking.messages.clientbound.SyncRunedBlocksToClient;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Mod.EventBusSubscriber(modid = ArcanumTheoria.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RuneEvents {
    private static final MultiBufferSource.BufferSource crumblingBufferSource = MultiBufferSource.immediate(new BufferBuilder(256));

    @SubscribeEvent
    public static void onAttachCapability(AttachCapabilitiesEvent<Level> event) {
        event.addCapability(IRunedBlocksCapability.RUNED_BLOCKS_CAPABILITY, new RunedBlocksProvider());
    }

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        event.getEntity().getLevel().getCapability(RunedBlocksProvider.RUNED_BLOCKS).ifPresent(cap -> {
            PacketHandler.network.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)event.getEntity()), new SyncRunedBlocksToClient(cap));
        });
    }

    @SubscribeEvent
    public static void onChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        event.getEntity().getLevel().getCapability(RunedBlocksProvider.RUNED_BLOCKS).ifPresent(cap -> {
            PacketHandler.network.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)event.getEntity()), new SyncRunedBlocksToClient(cap));
        });
    }

    @SubscribeEvent
    public static void onActivate(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();

        if (level.isClientSide) {
            return;
        }

        Player player = event.getEntity();
        Direction face = event.getFace();
        BlockPos pos = event.getPos();
        InteractionHand hand = event.getHand();

        if (!player.getItemInHand(hand).isEmpty()) {
            return;
        }

        level.getCapability(RunedBlocksProvider.RUNED_BLOCKS).ifPresent(p -> {
            ImmutableMap<Direction, Rune> runedFaces = p.getRunesAtBlockPos(pos);
            List<Rune> runes = runedFaces.values().asList();
            Rune rune = p.getRuneAtBlockPos(pos, face);
            if (rune != null && !rune.activate(player, level, pos, runes)) {
                event.setCanceled(true);
            }
        });
    }

    @SubscribeEvent
    public static void onStage(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_CUTOUT_BLOCKS) {
            Level level = Minecraft.getInstance().level;
            Frustum frustum = event.getFrustum();
            Vec3 cameraPosition = event.getCamera().getPosition();
            PoseStack stack = event.getPoseStack();

            if (level != null) {
                level.getCapability(RunedBlocksProvider.RUNED_BLOCKS).ifPresent(cap -> {
                    ImmutableMap<BlockPos, Map<Direction, Rune>> blocks = cap.getAllRunedBlocks();
                    blocks.forEach((pos, facedRunes) -> {
                        facedRunes.forEach((face, rune) -> {
                            if (frustum.isVisible(new AABB(pos))) {
                                BlockState state = level.getBlockState(pos);
                                PoseStack.Pose pose = stack.last();

                                VertexConsumer consumer = new SheetedDecalTextureGenerator(crumblingBufferSource.getBuffer(ModelBakery.DESTROY_TYPES.get(5)), pose.pose(), pose.normal());

                                stack.pushPose();
                                stack.translate(pos.getX() - cameraPosition.x(), pos.getY() - cameraPosition.y(), pos.getZ() - cameraPosition.z());
                                Minecraft.getInstance().getBlockRenderer().renderBreakingTexture(state, pos, level, stack, consumer, ModelData.EMPTY);
                                stack.popPose();
                            }
                        });
                    });
                });
                crumblingBufferSource.endBatch();
            }
        }
    }
}
