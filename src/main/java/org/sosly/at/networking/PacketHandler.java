/*
 * Copyright (c)  2022, Kevin Kragenbrink <kevin@writh.net>
 *           This program comes with ABSOLUTELY NO WARRANTY; for details see <https://www.gnu.org/licenses/gpl-3.0.html>.
 *           This is free software, and you are welcome to redistribute it under certain
 *           conditions; detailed at https://www.gnu.org/licenses/gpl-3.0.html
 */

package org.sosly.at.networking;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.sosly.at.ArcanumTheoria;
import org.sosly.at.networking.messages.clientbound.SyncRunedBlocksToClient;

@Mod.EventBusSubscriber(modid = ArcanumTheoria.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel network = NetworkRegistry.newSimpleChannel(new ResourceLocation(ArcanumTheoria.MODID, "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    @SubscribeEvent
    public static void setup(FMLCommonSetupEvent event) {
        int packet_id = 1;
        network.registerMessage(packet_id++, SyncRunedBlocksToClient.class, SyncRunedBlocksToClient::encode, SyncRunedBlocksToClient::decode, SyncRunedBlocksToClient::handleRunedBlockCapabilitiesSync);
    }
}
