package org.tomidori.fable.skill.manager;

import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.tomidori.fable.core.AttributeBag;
import org.tomidori.fable.skill.Skill;
import org.tomidori.fable.skill.SkillContext;
import org.tomidori.fable.skill.SkillExecutionContext;
import org.tomidori.fable.skill.SkillResponse;

import java.util.Objects;
import java.util.function.Supplier;

public final class SkillManager {
    private final Supplier<LivingEntity> entitySupplier;

    private @Nullable SkillExecutionContext executionContext;

    @ApiStatus.Internal
    public SkillManager(Supplier<LivingEntity> entitySupplier) {
        this.entitySupplier = Objects.requireNonNull(entitySupplier);
    }

    public boolean isCastingSkill() {
        return executionContext != null;
    }

    public @Nullable SkillExecutionContext getSkillExecutionContext() {
        return executionContext;
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

        beginCastingSkill(skillExecutionContext(skill));
        return response;
    }

    public boolean cancelCastingSkill() {
        return isCastingSkill() &&
                stopCastingSkill(executionContext, false);
    }

    public boolean interruptCastingSkill() {
        return isCastingSkill() &&
                stopCastingSkill(executionContext, true);
    }

    public boolean terminateCastingSkill() {
        if (!isCastingSkill()) {
            return false;
        }

        endCastingSkill(executionContext);
        return true;
    }

    @ApiStatus.Internal
    public void update() {
        if (isCastingSkill()) {
            updateCastingSkill(executionContext);
        }
    }

    private void beginCastingSkill(SkillExecutionContext executionContext) {
        this.executionContext = executionContext;
        executionContext.getSkill().value().getStartBehavior().onStart(executionContext);
    }

    private void endCastingSkill(SkillExecutionContext executionContext) {
        executionContext.getSkill().value().getEndBehavior().onEnd(executionContext);
        this.executionContext = null;
    }

    private boolean stopCastingSkill(SkillExecutionContext executionContext, boolean force) {
        boolean shouldStop = force ?
                executionContext.getSkill().value().getInterruptHandler().shouldInterrupt(executionContext) :
                executionContext.getSkill().value().getCancelHandler().shouldCancel(executionContext);

        if (!shouldStop) {
            return false;
        }

        endCastingSkill(executionContext);
        return true;
    }

    private void updateCastingSkill(SkillExecutionContext executionContext) {
        executionContext.setDuration(executionContext.getDuration() - 1);
        if (executionContext.getDuration() > 0) {
            executionContext.getSkill().value().getTickBehavior().onTick(executionContext);
        } else {
            executionContext.getSkill().value().getCompleteBehavior().onComplete(executionContext);
            endCastingSkill(executionContext);
        }
    }

    private SkillContext skillContext(RegistryEntry<Skill> skill) {
        return new SkillContext.Impl(skill, entitySupplier.get());
    }

    private SkillExecutionContext skillExecutionContext(RegistryEntry<Skill> skill) {
        return new SkillExecutionContext.Impl(skillContext(skill), AttributeBag.create(), skill.value().getInitialDuration());
    }
}
