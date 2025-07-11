package org.tomidori.fable.skill.behavior;

import org.jetbrains.annotations.ApiStatus;
import org.tomidori.fable.skill.SkillExecutionContext;

import java.util.Objects;

@FunctionalInterface
public interface SkillCompleteBehavior {
    static SkillCompleteBehavior noOp() {
        return context -> {};
    }

    void onComplete(SkillExecutionContext context);

    @ApiStatus.NonExtendable
    default SkillCompleteBehavior andThen(SkillCompleteBehavior after) {
        Objects.requireNonNull(after);
        return context -> {
            onComplete(context);
            after.onComplete(context);
        };
    }
}
