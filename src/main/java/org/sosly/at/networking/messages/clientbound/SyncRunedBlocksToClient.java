/*
 * Copyright (c)  2022, Kevin Kragenbrink <kevin@writh.net>
 *           This program comes with ABSOLUTELY NO WARRANTY; for details see <https://www.gnu.org/licenses/gpl-3.0.html>.
 *           This is free software, and you are welcome to redistribute it under certain
 *           conditions; detailed at https://www.gnu.org/licenses/gpl-3.0.html
 */

package org.sosly.at.networking.messages.clientbound;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import org.sosly.at.api.capabilities.IRunedBlocksCapability;
import org.sosly.at.capabilities.entities.rituals.RunedBlocksCapability;
import org.sosly.at.capabilities.entities.rituals.RunedBlocksProvider;
import org.sosly.at.networking.ClientMessageHandler;
import org.sosly.at.networking.messages.BaseMessage;

import java.util.function.Supplier;

public class SyncRunedBlocksToClient extends BaseMessage {
    private final IRunedBlocksCapability cap;

    public SyncRunedBlocksToClient(IRunedBlocksCapability cap) {
        this.cap = cap;
    }

    public static SyncRunedBlocksToClient decode(FriendlyByteBuf buf) {
        SyncRunedBlocksToClient msg = null;

        try {
            CompoundTag nbt = buf.readNbt();
            if (nbt != null) {
                RunedBlocksProvider provider = new RunedBlocksProvider();
                provider.deserializeNBT(nbt);
                IRunedBlocksCapability cap = provider.getCapability(RunedBlocksProvider.RUNED_BLOCKS).orElse(new RunedBlocksCapability());
                msg = new SyncRunedBlocksToClient(cap);
                msg.messageIsValid = true;
            }
        } catch (IndexOutOfBoundsException | IllegalArgumentException err) {
            return null;
        }

        return msg;
    }

    public static void encode(SyncRunedBlocksToClient msg, FriendlyByteBuf buf) {
        CompoundTag cnbt = (CompoundTag)RunedBlocksProvider.serializeNBT(msg.cap);
        buf.writeNbt(cnbt);
    }

    public static void handleRunedBlockCapabilitiesSync(SyncRunedBlocksToClient msg, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context ctx = contextSupplier.get();
        if (ClientMessageHandler.validateBasic(msg, ctx)) {
            Level level = Minecraft.getInstance().level;
            if (level != null) {
                level.getCapability(RunedBlocksProvider.RUNED_BLOCKS).ifPresent(cap -> {
                    cap.setAllRunedBlocks(msg.cap.getAllRunedBlocks());
                });
            }
        }
    }
}
