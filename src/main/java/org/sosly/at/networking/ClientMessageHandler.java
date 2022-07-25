/*
 * Copyright (c)  2022, Kevin Kragenbrink <kevin@writh.net>
 *           This program comes with ABSOLUTELY NO WARRANTY; for details see <https://www.gnu.org/licenses/gpl-3.0.html>.
 *           This is free software, and you are welcome to redistribute it under certain
 *           conditions; detailed at https://www.gnu.org/licenses/gpl-3.0.html
 */

package org.sosly.at.networking;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import org.sosly.at.networking.messages.BaseMessage;

public class ClientMessageHandler {
    public static <T extends BaseMessage> boolean validateBasic(T message, NetworkEvent.Context ctx) {
        LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
        ctx.setPacketHandled(true);
        if (sideReceived != LogicalSide.CLIENT) {
            LogUtils.getLogger().error("{} received on wrong side: {}", message.getClass().getName(), sideReceived);
            return false;
        } else if (!message.isMessageValid()) {
            LogUtils.getLogger().error("{} was invalid: {}", message.getClass().getName(), message);
            return false;
        } else {
            return true;
        }
    }
}
