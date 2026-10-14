package io.github.emberbocor.raidtracker.config;

import java.nio.file.Path;

import io.github.emberbocor.raidtracker.platform.Services;

public final class ServerConfig {
    static final Settings DEFAULTS = new Settings(
            "Raid - Wave {wave}/{waves} - {raiders} raiders left",
            "Raid - Wave {wave}/{waves} cleared",
            50);

    private static volatile Settings settings = DEFAULTS;

    private ServerConfig() {
    }

    public static void load() {
        Path file = Services.PLATFORM.getConfigDirectory().resolve(ServerConfigFile.FILE_NAME);
        settings = new ServerConfigReader(file).read(ServerConfigFile.load(file).orElse(null));
    }

    public static String bossBarFormat() {
        return settings.bossBarFormat();
    }

    public static String bossBarBetweenWavesFormat() {
        return settings.bossBarBetweenWavesFormat();
    }

    public static int glowAtRemainingPercent() {
        return settings.glowAtRemainingPercent();
    }

    record Settings(String bossBarFormat, String bossBarBetweenWavesFormat, int glowAtRemainingPercent) {
    }
}
