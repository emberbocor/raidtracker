package io.github.emberbocor.raidtracker.raid;

import org.jetbrains.annotations.Nullable;

import io.github.emberbocor.raidtracker.config.ServerConfig;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;

public final class RaidGlow {
    private static final int DURATION = 60;
    private static final int REFRESH_AT = 40;

    private RaidGlow() {
    }

    public static void tick(Raid raid, int waveSize) {
        if (!shouldGlow(raid, waveSize)) {
            return;
        }
        for (Raider raider : raid.getAllRaiders()) {
            if (raider.isAlive() && needsGlow(raider.getEffect(MobEffects.GLOWING))) {
                raider.addEffect(new MobEffectInstance(MobEffects.GLOWING, DURATION, 0, false, false, false));
            }
        }
    }

    private static boolean shouldGlow(Raid raid, int waveSize) {
        int percent = ServerConfig.glowAtRemainingPercent();
        int raiders = raid.getTotalRaidersAlive();
        if (percent == 0 || raiders == 0 || !raid.hasFirstWaveSpawned() || raid.isOver() || raid.isStopped()) {
            return false;
        }
        int total = Math.max(waveSize, raiders);
        return raiders <= (total * percent + 99) / 100;
    }

    private static boolean needsGlow(@Nullable MobEffectInstance glowing) {
        if (glowing == null) {
            return true;
        }
        if (isOwn(glowing)) {
            return glowing.endsWithin(REFRESH_AT);
        }
        // Glowing from another source (a bell, a spectral arrow, a command) is left alone. Taking it over on its
        // last tick only avoids a one-tick gap: it is removed later this tick anyway.
        return glowing.endsWithin(1);
    }

    private static boolean isOwn(MobEffectInstance glowing) {
        return glowing.getAmplifier() == 0
                && !glowing.isAmbient()
                && !glowing.isVisible()
                && !glowing.showIcon()
                && glowing.endsWithin(DURATION);
    }
}
