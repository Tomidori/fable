package org.tomidori.fable.skill.manager;

import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.tomidori.fable.skill.Skill;
import org.tomidori.fable.skill.SkillPostContext;
import org.tomidori.fable.skill.SkillPreContext;
import org.tomidori.fable.skill.SkillResponse;
import org.tomidori.fable.skill.state.SkillStateCreationResult;

import java.util.Objects;
import java.util.function.Supplier;

public final class SkillManager {
    private final Supplier<? extends LivingEntity> entitySupplier;
    private @Nullable SkillPostContext<?> postContext;

    @ApiStatus.Internal
    public SkillManager(Supplier<? extends LivingEntity> entitySupplier) {
        this.entitySupplier = Objects.requireNonNull(entitySupplier);
    }

    public boolean isCastingSkill() {
        return postContext != null;
    }

    @SuppressWarnings("unchecked")
    public <S> @Nullable SkillPostContext<S> getPostContext() {
        try {
            return (SkillPostContext<S>) postContext;
        } catch (ClassCastException exception) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public SkillResponse canCastSkill(RegistryEntry<? extends Skill<?>> skill) {
        Objects.requireNonNull(skill);

        RegistryEntry<? extends Skill<Object>> erased = (RegistryEntry<? extends Skill<Object>>) skill;
        return tryTestSkill(erased);
    }

    @SuppressWarnings("unchecked")
    public SkillResponse castSkill(RegistryEntry<? extends Skill<?>> skill) {
        Objects.requireNonNull(skill);

        RegistryEntry<? extends Skill<Object>> erased = (RegistryEntry<? extends Skill<Object>>) skill;
        return tryTestSkill(erased) instanceof SkillResponse.Failure failure ?
                failure :
                tryCastSkill(erased);
    }

    public boolean cancelCastingSkill() {
        return isCastingSkill() &&
                stopCastingSkill(postContext, false);
    }

    public boolean interruptCastingSkill() {
        return isCastingSkill() &&
                stopCastingSkill(postContext, true);
    }

    public boolean terminateCastingSkill() {
        if (!isCastingSkill()) {
            return false;
        }

        endCastingSkill(postContext);
        return true;
    }

    @ApiStatus.Internal
    public void update() {
        if (isCastingSkill()) {
            updateCastingSkill(postContext);
        }
    }

    private <S> SkillResponse tryTestSkill(RegistryEntry<? extends Skill<S>> skill) {
        return skill.value()
                .getCondition().check(createPreContext(skill));
    }

    private <S> SkillResponse tryCastSkill(RegistryEntry<? extends Skill<S>> skill) {
        switch (
                skill.value().getStateFactory().create(createPreContext(skill))
        ) {
            case SkillStateCreationResult.Success<S> success -> {
                beginCastingSkill(createPostContext(skill, success.getState()));
                return SkillResponse.success();
            }
            case SkillStateCreationResult.Failure<S> failure -> {
                return failure.getReason();
            }
        }
    }

    private <S> void beginCastingSkill(SkillPostContext<S> postContext) {
        this.postContext = postContext;
        postContext.getSkill().value().getAttributeModifiers().addModifiers(entitySupplier.get().getAttributes());
        postContext.getSkill().value().getStartBehavior().onStart(postContext);
    }

    private <S> void endCastingSkill(SkillPostContext<S> postContext) {
        postContext.getSkill().value().getEndBehavior().onEnd(postContext);
        postContext.getSkill().value().getAttributeModifiers().removeModifiers(entitySupplier.get().getAttributes());
        this.postContext = null;
    }

    private <S> boolean stopCastingSkill(SkillPostContext<S> postContext, boolean force) {
        boolean shouldStop = force ?
                postContext.getSkill().value().getInterruptHandler().shouldInterrupt(postContext) :
                postContext.getSkill().value().getCancelHandler().shouldCancel(postContext);

        if (!shouldStop) {
            return false;
        }

        endCastingSkill(postContext);
        return true;
    }

    private <S> void updateCastingSkill(SkillPostContext<S> postContext) {
        postContext.setRemainingTicks(postContext.getRemainingTicks() - 1);
        if (postContext.getRemainingTicks() > 0) {
            postContext.getSkill().value().getTickBehavior().onTick(postContext);
        } else {
            postContext.getSkill().value().getCompleteBehavior().onComplete(postContext);
            endCastingSkill(postContext);
        }
    }

    private <S> SkillPreContext createPreContext(RegistryEntry<? extends Skill<S>> skill) {
        return new SkillPreContext(
                skill,
                entitySupplier.get()
        );
    }

    private <S> SkillPostContext<S> createPostContext(RegistryEntry<? extends Skill<S>> skill, S state) {
        return new SkillPostContext<>(
                skill,
                entitySupplier.get(),
                state,
                skill.value().getDurationProvider().get(createPreContext(skill))
        );
    }
}
