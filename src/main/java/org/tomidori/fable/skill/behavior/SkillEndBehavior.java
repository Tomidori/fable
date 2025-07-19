package org.tomidori.fable.skill.behavior;

import org.jetbrains.annotations.ApiStatus;
import org.tomidori.fable.skill.SkillPostContext;

import java.util.Objects;

@FunctionalInterface
public interface SkillEndBehavior<S> {
    static <S> SkillEndBehavior<S> noOp() {
        return context -> {};
    }

    void onEnd(SkillPostContext<S> context);

    @ApiStatus.NonExtendable
    default SkillEndBehavior<S> andThen(SkillEndBehavior<S> after) {
        Objects.requireNonNull(after);
        return context -> {
            onEnd(context);
            after.onEnd(context);
        };
    }
}
