package org.tomidori.fable.mixin;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.tomidori.fable.entity.LivingEntityHook;

@Mixin(value = Entity.class)
public abstract class EntityMixin {
    @Inject(method = "copyFrom", at = @At(value = "TAIL"))
    private void fable$copyFrom(Entity original, CallbackInfo ci) {
        if (this instanceof LivingEntityHook && original instanceof LivingEntityHook) {
            ((LivingEntityHook) this).setSkillCooldownManager(((LivingEntityHook) original).getSkillCooldownManager());
        }
    }
}
