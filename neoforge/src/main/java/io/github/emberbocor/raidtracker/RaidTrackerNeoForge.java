package io.github.emberbocor.raidtracker;

import io.github.emberbocor.raidtracker.config.ServerConfig;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;

@Mod(RaidTracker.MODID)
public class RaidTrackerNeoForge {
    public RaidTrackerNeoForge() {
        NeoForge.EVENT_BUS.addListener((ServerAboutToStartEvent event) -> ServerConfig.load());
    }
}
