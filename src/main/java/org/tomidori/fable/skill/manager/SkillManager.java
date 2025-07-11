package org.tomidori.fable.skill.manager;

import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.tomidori.fable.core.AttributeBag;
import org.tomidori.fable.skill.Skill;
import org.tomidori.fable.skill.SkillContext;
import org.tomidori.fable.skill.SkillInstance;
import org.tomidori.fable.skill.SkillResponse;

import java.util.Objects;
import java.util.function.Supplier;

public final class SkillManager {
    private final Supplier<LivingEntity> entitySupplier;

    private @Nullable SkillInstance skillInstance;

    @ApiStatus.Internal
    public SkillManager(Supplier<LivingEntity> entitySupplier) {
        this.entitySupplier = Objects.requireNonNull(entitySupplier);
    }

    public boolean isCastingSkill() {
        return skillInstance != null;
    }

    public @Nullable SkillInstance getSkillInstance() {
        return skillInstance;
    }

    public SkillResponse canCastSkill(RegistryEntry<Skill> skill) {
        Objects.requireNonNull(skill);
        return skill.value().getCondition().check(skillContext(skill));
    }

    public SkillResponse castSkill(RegistryEntry<Skill> skill) {
        Objects.requireNonNull(skill);
        SkillResponse response = canCastSkill(skill);
        if (response instanceof SkillResponse.Failure) {
            return response;
        }

        if (isCastingSkill() && !cancelCastingSkill()) {
            return SkillResponse.inProgress();
        }

        beginCastingSkill(skillInstance(skill));
        return response;
    }

    public boolean cancelCastingSkill() {
        return isCastingSkill() &&
                stopCastingSkill(skillInstance, false);
    }

    public boolean interruptCastingSkill() {
        return isCastingSkill() &&
                stopCastingSkill(skillInstance, true);
    }

    public boolean terminateCastingSkill() {
        if (!isCastingSkill()) {
            return false;
        }

        endCastingSkill(skillInstance);
        return true;
    }

    @ApiStatus.Internal
    public void update() {
        if (isCastingSkill()) {
            updateCastingSkill(skillInstance);
        }
    }

    private void beginCastingSkill(SkillInstance skillInstance) {
        this.skillInstance = skillInstance;
        skillInstance.getSkill().value().getStartBehavior().onStart(skillInstance);
    }

    private void endCastingSkill(SkillInstance skillInstance) {
        skillInstance.getSkill().value().getEndBehavior().onEnd(skillInstance);
        this.skillInstance = null;
    }

    private boolean stopCastingSkill(SkillInstance skillInstance, boolean force) {
        boolean shouldStop = force ?
                skillInstance.getSkill().value().getInterruptHandler().shouldInterrupt(skillInstance) :
                skillInstance.getSkill().value().getCancelHandler().shouldCancel(skillInstance);

        if (!shouldStop) {
            return false;
        }

        endCastingSkill(skillInstance);
        return true;
    }

    private void updateCastingSkill(SkillInstance skillInstance) {
        skillInstance.setDuration(skillInstance.getDuration() - 1);
        if (skillInstance.getDuration() > 0) {
            skillInstance.getSkill().value().getTickBehavior().onTick(skillInstance);
        } else {
            skillInstance.getSkill().value().getCompleteBehavior().onComplete(skillInstance);
            endCastingSkill(skillInstance);
        }
    }

    private SkillContext skillContext(RegistryEntry<Skill> skill) {
        return new SkillContext.Impl(skill, entitySupplier.get());
    }

    private SkillInstance skillInstance(RegistryEntry<Skill> skill) {
        return new SkillInstance.Impl(skillContext(skill), AttributeBag.create(), skill.value().getInitialDuration());
    }
}
