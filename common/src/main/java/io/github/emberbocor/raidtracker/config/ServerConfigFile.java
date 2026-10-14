package io.github.emberbocor.raidtracker.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.electronwill.nightconfig.toml.TomlFormat;

import io.github.emberbocor.raidtracker.RaidTracker;

final class ServerConfigFile {
    static final String FILE_NAME = RaidTracker.MODID + "-server.toml";

    private static final String DEFAULT_TEXT = """
            # Raid Tracker server config.
            # Created on the first server start. Changes take effect after a server restart.
            # Before the first wave arrives and once the raid is won or lost, the boss bar keeps the vanilla text.

            # Boss bar text while a wave is running.
            # Placeholders: {wave} = the current wave, {waves} = the total number of waves (including the bonus wave
            # of Bad Omen II and higher), {raiders} = the raiders left alive.
            # "" keeps the vanilla text.
            bossBarFormat = "Raid - Wave {wave}/{waves} - {raiders} raiders left"

            # Boss bar text after a wave is cleared, until the next wave arrives (after the last wave: until the
            # victory text). Same placeholders; {wave} is the wave that was just cleared.
            # "" keeps the vanilla text.
            bossBarBetweenWavesFormat = "Raid - Wave {wave}/{waves} cleared"

            # Every raider of the current wave glows once this percentage of the wave or less is left alive, rounded
            # up (with 50 and a wave of 5 raiders, glowing starts at 3 left). Raiders that join during the wave count
            # toward it. Whole number from 0 to 100: 0 turns glowing off, 100 makes the whole wave glow from the start.
            glowAtRemainingPercent = 50
            """;

    private ServerConfigFile() {
    }

    static Optional<UnmodifiableConfig> load(Path file) {
        if (!Files.exists(file)) {
            if (write(file)) {
                RaidTracker.LOGGER.info("Created {}", file);
            }
            return parse(file, DEFAULT_TEXT);
        }

        try {
            String text = Files.readString(file, StandardCharsets.UTF_8);
            return parse(file, text.startsWith("﻿") ? text.substring(1) : text);
        } catch (IOException e) {
            RaidTracker.LOGGER.warn("Could not read {}, using the default settings: {}", file, e.getMessage());
            return Optional.empty();
        }
    }

    private static Optional<UnmodifiableConfig> parse(Path file, String text) {
        try {
            return Optional.of(TomlFormat.instance().createParser().parse(text));
        } catch (RuntimeException e) {
            RaidTracker.LOGGER.warn("Could not parse {}, using the default settings: {}", file, e.getMessage());
            return Optional.empty();
        }
    }

    private static boolean write(Path file) {
        try {
            Files.createDirectories(file.getParent());
            Files.writeString(file, DEFAULT_TEXT, StandardCharsets.UTF_8);
            return true;
        } catch (IOException e) {
            RaidTracker.LOGGER.warn("Could not write {}: {}", file, e.getMessage());
            return false;
        }
    }
}
