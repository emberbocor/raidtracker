package io.github.emberbocor.raidtracker.raid;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;

// Everyone who has been part of the current wave, dead or alive. Saved with the raid; vanilla ignores the extra keys,
// so a world saved with the mod still loads without it.
public final class RaidWave {
    private static final String WAVE = "raidtracker:Wave";
    private static final String MEMBERS = "raidtracker:WaveMembers";

    private final Set<UUID> members = new HashSet<>();
    private int wave = -1;

    public void add(int wave, UUID member) {
        if (wave > this.wave) {
            this.wave = wave;
            members.clear();
        }
        members.add(member);
    }

    public int size() {
        return members.size();
    }

    public void save(CompoundTag compound) {
        if (members.isEmpty()) {
            return;
        }
        ListTag list = new ListTag();
        for (UUID member : members) {
            list.add(NbtUtils.createUUID(member));
        }
        compound.putInt(WAVE, wave);
        compound.put(MEMBERS, list);
    }

    public void load(CompoundTag compound) {
        if (!compound.contains(WAVE, Tag.TAG_INT) || !compound.contains(MEMBERS, Tag.TAG_LIST)) {
            return;
        }
        wave = compound.getInt(WAVE);
        for (Tag member : compound.getList(MEMBERS, Tag.TAG_INT_ARRAY)) {
            members.add(NbtUtils.loadUUID(member));
        }
    }
}
