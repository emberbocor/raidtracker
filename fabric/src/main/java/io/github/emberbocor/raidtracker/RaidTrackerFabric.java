package io.github.emberbocor.raidtracker;

import io.github.emberbocor.raidtracker.config.ServerConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class RaidTrackerFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> ServerConfig.load());
    }
}
