package io.github.emberbocor.raidtracker.config;

import java.nio.file.Path;
import java.util.Set;

import org.jetbrains.annotations.Nullable;
import org.slf4j.helpers.MessageFormatter;

import com.electronwill.nightconfig.core.UnmodifiableConfig;

import io.github.emberbocor.raidtracker.RaidTracker;

final class ServerConfigReader {
    private static final String BOSS_BAR_FORMAT = "bossBarFormat";
    private static final String BOSS_BAR_BETWEEN_WAVES_FORMAT = "bossBarBetweenWavesFormat";
    private static final String GLOW_AT_REMAINING_PERCENT = "glowAtRemainingPercent";
    private static final Set<String> KEYS = Set.of(BOSS_BAR_FORMAT, BOSS_BAR_BETWEEN_WAVES_FORMAT, GLOW_AT_REMAINING_PERCENT);

    private final Path file;

    ServerConfigReader(Path file) {
        this.file = file;
    }

    ServerConfig.Settings read(@Nullable UnmodifiableConfig config) {
        ServerConfig.Settings defaults = ServerConfig.DEFAULTS;
        if (config == null) {
            return defaults;
        }
        for (UnmodifiableConfig.Entry entry : config.entrySet()) {
            if (!KEYS.contains(entry.getKey())) {
                warn("unknown option '{}', ignored", entry.getKey());
            }
        }
        return new ServerConfig.Settings(
                format(config, BOSS_BAR_FORMAT, defaults.bossBarFormat()),
                format(config, BOSS_BAR_BETWEEN_WAVES_FORMAT, defaults.bossBarBetweenWavesFormat()),
                percent(config, GLOW_AT_REMAINING_PERCENT, defaults.glowAtRemainingPercent()));
    }

    private String format(UnmodifiableConfig config, String key, String fallback) {
        Object value = config.get(key);
        if (value == null) {
            return fallback;
        }
        if (value instanceof String format) {
            return format;
        }
        warn("{} must be text in quotes, got '{}'; using \"{}\"", key, value, fallback);
        return fallback;
    }

    private int percent(UnmodifiableConfig config, String key, int fallback) {
        Object value = config.get(key);
        if (value == null) {
            return fallback;
        }
        if ((value instanceof Integer || value instanceof Long) && ((Number) value).longValue() >= 0 && ((Number) value).longValue() <= 100) {
            return ((Number) value).intValue();
        }
        warn("{} must be a whole number from 0 to 100, got '{}'; using {}", key, value, fallback);
        return fallback;
    }

    private void warn(String message, Object... args) {
        RaidTracker.LOGGER.warn("{}: {}", file, MessageFormatter.arrayFormat(message, args).getMessage());
    }
}
