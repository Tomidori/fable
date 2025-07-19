package org.tomidori.fable.skill.behavior;

import org.jetbrains.annotations.ApiStatus;
import org.tomidori.fable.skill.SkillPostContext;

import java.util.Objects;

@FunctionalInterface
public interface SkillCompleteBehavior<S> {
    static <S> SkillCompleteBehavior<S> noOp() {
        return context -> {};
    }

    void onComplete(SkillPostContext<S> context);

    @ApiStatus.NonExtendable
    default SkillCompleteBehavior<S> andThen(SkillCompleteBehavior<S> after) {
        Objects.requireNonNull(after);
        return context -> {
            onComplete(context);
            after.onComplete(context);
        };
    }
}
