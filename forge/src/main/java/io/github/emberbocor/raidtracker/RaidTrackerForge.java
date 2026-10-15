package io.github.emberbocor.raidtracker;

import io.github.emberbocor.raidtracker.config.ServerConfig;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(RaidTracker.MODID)
public class RaidTrackerForge {
    public RaidTrackerForge() {
        ServerAboutToStartEvent.BUS.addListener(event -> ServerConfig.load());
    }
}
