/*
 * Copyright (c)  2022, Kevin Kragenbrink <kevin@writh.net>
 *           This program comes with ABSOLUTELY NO WARRANTY; for details see <https://www.gnu.org/licenses/gpl-3.0.html>.
 *           This is free software, and you are welcome to redistribute it under certain
 *           conditions; detailed at https://www.gnu.org/licenses/gpl-3.0.html
 */

package org.sosly.at.api.capabilities;

import net.minecraft.resources.ResourceLocation;
import org.sosly.at.ArcanumTheoria;

public interface IRitualistCapability {
    ResourceLocation RITUALIST_CAPABILITY = new ResourceLocation(ArcanumTheoria.MODID, "ritualist");

    /**
     * RitualKnowledge represents a player's knowledge of specific pages within the Rituals guidebook.
     * If the player does not have knowledge, they will be unable to read the page or perform the ritual magic on it.
     * If they do have knowledge, they can read the page and perform the ritual magic on it.
     *
     * @param knowledge
     **/
    void addRitualKnowledge(int knowledge);
    int getRitualKnowledge();
    boolean hasRitualKnowledge(int knowledge);
    void remRitualKnowledge(int knowledge);

    void setRitualKnowledge(int knowledge);

    public int Basic = 1 << 0;
}
