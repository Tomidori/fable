package org.tomidori.fable.skill.handler;

import org.jetbrains.annotations.ApiStatus;
import org.tomidori.fable.skill.SkillPostContext;

import java.util.Objects;

@FunctionalInterface
public interface SkillInterruptHandler<S> {
    static <S> SkillInterruptHandler<S> alwaysAllow() {
        return context -> true;
    }

    static <S> SkillInterruptHandler<S> alwaysDeny() {
        return context -> false;
    }

    boolean shouldInterrupt(SkillPostContext<S> context);

    @ApiStatus.NonExtendable
    default SkillInterruptHandler<S> and(SkillInterruptHandler<S> other) {
        Objects.requireNonNull(other);
        return context -> shouldInterrupt(context) && other.shouldInterrupt(context);
    }

    @ApiStatus.NonExtendable
    default SkillInterruptHandler<S> or(SkillInterruptHandler<S> other) {
        Objects.requireNonNull(other);
        return context -> shouldInterrupt(context) || other.shouldInterrupt(context);
    }
}
