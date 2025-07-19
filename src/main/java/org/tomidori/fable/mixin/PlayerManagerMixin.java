package org.tomidori.fable.mixin;

import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PlayerManager.class)
public abstract class PlayerManagerMixin {
    @Inject(method = "remove", at = @At(value = "HEAD"))
    private void firePlayerLeaveEvent(ServerPlayerEntity player, CallbackInfo ci) {
        if (player.getSkillManager().isCastingSkill() && !player.getSkillManager().cancelCastingSkill()) {
            player.getSkillManager().terminateCastingSkill();
        }
    }
}
