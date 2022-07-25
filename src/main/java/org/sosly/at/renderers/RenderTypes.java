/*
 * Copyright (c)  2022, Kevin Kragenbrink <kevin@writh.net>
 *           This program comes with ABSOLUTELY NO WARRANTY; for details see <https://www.gnu.org/licenses/gpl-3.0.html>.
 *           This is free software, and you are welcome to redistribute it under certain
 *           conditions; detailed at https://www.gnu.org/licenses/gpl-3.0.html
 */

package org.sosly.at.renderers;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class RenderTypes {
    public static final Function<ResourceLocation, RenderType> RUNE = Util.memoize((p) -> {
        RenderStateShard.TextureStateShard textureStateShard = new RenderStateShard.TextureStateShard(p, false, false);
        RenderStateShard.ShaderStateShard shaderStateShard = new RenderStateShard.ShaderStateShard(GameRenderer::getRendertypeCutoutShader);

        return RenderType.create("rune", DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 256, false, true,
                RenderType.CompositeState.builder()
                        .setShaderState(shaderStateShard)
                        .setTextureState(textureStateShard)
                        .setTransparencyState(RenderStateShard.ADDITIVE_TRANSPARENCY)
                        .setWriteMaskState(RenderStateShard.COLOR_WRITE)
                        .setLayeringState(RenderStateShard.POLYGON_OFFSET_LAYERING)
                        .createCompositeState(false));
    });
}
