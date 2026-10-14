package io.github.emberbocor.raidtracker.raid;

import org.jetbrains.annotations.Nullable;

import io.github.emberbocor.raidtracker.config.ServerConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.raid.Raid;

public final class RaidBossBar {
    private RaidBossBar() {
    }

    @Nullable
    public static Component name(Raid raid, int normalWaves) {
        if (!raid.hasFirstWaveSpawned() || raid.isOver() || raid.isStopped()) {
            return null;
        }
        int raiders = raid.getTotalRaidersAlive();
        String format = raiders > 0 ? ServerConfig.bossBarFormat() : ServerConfig.bossBarBetweenWavesFormat();
        if (format.isEmpty()) {
            return null;
        }
        int waves = raid.getRaidOmenLevel() > 1 ? normalWaves + 1 : normalWaves;
        return Component.literal(format
                .replace("{wave}", Integer.toString(raid.getGroupsSpawned()))
                .replace("{waves}", Integer.toString(waves))
                .replace("{raiders}", Integer.toString(raiders)));
    }
}
