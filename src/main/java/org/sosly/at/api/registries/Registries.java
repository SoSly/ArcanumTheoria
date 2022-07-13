/*
 * Copyright (c)  2022, Kevin Kragenbrink <kevin@writh.net>
 *           This program comes with ABSOLUTELY NO WARRANTY; for details see <https://www.gnu.org/licenses/gpl-3.0.html>.
 *           This is free software, and you are welcome to redistribute it under certain
 *           conditions; detailed at https://www.gnu.org/licenses/gpl-3.0.html
 */

package org.sosly.at.api.registries;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;
import org.sosly.at.ArcanumTheoria;
import org.sosly.at.api.magic.Rune;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = ArcanumTheoria.MODID)
public class Registries {
    public static IForgeRegistry<Rune> RUNES;

    public static class Keys {
        public static final ResourceLocation RUNES = new ResourceLocation(ArcanumTheoria.MODID, "runes");
    }

    @SubscribeEvent
    public static void onNewRegistries(NewRegistryEvent event) {
        makeRegistry(event, Keys.RUNES, (IForgeRegistry<Rune> registry) -> RUNES = registry);
    }

    static <T> void makeRegistry(NewRegistryEvent event, ResourceLocation name, Consumer<IForgeRegistry<T>> consumer) {
        RegistryBuilder<T> builder = new RegistryBuilder<T>();
        builder.setName(name);
        builder.setMaxID(Integer.MAX_VALUE - 1);
        builder.disableSaving();
        builder.allowModification();
        Supplier<IForgeRegistry<T>> registry = event.create(builder, consumer);
    }
}
