/*
 * Copyright (c)  2022, Kevin Kragenbrink <kevin@writh.net>
 *           This program comes with ABSOLUTELY NO WARRANTY; for details see <https://www.gnu.org/licenses/gpl-3.0.html>.
 *           This is free software, and you are welcome to redistribute it under certain
 *           conditions; detailed at https://www.gnu.org/licenses/gpl-3.0.html
 */

package org.sosly.at.capabilities.entities.rituals;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.sosly.at.api.capabilities.IRunedBlocksCapability;
import org.sosly.at.api.magic.Rune;
import org.sosly.at.api.registries.Registries;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class RunedBlocksProvider implements ICapabilitySerializable<Tag> {
    public static final Capability<IRunedBlocksCapability> RUNED_BLOCKS = CapabilityManager.get(new CapabilityToken<>() {});
    private final LazyOptional<IRunedBlocksCapability> holder = LazyOptional.of(RunedBlocksCapability::new);

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return RUNED_BLOCKS.orEmpty(cap, this.holder);
    }

    @Override
    public Tag serializeNBT() {
        IRunedBlocksCapability instance = this.holder.orElse(new RunedBlocksCapability());
        CompoundTag nbt = new CompoundTag();
        AtomicInteger blocks = new AtomicInteger(0);
        instance.getAllRunedBlocks().forEach((pos, runedFaces) -> {
            CompoundTag blockNBT = new CompoundTag();
            blockNBT.putInt("x", pos.getX());
            blockNBT.putInt("y", pos.getY());
            blockNBT.putInt("z", pos.getZ());

            Direction.stream().forEach((direction) -> {
                ResourceLocation rune = Registries.RUNES.getKey(runedFaces.get(direction));
                if (rune != null) {
                    blockNBT.putString(direction.getName(), rune.toString());
                }
            });

            int idx = blocks.getAndIncrement();
            nbt.put(Integer.toHexString(idx), blockNBT);
        });
        nbt.putInt("blocks", blocks.get());
        return nbt;
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        IRunedBlocksCapability instance = this.holder.orElse(new RunedBlocksCapability());

        Map<BlockPos, Map<Direction, Rune>> runedBlocks = new HashMap<>();
        if (nbt instanceof CompoundTag cnbt) {
            int blocks = cnbt.getInt("blocks");
            do {
                if ((cnbt.get(Integer.toHexString(blocks))) instanceof CompoundTag blockNBT) {
                    int x = blockNBT.getInt("x");
                    int y = blockNBT.getInt("y");
                    int z = blockNBT.getInt("z");
                    BlockPos pos = new BlockPos(x, y, z);
                    Map<Direction, Rune> RunedFaces = new HashMap<>();

                    Direction.stream().forEach((direction) -> {
                        String runeLoc = blockNBT.getString(direction.getName());
                        if (!runeLoc.equals("")) {
                            Rune rune = Registries.RUNES.getValue(new ResourceLocation(runeLoc));
                            if (rune != null) {
                                RunedFaces.put(direction, rune);
                            }
                        }
                    });

                    if (!RunedFaces.isEmpty()) {
                        runedBlocks.put(pos, RunedFaces);
                    }
                }

                if (!runedBlocks.isEmpty()) {
                    instance.setAllRunedBlocks(runedBlocks);
                }
            } while (blocks-- > 0);
        }
    }
}
