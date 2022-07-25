/*
 * Copyright (c)  2022, Kevin Kragenbrink <kevin@writh.net>
 *           This program comes with ABSOLUTELY NO WARRANTY; for details see <https://www.gnu.org/licenses/gpl-3.0.html>.
 *           This is free software, and you are welcome to redistribute it under certain
 *           conditions; detailed at https://www.gnu.org/licenses/gpl-3.0.html
 */

package org.sosly.at.api.magic;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.sosly.at.ArcanumTheoria;
import org.sosly.at.renderers.RenderTypes;

import java.util.List;

public abstract class Rune implements IRune {
    public abstract boolean activate(Player player, Level level, BlockPos pos, List<Rune> runes);
    public abstract RenderType getRenderType();

    protected static RenderType setTexture(String location) {
        return RenderTypes.RUNE.apply(new ResourceLocation(ArcanumTheoria.MODID, location));
    }
}
