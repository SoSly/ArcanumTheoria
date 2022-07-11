/*
 * Copyright (c)  2022, Kevin Kragenbrink <kevin@writh.net>
 *           This program comes with ABSOLUTELY NO WARRANTY; for details see <https://www.gnu.org/licenses/gpl-3.0.html>.
 *           This is free software, and you are welcome to redistribute it under certain
 *           conditions; detailed at https://www.gnu.org/licenses/gpl-3.0.html
 */

package org.sosly.at.capabilities.entities.rituals;

import org.sosly.at.api.capabilities.IRitualistCapability;

public class RitualistCapability implements IRitualistCapability {
    private int ritualKnowledge;

    @Override
    public void addRitualKnowledge(int knowledge) {
        ritualKnowledge |= knowledge;
    }

    @Override
    public int getRitualKnowledge() {
        return ritualKnowledge;
    }

    @Override
    public boolean hasRitualKnowledge(int knowledge) {
        return (ritualKnowledge & knowledge) != 0;
    }

    @Override
    public void remRitualKnowledge(int knowledge) {
        ritualKnowledge &= ~knowledge;
    }

    @Override
    public void setRitualKnowledge(int knowledge) {
        ritualKnowledge = knowledge;
    }
}
