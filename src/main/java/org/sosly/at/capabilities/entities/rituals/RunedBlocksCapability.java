/*
 * Copyright (c)  2022, Kevin Kragenbrink <kevin@writh.net>
 *           This program comes with ABSOLUTELY NO WARRANTY; for details see <https://www.gnu.org/licenses/gpl-3.0.html>.
 *           This is free software, and you are welcome to redistribute it under certain
 *           conditions; detailed at https://www.gnu.org/licenses/gpl-3.0.html
 */

package org.sosly.at.capabilities.entities.rituals;

import com.google.common.collect.ImmutableMap;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.sosly.at.api.capabilities.IRunedBlocksCapability;
import org.sosly.at.api.magic.Rune;

import java.util.HashMap;
import java.util.Map;

public class RunedBlocksCapability implements IRunedBlocksCapability {
    final Map<BlockPos, Map<Direction, Rune>> runedBlocks = new HashMap<>();

    @Override
    public ImmutableMap<BlockPos, Map<Direction, Rune>> getAllRunedBlocks() {
        return ImmutableMap.copyOf(runedBlocks);
    }

    @Override
    public void setAllRunedBlocks(Map<BlockPos, Map<Direction, Rune>> blockedRunes) {
        runedBlocks.clear();
        runedBlocks.putAll(blockedRunes);
    }

    @Override
    public boolean clearRuneAtBlockPos(BlockPos pos, Direction direction) {
        Map<Direction, Rune> runedFaces = runedBlocks.get(pos);
        if (runedFaces == null || runedFaces.isEmpty()) {
            return true;
        }

        if (!runedFaces.containsKey(direction)) {
            return true;
        }

        try {
            runedFaces.remove(direction);
            if (runedFaces.size() > 0) {
                runedBlocks.put(pos, runedFaces);
            } else {
                runedBlocks.remove(pos);
            }
            return true;
        } catch (Exception e) {
            LogUtils.getLogger().warn("unable to clear rune on block at pos={} face={}", pos, direction);
            return false;
        }
    }

    @Override
    public Rune getRuneAtBlockPos(BlockPos pos, Direction direction) {
        return getRunedFaces(pos).get(direction);
    }

    @Override
    public ImmutableMap<Direction, Rune> getRunesAtBlockPos(BlockPos pos) {
        return ImmutableMap.copyOf(getRunedFaces(pos));
    }

    @Override
    public boolean setRuneAtBlockPos(BlockPos pos, Direction direction, Rune rune) {
        Map<Direction, Rune> runedFaces = getRunedFaces(pos);

        if (runedFaces.containsKey(direction)) {
            return false;
        }

        try {
            runedFaces.put(direction, rune);
            runedBlocks.put(pos, runedFaces);
            return true;
        } catch (Exception e) {
            LogUtils.getLogger().warn("Unable to put rune on block at pos={} direction={}", pos, direction);
            return false;
        }
    }

    private Map<Direction, Rune> getRunedFaces(BlockPos pos) {
        return runedBlocks.getOrDefault(pos, new HashMap<>());
    }
}
