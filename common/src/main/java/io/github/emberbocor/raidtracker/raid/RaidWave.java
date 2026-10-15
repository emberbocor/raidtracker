package io.github.emberbocor.raidtracker.raid;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.UUIDUtil;
import net.minecraft.world.entity.raid.Raid;

public final class RaidWave {
    // Same keys and format as the NBT of the 1.21.1 version, so raids saved there keep their wave size.
    private static final MapCodec<RaidWave> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.lenientOptionalFieldOf("raidtracker:Wave", -1).forGetter(wave -> wave.wave),
            UUIDUtil.CODEC_SET.lenientOptionalFieldOf("raidtracker:WaveMembers", Set.of()).forGetter(wave -> wave.members)
    ).apply(instance, RaidWave::new));

    private final Set<UUID> members;
    private int wave;

    public RaidWave() {
        this(-1, Set.of());
    }

    private RaidWave(int wave, Set<UUID> members) {
        this.wave = wave;
        this.members = new HashSet<>(members);
    }

    public static MapCodec<Raid> withWave(MapCodec<Raid> raidCodec) {
        return Codec.mapPair(raidCodec, MAP_CODEC).xmap(
                pair -> {
                    ((RaidWaveHolder) pair.getFirst()).raidtracker$setWave(pair.getSecond());
                    return pair.getFirst();
                },
                raid -> Pair.of(raid, ((RaidWaveHolder) raid).raidtracker$getWave()));
    }

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
}
