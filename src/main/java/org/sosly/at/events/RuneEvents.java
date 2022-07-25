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
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
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
import org.sosly.at.ArcanumTheoria;
import org.sosly.at.api.capabilities.IRunedBlocksCapability;
import org.sosly.at.api.magic.Rune;
import org.sosly.at.capabilities.entities.rituals.RunedBlocksProvider;
import org.sosly.at.networking.PacketHandler;
import org.sosly.at.networking.messages.clientbound.SyncRunedBlocksToClient;

import java.util.BitSet;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = ArcanumTheoria.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RuneEvents {
    private static final MultiBufferSource.BufferSource runeBufferSource = MultiBufferSource.immediate(new BufferBuilder(256));
    private static final RandomSource random = RandomSource.create();

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
                                BlockRenderDispatcher renderDispatcher = Minecraft.getInstance().getBlockRenderer();

                                VertexConsumer consumer = new SheetedDecalTextureGenerator(runeBufferSource.getBuffer(rune.getRenderType()), pose.pose(), pose.normal());
                                stack.pushPose();
                                stack.translate(pos.getX() - cameraPosition.x(), pos.getY() - cameraPosition.y(), pos.getZ() - cameraPosition.z());

                                if (state.getRenderShape() == RenderShape.MODEL) {
                                    BakedModel bakedmodel = renderDispatcher.getBlockModelShaper().getBlockModel(state);
                                    List<BakedQuad> quads = bakedmodel.getQuads(state, face, random, ModelData.EMPTY, null);
                                    BlockPos.MutableBlockPos mPos = pos.mutable();
                                    mPos.setWithOffset(pos, face);

                                    boolean useAO = Minecraft.useAmbientOcclusion() && state.getLightEmission(level, pos) == 0 && bakedmodel.useAmbientOcclusion(state, null);

                                    if (Block.shouldRenderFace(state, level, pos, face, mPos)) {
                                        float[] afloat = new float[Direction.values().length * 2];
                                        BitSet bitset = new BitSet(3);

                                        if (useAO) {
                                            ModelBlockRenderer.AmbientOcclusionFace aoface = new ModelBlockRenderer.AmbientOcclusionFace();
                                            renderDispatcher.getModelRenderer().renderModelFaceAO(level, state, pos, stack, consumer, quads, afloat, bitset, aoface, OverlayTexture.NO_OVERLAY);
                                        } else {
                                            int i = LevelRenderer.getLightColor(level, state, mPos);
                                            renderDispatcher.getModelRenderer().renderModelFaceFlat(level, state, pos, i, OverlayTexture.NO_OVERLAY, false, stack, consumer, quads, bitset);
                                        }
                                    }
                                }
                                stack.popPose();
                            }
                        });
                    });
                });
                runeBufferSource.endBatch();
            }
        }
    }
}
