package org.tomidori.fable.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.tomidori.fable.entity.LivingEntityHook;

@Mixin(value = ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {
    @Inject(method = "onDeath", at = @At(value = "TAIL"))
    private void fable$onDeath(DamageSource source, CallbackInfo ci) {
        ((LivingEntityHook) this).getSkillManager().terminateCastingSkill();
    }

    @Inject(method = "onDisconnect", at = @At(value = "TAIL"))
    private void fable$onDisconnect(CallbackInfo ci) {
        ((LivingEntityHook) this).getSkillManager().terminateCastingSkill();
    }

    @Inject(method = "copyFrom", at = @At(value = "TAIL"))
    private void fable$copyFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        if (!(this instanceof LivingEntityHook hook)) {
            return;
        }

        hook.setSkillContainer(oldPlayer.getSkillContainer());
        hook.setSkillCooldownManager(oldPlayer.getSkillCooldownManager());
    }
}
