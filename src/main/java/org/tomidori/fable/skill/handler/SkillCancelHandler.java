package org.tomidori.fable.skill.handler;

import org.jetbrains.annotations.ApiStatus;
import org.tomidori.fable.skill.SkillInstance;

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
        return context -> shouldCancel(context) && other.shouldCancel(context);
    }

    @ApiStatus.NonExtendable
    default SkillCancelHandler or(SkillCancelHandler other) {
        return context -> shouldCancel(context) || other.shouldCancel(context);
    }
}
