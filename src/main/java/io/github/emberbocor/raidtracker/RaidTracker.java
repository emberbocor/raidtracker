package io.github.emberbocor.raidtracker;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

@Mod(RaidTracker.MODID)
public class RaidTracker {
    public static final String MODID = "raidtracker";
    public static final Logger LOGGER = LogUtils.getLogger();

    public RaidTracker(IEventBus modEventBus, ModContainer modContainer) {
    }
}
