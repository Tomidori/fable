package org.tomidori.fable.mixin;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.tomidori.fable.entity.LivingEntityHook;
import org.tomidori.fable.skill.manager.SkillManager;

@Mixin(value = LivingEntity.class)
public abstract class LivingEntityMixin implements LivingEntityHook {
    @Unique
    private final SkillManager fable$skillManager = new SkillManager(this::fable$livingEntity);

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public SkillManager getSkillManager() {
        return fable$skillManager;
    }

    @Unique
    private LivingEntity fable$livingEntity() {
        return (LivingEntity) (Object) this;
    }

    @Inject(method = "tick", at = @At(value = "TAIL"))
    private void fable$tick(CallbackInfo ci) {
        fable$skillManager.update();
    }
}
