package org.tomidori.fable.skill.behavior;

import org.jetbrains.annotations.ApiStatus;
import org.tomidori.fable.skill.SkillContext;

import java.util.Objects;

@FunctionalInterface
public interface SkillStartBehavior {
    static SkillStartBehavior noOp() {
        return context -> {};
    }

    void onStart(SkillContext context);

    @ApiStatus.NonExtendable
    default SkillStartBehavior andThen(SkillStartBehavior after) {
        Objects.requireNonNull(after);
        return context -> {
            onStart(context);
            after.onStart(context);
        };
    }
}
