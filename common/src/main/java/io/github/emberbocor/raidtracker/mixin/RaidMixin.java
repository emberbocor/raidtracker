package io.github.emberbocor.raidtracker.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.serialization.MapCodec;

import io.github.emberbocor.raidtracker.raid.RaidBossBar;
import io.github.emberbocor.raidtracker.raid.RaidGlow;
import io.github.emberbocor.raidtracker.raid.RaidWave;
import io.github.emberbocor.raidtracker.raid.RaidWaveHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;

@Mixin(Raid.class)
public abstract class RaidMixin implements RaidWaveHolder {
    @Shadow
    @Final
    @Mutable
    public static MapCodec<Raid> MAP_CODEC;

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

    @Unique
    private RaidWave raidtracker$wave = new RaidWave();

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void raidtracker$saveWaveWithRaid(CallbackInfo ci) {
        MAP_CODEC = RaidWave.withWave(MAP_CODEC);
    }

    @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerBossEvent;setName(Lnet/minecraft/network/chat/Component;)V"))
    private Component raidtracker$replaceVanillaName(Component name) {
        Component custom = RaidBossBar.name((Raid) (Object) this, numGroups);
        return custom != null ? custom : name;
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void raidtracker$afterTick(ServerLevel level, CallbackInfo ci) {
        raidtracker$updateName();
        RaidGlow.tick((Raid) (Object) this, raidtracker$wave.size());
    }

    @Inject(method = "removeFromRaid", at = @At("TAIL"))
    private void raidtracker$updateNameAfterRemoval(ServerLevel level, Raider raider, boolean wanderedOutOfRaid, CallbackInfo ci) {
        raidtracker$updateName();
    }

    @Inject(method = "addWaveMob(Lnet/minecraft/server/level/ServerLevel;ILnet/minecraft/world/entity/raid/Raider;Z)Z", at = @At("TAIL"))
    private void raidtracker$countWaveMember(ServerLevel level, int wave, Raider raider, boolean isRecruited, CallbackInfoReturnable<Boolean> cir) {
        raidtracker$wave.add(wave, raider.getUUID());
    }

    @Override
    public RaidWave raidtracker$getWave() {
        return raidtracker$wave;
    }

    @Override
    public void raidtracker$setWave(RaidWave wave) {
        raidtracker$wave = wave;
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
