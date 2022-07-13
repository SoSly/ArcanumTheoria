/*
 * Copyright (c)  2022, Kevin Kragenbrink <kevin@writh.net>
 *           This program comes with ABSOLUTELY NO WARRANTY; for details see <https://www.gnu.org/licenses/gpl-3.0.html>.
 *           This is free software, and you are welcome to redistribute it under certain
 *           conditions; detailed at https://www.gnu.org/licenses/gpl-3.0.html
 */

package org.sosly.at.magic;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.sosly.at.ArcanumTheoria;
import org.sosly.at.api.magic.Rune;
import org.sosly.at.api.registries.Registries;
import org.sosly.at.magic.runes.Break;
import org.sosly.at.magic.runes.Illuminate;

public class RuneRegistry {
    public static final DeferredRegister<Rune> RUNES = DeferredRegister.create(Registries.Keys.RUNES, ArcanumTheoria.MODID);
    public static final RegistryObject<Break> BREAK = RUNES.register("break", Break::new);
    public static final RegistryObject<Illuminate> ILLUMINATE = RUNES.register("illuminate", Illuminate::new);
}
