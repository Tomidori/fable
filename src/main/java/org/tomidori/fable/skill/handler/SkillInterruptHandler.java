package org.tomidori.fable.skill.handler;

import org.jetbrains.annotations.ApiStatus;
import org.tomidori.fable.skill.SkillExecutionContext;

import java.util.Objects;

@FunctionalInterface
public interface SkillInterruptHandler {
    static SkillInterruptHandler alwaysAllow() {
        return context -> true;
    }

    static SkillInterruptHandler alwaysDeny() {
        return context -> false;
    }

    boolean shouldInterrupt(SkillExecutionContext context);

    @ApiStatus.NonExtendable
    default SkillInterruptHandler and(SkillInterruptHandler other) {
        Objects.requireNonNull(other);
        return context -> shouldInterrupt(context) && other.shouldInterrupt(context);
    }

    @ApiStatus.NonExtendable
    default SkillInterruptHandler or(SkillInterruptHandler other) {
        Objects.requireNonNull(other);
        return context -> shouldInterrupt(context) || other.shouldInterrupt(context);
    }
}
