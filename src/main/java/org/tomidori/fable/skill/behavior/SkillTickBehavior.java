package org.tomidori.fable.skill.behavior;

import org.jetbrains.annotations.ApiStatus;
import org.tomidori.fable.skill.SkillPostContext;

import java.util.Objects;

@FunctionalInterface
public interface SkillTickBehavior<S> {
    static <S> SkillTickBehavior<S> noOp() {
        return context -> {};
    }

    void onTick(SkillPostContext<S> context);

    @ApiStatus.NonExtendable
    default SkillTickBehavior<S> andThen(SkillTickBehavior<S> after) {
        Objects.requireNonNull(after);
        return context -> {
            onTick(context);
            after.onTick(context);
        };
    }
}
