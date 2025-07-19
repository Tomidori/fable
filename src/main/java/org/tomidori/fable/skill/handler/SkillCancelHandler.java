package org.tomidori.fable.skill.handler;

import org.jetbrains.annotations.ApiStatus;
import org.tomidori.fable.skill.SkillPostContext;

import java.util.Objects;

@FunctionalInterface
public interface SkillCancelHandler<S> {
    static <S> SkillCancelHandler<S> alwaysAllow() {
        return context -> true;
    }

    static <S> SkillCancelHandler<S> alwaysDeny() {
        return context -> false;
    }

    boolean shouldCancel(SkillPostContext<S> context);

    @ApiStatus.NonExtendable
    default SkillCancelHandler<S> and(SkillCancelHandler<S> other) {
        Objects.requireNonNull(other);
        return context -> shouldCancel(context) && other.shouldCancel(context);
    }

    @ApiStatus.NonExtendable
    default SkillCancelHandler<S> or(SkillCancelHandler<S> other) {
        Objects.requireNonNull(other);
        return context -> shouldCancel(context) || other.shouldCancel(context);
    }
}
