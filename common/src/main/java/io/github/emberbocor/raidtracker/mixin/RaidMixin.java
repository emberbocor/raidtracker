package io.github.emberbocor.raidtracker.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.emberbocor.raidtracker.raid.RaidBossBar;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;

@Mixin(Raid.class)
public abstract class RaidMixin {
    @Shadow
    @Final
    private static Component RAID_NAME_COMPONENT;

    @Shadow
    @Final
    private ServerBossEvent raidEvent;

    @Shadow
    @Final
    private int numGroups;

    @Unique
    private boolean raidtracker$customName;

    @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerBossEvent;setName(Lnet/minecraft/network/chat/Component;)V"))
    private Component raidtracker$replaceVanillaName(Component name) {
        Component custom = RaidBossBar.name((Raid) (Object) this, numGroups);
        return custom != null ? custom : name;
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void raidtracker$updateNameAfterTick(CallbackInfo ci) {
        raidtracker$updateName();
    }

    @Inject(method = "removeFromRaid", at = @At("TAIL"))
    private void raidtracker$updateNameAfterRemoval(Raider raider, boolean wanderedOutOfRaid, CallbackInfo ci) {
        raidtracker$updateName();
    }

    @Unique
    private void raidtracker$updateName() {
        Component custom = RaidBossBar.name((Raid) (Object) this, numGroups);
        if (custom != null) {
            raidEvent.setName(custom);
            raidtracker$customName = true;
        } else if (raidtracker$customName) {
            raidEvent.setName(RAID_NAME_COMPONENT);
            raidtracker$customName = false;
        }
    }
}
