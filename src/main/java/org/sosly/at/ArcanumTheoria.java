/*
 * Copyright (c)  2022, Kevin Kragenbrink <kevin@writh.net>
 *           This program comes with ABSOLUTELY NO WARRANTY; for details see <https://www.gnu.org/licenses/gpl-3.0.html>.
 *           This is free software, and you are welcome to redistribute it under certain
 *           conditions; detailed at https://www.gnu.org/licenses/gpl-3.0.html
 */

package org.sosly.at;

import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.sosly.at.items.ItemRegistry;
import org.sosly.at.magic.RuneRegistry;

@Mod(ArcanumTheoria.MODID)
public class ArcanumTheoria {
    public static final String MODID = "at";
    private static final Logger LOGGER = LogUtils.getLogger();

    public ArcanumTheoria() {
        // Initialize registries
        IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();
        ItemRegistry.ITEMS.register(modbus);
        RuneRegistry.RUNES.register(modbus);
    }
}
