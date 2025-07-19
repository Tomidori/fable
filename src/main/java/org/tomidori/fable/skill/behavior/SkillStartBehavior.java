package org.tomidori.fable.skill.behavior;

import org.jetbrains.annotations.ApiStatus;
import org.tomidori.fable.skill.SkillPostContext;

import java.util.Objects;

@FunctionalInterface
public interface SkillStartBehavior<S> {
    static <S> SkillStartBehavior<S> noOp() {
        return context -> {};
    }

    void onStart(SkillPostContext<S> context);

    @ApiStatus.NonExtendable
    default SkillStartBehavior<S> andThen(SkillStartBehavior<S> after) {
        Objects.requireNonNull(after);
        return context -> {
            onStart(context);
            after.onStart(context);
        };
    }
}
