package org.tomidori.fable.skill.handler;

import org.jetbrains.annotations.ApiStatus;
import org.tomidori.fable.skill.SkillInstance;

import java.util.Objects;

@FunctionalInterface
public interface SkillCancelHandler {
    static SkillCancelHandler alwaysAllow() {
        return instance -> true;
    }

    static SkillCancelHandler alwaysDeny() {
        return instance -> false;
    }

    boolean shouldCancel(SkillInstance instance);

    @ApiStatus.NonExtendable
    default SkillCancelHandler and(SkillCancelHandler other) {
        Objects.requireNonNull(other);
        return context -> shouldCancel(context) && other.shouldCancel(context);
    }

    @ApiStatus.NonExtendable
    default SkillCancelHandler or(SkillCancelHandler other) {
        Objects.requireNonNull(other);
        return context -> shouldCancel(context) || other.shouldCancel(context);
    }
}
