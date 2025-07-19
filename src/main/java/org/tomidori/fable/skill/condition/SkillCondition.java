package org.tomidori.fable.skill.condition;

import org.jetbrains.annotations.ApiStatus;
import org.tomidori.fable.skill.SkillPreContext;
import org.tomidori.fable.skill.SkillResponse;

import java.util.Objects;

@FunctionalInterface
public interface SkillCondition {
    static SkillCondition alwaysSuccess() {
        return context -> SkillResponse.success();
    }

    static SkillCondition alwaysUnavailable() {
        return context -> SkillResponse.unavailable();
    }

    static SkillCondition requireCooldownReady() {
        return context -> context.getSource().getSkillCooldownManager().isCoolingDown(context.getSkill()) ?
                SkillResponse.onCooldown() :
                SkillResponse.success();
    }

    static SkillCondition requireNotCasting() {
        return context -> context.getSource().getSkillManager().isCastingSkill() ?
                SkillResponse.inProgress() :
                SkillResponse.success();
    }

    static SkillCondition requireSkillLearned() {
        return context -> context.getSource().getSkillContainer().hasSkill(context.getSkill()) ?
                SkillResponse.success() :
                SkillResponse.notLearned();
    }

    static SkillCondition requireInGame() {
        return context -> context.getSource().isPartOfGame() ?
                SkillResponse.success() :
                SkillResponse.unavailable();
    }

    static SkillCondition defaultConditions() {
        return requireCooldownReady()
                .and(requireNotCasting())
                .and(requireSkillLearned())
                .and(requireInGame());
    }

    SkillResponse check(SkillPreContext context);

    @ApiStatus.NonExtendable
    default SkillCondition and(SkillCondition other) {
        Objects.requireNonNull(other);
        return context -> check(context) instanceof SkillResponse.Failure failure ?
                failure :
                other.check(context);
    }

    @ApiStatus.NonExtendable
    default SkillCondition or(SkillCondition other) {
        Objects.requireNonNull(other);
        return context -> check(context) instanceof SkillResponse.Success success ?
                success :
                other.check(context);
    }
}
