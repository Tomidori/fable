package org.tomidori.fable.mixin;

import com.google.common.base.Suppliers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.tomidori.fable.entity.LivingEntityHook;
import org.tomidori.fable.skill.manager.SkillContainer;
import org.tomidori.fable.skill.manager.SkillCooldownManager;
import org.tomidori.fable.skill.manager.SkillManager;

import java.util.Objects;

@Mixin(value = LivingEntity.class)
public abstract class LivingEntityMixin implements LivingEntityHook {
    @Unique
    private final SkillManager fable$skillManager = new SkillManager(Suppliers.memoize(this::fable$livingEntity));
    @Unique
    private SkillContainer fable$skillContainer = new SkillContainer();
    @Unique
    private SkillCooldownManager fable$skillCooldownManager = new SkillCooldownManager();

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public SkillManager getSkillManager() {
        return fable$skillManager;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public SkillContainer getSkillContainer() {
        return fable$skillContainer;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public void setSkillContainer(SkillContainer skillContainer) {
        fable$skillContainer = Objects.requireNonNull(skillContainer);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public SkillCooldownManager getSkillCooldownManager() {
        return fable$skillCooldownManager;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public void setSkillCooldownManager(SkillCooldownManager skillCooldownManager) {
        fable$skillCooldownManager = Objects.requireNonNull(skillCooldownManager);
    }

    @Inject(method = "tick", at = @At(value = "TAIL"))
    private void fable$tick(CallbackInfo ci) {
        fable$skillCooldownManager.update();
        fable$skillManager.update();
    }

    @Inject(method = "onDeath", at = @At(value = "INVOKE", target = "net/minecraft/world/World.sendEntityStatus(Lnet/minecraft/entity/Entity;B)V"))
    private void fable$onDeath(DamageSource damageSource, CallbackInfo ci) {
        fable$skillManager.terminateCastingSkill();
    }

    @Unique
    private LivingEntity fable$livingEntity() {
        return (LivingEntity) (Object) this;
    }
}
