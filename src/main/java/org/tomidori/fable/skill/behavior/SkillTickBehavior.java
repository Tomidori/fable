package org.tomidori.fable.skill.behavior;

import org.jetbrains.annotations.ApiStatus;
import org.tomidori.fable.skill.SkillExecutionContext;

import java.util.Objects;

@FunctionalInterface
public interface SkillTickBehavior {
    static SkillTickBehavior noOp() {
        return context -> {};
    }

    void onTick(SkillExecutionContext context);

    @ApiStatus.NonExtendable
    default SkillTickBehavior andThen(SkillTickBehavior after) {
        Objects.requireNonNull(after);
        return context -> {
            onTick(context);
            after.onTick(context);
        };
    }
}
