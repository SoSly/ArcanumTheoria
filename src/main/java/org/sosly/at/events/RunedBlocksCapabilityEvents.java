/*
 * Copyright (c)  2022, Kevin Kragenbrink <kevin@writh.net>
 *           This program comes with ABSOLUTELY NO WARRANTY; for details see <https://www.gnu.org/licenses/gpl-3.0.html>.
 *           This is free software, and you are welcome to redistribute it under certain
 *           conditions; detailed at https://www.gnu.org/licenses/gpl-3.0.html
 */

package org.sosly.at.events;

import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.sosly.at.ArcanumTheoria;
import org.sosly.at.api.capabilities.IRunedBlocksCapability;
import org.sosly.at.capabilities.entities.rituals.RunedBlocksProvider;

@Mod.EventBusSubscriber(modid = ArcanumTheoria.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RunedBlocksCapabilityEvents {
    @SubscribeEvent
    public static void onAttachCapability(AttachCapabilitiesEvent<LevelChunk> event) {
        event.addCapability(IRunedBlocksCapability.RUNED_BLOCKS_CAPABILITY, new RunedBlocksProvider());
    }
}
