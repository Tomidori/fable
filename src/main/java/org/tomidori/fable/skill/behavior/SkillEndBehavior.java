package org.tomidori.fable.skill.behavior;

import org.jetbrains.annotations.ApiStatus;
import org.tomidori.fable.skill.SkillExecutionContext;

import java.util.Objects;

@FunctionalInterface
public interface SkillEndBehavior {
    static SkillEndBehavior noOp() {
        return context -> {};
    }

    void onEnd(SkillExecutionContext context);

    @ApiStatus.NonExtendable
    default SkillEndBehavior andThen(SkillEndBehavior after) {
        Objects.requireNonNull(after);
        return context -> {
            onEnd(context);
            after.onEnd(context);
        };
    }
}
