package org.tomidori.fable.skill.handler;

import org.jetbrains.annotations.ApiStatus;
import org.tomidori.fable.skill.SkillInstance;

import java.util.Objects;

@FunctionalInterface
public interface SkillInterruptHandler {
    static SkillInterruptHandler alwaysAllow() {
        return instance -> true;
    }

    static SkillInterruptHandler alwaysDeny() {
        return instance -> false;
    }

    boolean shouldInterrupt(SkillInstance instance);

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
