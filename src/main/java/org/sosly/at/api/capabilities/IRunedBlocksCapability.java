/*
 * Copyright (c)  2022, Kevin Kragenbrink <kevin@writh.net>
 *           This program comes with ABSOLUTELY NO WARRANTY; for details see <https://www.gnu.org/licenses/gpl-3.0.html>.
 *           This is free software, and you are welcome to redistribute it under certain
 *           conditions; detailed at https://www.gnu.org/licenses/gpl-3.0.html
 */

package org.sosly.at.api.capabilities;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import org.sosly.at.ArcanumTheoria;

import java.util.Map;

public interface IRunedBlocksCapability {
    ResourceLocation RUNED_BLOCKS_CAPABILITY = new ResourceLocation(ArcanumTheoria.MODID, "runed_blocks");

    ImmutableMap<BlockPos, Map<Direction, Rune>> getAllRunedBlocks();
    void setAllRunedBlocks(Map<BlockPos, Map<Direction, Rune>> blockedRunes);

    boolean clearRuneAtBlockPos(BlockPos pos, Direction direction);
    Rune getRuneAtBlockPos(BlockPos pos, Direction direction);
    ImmutableMap<Direction, Rune> getRunesAtBlockPos(BlockPos pos);
    boolean setRuneAtBlockPos(BlockPos pos, Direction direction, Rune rune);

    // todo: This should move to an object stored in the API and initialized at startup time, so that addons can register their own runes
    //       Individual runes should include not just a name, but also a ResourceLocation, which should probably replace the string
    //       representation of this enum in the Provider.
    static enum Rune {
        BREAK
    }
}
