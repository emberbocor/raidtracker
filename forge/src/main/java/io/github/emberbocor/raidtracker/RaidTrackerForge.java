package io.github.emberbocor.raidtracker;

import io.github.emberbocor.raidtracker.config.ServerConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(RaidTracker.MODID)
public class RaidTrackerForge {
    public RaidTrackerForge() {
        MinecraftForge.EVENT_BUS.addListener((ServerAboutToStartEvent event) -> ServerConfig.load());
    }
}
