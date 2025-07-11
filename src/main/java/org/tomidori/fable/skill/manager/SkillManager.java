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

    private @Nullable SkillExecutionContext skillExecutionContext;

    @ApiStatus.Internal
    public SkillManager(Supplier<LivingEntity> entitySupplier) {
        this.entitySupplier = Objects.requireNonNull(entitySupplier);
    }

    public boolean isCastingSkill() {
        return skillExecutionContext != null;
    }

    public @Nullable SkillExecutionContext getSkillExecutionContext() {
        return skillExecutionContext;
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
                stopCastingSkill(skillExecutionContext, false);
    }

    public boolean interruptCastingSkill() {
        return isCastingSkill() &&
                stopCastingSkill(skillExecutionContext, true);
    }

    public boolean terminateCastingSkill() {
        if (!isCastingSkill()) {
            return false;
        }

        endCastingSkill(skillExecutionContext);
        return true;
    }

    @ApiStatus.Internal
    public void update() {
        if (isCastingSkill()) {
            updateCastingSkill(skillExecutionContext);
        }
    }

    private void beginCastingSkill(SkillExecutionContext skillExecutionContext) {
        this.skillExecutionContext = skillExecutionContext;
        skillExecutionContext.getSkill().value().getStartBehavior().onStart(skillExecutionContext);
    }

    private void endCastingSkill(SkillExecutionContext skillExecutionContext) {
        skillExecutionContext.getSkill().value().getEndBehavior().onEnd(skillExecutionContext);
        this.skillExecutionContext = null;
    }

    private boolean stopCastingSkill(SkillExecutionContext skillExecutionContext, boolean force) {
        boolean shouldStop = force ?
                skillExecutionContext.getSkill().value().getInterruptHandler().shouldInterrupt(skillExecutionContext) :
                skillExecutionContext.getSkill().value().getCancelHandler().shouldCancel(skillExecutionContext);

        if (!shouldStop) {
            return false;
        }

        endCastingSkill(skillExecutionContext);
        return true;
    }

    private void updateCastingSkill(SkillExecutionContext skillExecutionContext) {
        skillExecutionContext.setDuration(skillExecutionContext.getDuration() - 1);
        if (skillExecutionContext.getDuration() > 0) {
            skillExecutionContext.getSkill().value().getTickBehavior().onTick(skillExecutionContext);
        } else {
            skillExecutionContext.getSkill().value().getCompleteBehavior().onComplete(skillExecutionContext);
            endCastingSkill(skillExecutionContext);
        }
    }

    private SkillContext skillContext(RegistryEntry<Skill> skill) {
        return new SkillContext.Impl(skill, entitySupplier.get());
    }

    private SkillExecutionContext skillExecutionContext(RegistryEntry<Skill> skill) {
        return new SkillExecutionContext.Impl(skillContext(skill), AttributeBag.create(), skill.value().getInitialDuration());
    }
}
