package org.tomidori.fable.skill.manager;

import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.tomidori.fable.core.AttributeBag;
import org.tomidori.fable.skill.Skill;
import org.tomidori.fable.skill.SkillContext;
import org.tomidori.fable.skill.SkillResponse;

import java.util.Objects;
import java.util.function.Supplier;

public final class SkillManager {
    private final Supplier<LivingEntity> entitySupplier;
    private @Nullable SkillContext context;

    @ApiStatus.Internal
    public SkillManager(Supplier<LivingEntity> entitySupplier) {
        this.entitySupplier = Objects.requireNonNull(entitySupplier);
    }

    public boolean isCastingSkill() {
        return context != null;
    }

    public @Nullable SkillContext getSkillContext() {
        return context;
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

        beginCastingSkill(skillContext(skill));
        return response;
    }

    public boolean cancelCastingSkill() {
        return isCastingSkill() &&
                stopCastingSkill(context, false);
    }

    public boolean interruptCastingSkill() {
        return isCastingSkill() &&
                stopCastingSkill(context, true);
    }

    public boolean terminateCastingSkill() {
        if (!isCastingSkill()) {
            return false;
        }

        endCastingSkill(context);
        return true;
    }

    @ApiStatus.Internal
    public void update() {
        if (isCastingSkill()) {
            updateCastingSkill(context);
        }
    }

    private void beginCastingSkill(SkillContext context) {
        this.context = context;
        context.getSkill().value().getStartBehavior().onStart(context);
    }

    private void endCastingSkill(SkillContext context) {
        context.getSkill().value().getEndBehavior().onEnd(context);
        this.context = null;
    }

    private boolean stopCastingSkill(SkillContext context, boolean force) {
        boolean shouldStop = force ?
                context.getSkill().value().getInterruptHandler().shouldInterrupt(context) :
                context.getSkill().value().getCancelHandler().shouldCancel(context);

        if (!shouldStop) {
            return false;
        }

        endCastingSkill(context);
        return true;
    }

    private void updateCastingSkill(SkillContext context) {
        context.setDuration(context.getDuration() - 1);
        if (context.getDuration() > 0) {
            context.getSkill().value().getTickBehavior().onTick(context);
        } else {
            context.getSkill().value().getCompleteBehavior().onComplete(context);
            endCastingSkill(context);
        }
    }

    private SkillContext skillContext(RegistryEntry<Skill> skill) {
        return new SkillContext(skill, entitySupplier.get(), AttributeBag.create(), skill.value().getInitialDuration());
    }
}
