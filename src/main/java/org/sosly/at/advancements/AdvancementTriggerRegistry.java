/*
 * Copyright (c)  2022, Kevin Kragenbrink <kevin@writh.net>
 *           This program comes with ABSOLUTELY NO WARRANTY; for details see <https://www.gnu.org/licenses/gpl-3.0.html>.
 *           This is free software, and you are welcome to redistribute it under certain
 *           conditions; detailed at https://www.gnu.org/licenses/gpl-3.0.html
 */

package org.sosly.at.advancements;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.sosly.at.ArcanumTheoria;
import org.sosly.at.advancements.triggers.FoundRitualistGuidebook;

@Mod.EventBusSubscriber(modid = ArcanumTheoria.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AdvancementTriggerRegistry {
    public static final FoundRitualistGuidebook FOUND_RITUALIST_GUIDEBOOK = new FoundRitualistGuidebook();

    @SubscribeEvent
    public static void commonLoad(FMLCommonSetupEvent event) {
        CriteriaTriggers.register(FOUND_RITUALIST_GUIDEBOOK);
    }
}
