/*
 * Copyright (c)  2022, Kevin Kragenbrink <kevin@writh.net>
 *           This program comes with ABSOLUTELY NO WARRANTY; for details see <https://www.gnu.org/licenses/gpl-3.0.html>.
 *           This is free software, and you are welcome to redistribute it under certain
 *           conditions; detailed at https://www.gnu.org/licenses/gpl-3.0.html
 */

package org.sosly.at.capabilities.entities.rituals;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.sosly.at.api.capabilities.IRitualistCapability;

public class RitualistProvider implements ICapabilitySerializable<Tag> {
    private static final String RITUAL_KNOWLEDGE_TAG = "ritual_knowledge";

    public static final Capability<IRitualistCapability> RITUALIST = CapabilityManager.get(new CapabilityToken<>() {});
    private final LazyOptional<IRitualistCapability> holder = LazyOptional.of(RitualistCapability::new);

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return RITUALIST.orEmpty(cap, this.holder);
    }

    @Override
    public Tag serializeNBT() {
        IRitualistCapability instance = this.holder.orElse(new RitualistCapability());
        CompoundTag nbt = new CompoundTag();
        nbt.putInt(RITUAL_KNOWLEDGE_TAG, instance.getRitualKnowledge());
        return nbt;
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        IRitualistCapability instance = this.holder.orElse(new RitualistCapability());
        if (nbt instanceof CompoundTag cnbt) {
            if (cnbt.contains(RITUAL_KNOWLEDGE_TAG)) {
                instance.setRitualKnowledge(cnbt.getInt(RITUAL_KNOWLEDGE_TAG));
            }
        }
    }
}
